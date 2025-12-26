package com.easyway.business.framework.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;

/**
 * 高性能ID锁
 * 
 * 特性：无锁设计、内存池、快速路径优化、自适应策略
 * 
 * // 1. 对于极高并发场景
 * MillionIdLock<String> lock = new MillionIdLock<>(4096);
 * 
 * // 2. 批量操作优化
 * String[] resources = {"a", "b", "c"};
 * // 按段排序减少死锁
 * Arrays.sort(resources, Comparator.comparingInt(System::identityHashCode));
 * for (String res : resources) {
 *     lock.lock(res);
 * }
 * 
 * // 3. 监控和调优
 * // 使用tryLock控制超时，避免死锁
 * if (lock.tryLock(resource, 100, TimeUnit.MILLISECONDS)) {
 *     try {
 *         // 临界区
 *     } finally {
 *         lock.unlock(resource);
 *     }
 * }
 */
@SuppressWarnings("all")
public final class IdLock<T> {
    
    // ==================== 配置常量 ====================
    private static final int DEFAULT_CONCURRENCY = 256;
    private static final int MAX_SEGMENTS = 65536;         // 最大分段数
    private static final int INITIAL_SPIN_LIMIT = 64;      // 初始自旋次数
    private static final int MAX_SPIN_LIMIT = 1024;        // 最大自旋次数
    private static final long PARK_TIMEOUT_NS = 1000L;     // park超时时间
    private static final boolean USE_BACKOFF = true;       // 使用指数退避
    private static final int BACKOFF_BASE = 100;           // 退避基数（纳秒）
    private static final int BACKOFF_MAX = 10000;          // 最大退避时间
    
    // ==================== 核心数据结构 ====================
    private final Segment<T>[] segments;
    private final int segmentShift;
    private final int segmentMask;
    
    // ThreadLocal优化 - 使用自定义的快速ThreadLocal
    private static final ThreadLocal<FastThreadLocalState> localState = 
        new ThreadLocal<>();
    
    // 内存池 - 减少对象创建
    private final EntryPool entryPool;
    
    // 性能统计（可选）
    private final AdaptiveSpinner[] spinners;
    
    public IdLock() {
        this(DEFAULT_CONCURRENCY);
    }
    
    public IdLock(int concurrencyLevel) {
        // 找到最接近的2的幂
        int segmentsCount = 1;
        while (segmentsCount < concurrencyLevel && segmentsCount < MAX_SEGMENTS) {
            segmentsCount <<= 1;
        }
        
        this.segments = new Segment[segmentsCount];
        this.segmentMask = segmentsCount - 1;
        this.segmentShift = 32 - Integer.numberOfTrailingZeros(segmentsCount);
        
        // 初始化段
        for (int i = 0; i < segmentsCount; i++) {
            segments[i] = new Segment<>();
        }
        
        // 初始化内存池
        this.entryPool = new EntryPool(segmentsCount * 4);
        
        // 初始化自适应自旋器
        this.spinners = new AdaptiveSpinner[segmentsCount];
        for (int i = 0; i < segmentsCount; i++) {
            spinners[i] = new AdaptiveSpinner();
        }
    }
    
    // ==================== 公共API ====================
    
    /**
     * 获取锁（阻塞）
     */
    public void lock(T id) throws InterruptedException {
        Thread thread = Thread.currentThread();
        FastThreadLocalState state = getThreadLocalState();
        
        // 快速路径1：重入检查（使用本地缓存）
        int reentrantCount = state.getReentrantCount(id);
        if (reentrantCount > 0) {
            state.recordLock(id, reentrantCount + 1);
            return;
        }
        
        // 获取对应的段
        Segment<T> segment = segmentFor(id);
        
        // 快速路径2：无竞争获取
        LockEntry entry = segment.tryFastLock(id, thread, state);
        if (entry != null) {
            state.recordLock(id, 1);
            return;
        }
        
        // 慢速路径：完整获取流程
        segment.lock(id, thread, state);
        state.recordLock(id, 1);
    }
    
    /**
     * 尝试非阻塞获取锁
     */
    public boolean tryLock(T id) {
        Thread thread = Thread.currentThread();
        FastThreadLocalState state = getThreadLocalState();
        
        int reentrantCount = state.getReentrantCount(id);
        if (reentrantCount > 0) {
            state.recordLock(id, reentrantCount + 1);
            return true;
        }
        
        Segment<T> segment = segmentFor(id);
        LockEntry entry = segment.tryFastLock(id, thread, state);
        
        if (entry != null) {
            state.recordLock(id, 1);
            return true;
        }
        
        return false;
    }
    
    /**
     * 带超时的尝试获取锁
     */
    public boolean tryLock(T id, long timeout, TimeUnit unit) throws InterruptedException {
        long nanosTimeout = unit.toNanos(timeout);
        Thread thread = Thread.currentThread();
        FastThreadLocalState state = getThreadLocalState();
        
        int reentrantCount = state.getReentrantCount(id);
        if (reentrantCount > 0) {
            state.recordLock(id, reentrantCount + 1);
            return true;
        }
        
        Segment<T> segment = segmentFor(id);
        
        // 快速尝试
        LockEntry entry = segment.tryFastLock(id, thread, state);
        if (entry != null) {
            state.recordLock(id, 1);
            return true;
        }
        
        // 带超时的尝试
        if (segment.tryLock(id, thread, state, nanosTimeout)) {
            state.recordLock(id, 1);
            return true;
        }
        
        return false;
    }
    
    /**
     * 释放锁
     */
    public void unlock(T id) {
        Thread thread = Thread.currentThread();
        FastThreadLocalState state = getThreadLocalState();
        
        int newCount = state.recordUnlock(id);
        
        // 如果还有重入计数，不真正释放物理锁
        if (newCount > 0) {
            return;
        }
        
        // 完全释放物理锁
        Segment<T> segment = segmentFor(id);
        segment.unlock(id, thread);
    }
    
    // ==================== 内部辅助方法 ====================
    
    private Segment<T> segmentFor(T id) {
        // 使用身份哈希和段掩码计算段索引
        int hash = System.identityHashCode(id);
        int index = (hash >>> segmentShift) & segmentMask;
        return segments[index];
    }
    
    private FastThreadLocalState getThreadLocalState() {
        FastThreadLocalState state = localState.get();
        if (state == null) {
            state = new FastThreadLocalState();
            localState.set(state);
        }
        return state;
    }
    
    // ==================== 段实现 ====================
    
    private final class Segment<T> {
        // 使用固定大小的开放寻址哈希表
        private static final int TABLE_SIZE = 256;
        private final AtomicReference<LockEntry>[] table;
        private final AdaptiveSpinner spinner;
        
        Segment() {
            table = new AtomicReference[TABLE_SIZE];
            for (int i = 0; i < TABLE_SIZE; i++) {
                table[i] = new AtomicReference<>();
            }
            spinner = new AdaptiveSpinner();
        }
        
        // 快速尝试获取锁（无等待）
        LockEntry tryFastLock(T id, Thread thread, FastThreadLocalState state) {
            int index = indexFor(id);
            AtomicReference<LockEntry> slot = table[index];
            
            // 读取-修改-更新循环
            while (true) {
                LockEntry entry = slot.get();
                
                // 槽位为空
                if (entry == null) {
                    LockEntry newEntry = entryPool.acquire(id);
                    if (slot.compareAndSet(null, newEntry)) {
                        if (newEntry.tryLock(thread)) {
                            return newEntry;
                        }
                        // CAS成功但获取锁失败（不应该发生）
                        slot.set(null);
                        entryPool.release(newEntry);
                        return null;
                    }
                    // CAS失败，重试
                    continue;
                }
                
                // 检查ID是否匹配
                if (entry.id == id) {
                    if (entry.tryLock(thread)) {
                        return entry;
                    }
                    return null;
                }
                
                // 哈希冲突，线性探测
                for (int i = 1; i < TABLE_SIZE; i++) {
                    int probeIndex = (index + i) & (TABLE_SIZE - 1);
                    slot = table[probeIndex];
                    entry = slot.get();
                    
                    if (entry == null) {
                        continue;
                    }
                    
                    if (entry.id == id) {
                        if (entry.tryLock(thread)) {
                            return entry;
                        }
                        return null;
                    }
                }
                
                // 未找到，创建新条目（使用下一个空槽）
                for (int i = 0; i < TABLE_SIZE; i++) {
                    int probeIndex = (index + i) & (TABLE_SIZE - 1);
                    slot = table[probeIndex];
                    entry = slot.get();
                    
                    if (entry == null) {
                        LockEntry newEntry = entryPool.acquire(id);
                        if (slot.compareAndSet(null, newEntry)) {
                            if (newEntry.tryLock(thread)) {
                                return newEntry;
                            }
                            slot.set(null);
                            entryPool.release(newEntry);
                        }
                        break;
                    }
                }
                
                return null;
            }
        }
        
        // 完整获取锁（可能阻塞）
        void lock(T id, Thread thread, FastThreadLocalState state) throws InterruptedException {
            LockEntry entry = getOrCreateEntry(id);
            
            // 自适应自旋
            int spinLimit = spinner.getSpinLimit();
            int spins = 0;
            
            while (spins < spinLimit) {
                if (entry.tryLock(thread)) {
                    return;
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                spins++;
                
                // 指数退避
                if (USE_BACKOFF && spins > INITIAL_SPIN_LIMIT) {
                    int backoff = Math.min(
                        BACKOFF_BASE << (spins - INITIAL_SPIN_LIMIT),
                        BACKOFF_MAX
                    );
                    Thread.sleep(0, backoff);
                } else {
                    Thread.onSpinWait();
                }
            }
            
            // 进入等待队列
            spinner.recordContention(); // 记录竞争
            
            try {
                entry.lock(thread);
            } catch (InterruptedException e) {
                spinner.recordInterrupt();
                throw e;
            }
        }
        
        // 带超时的尝试获取锁
        boolean tryLock(T id, Thread thread, FastThreadLocalState state, long nanosTimeout) 
                throws InterruptedException {
            
            LockEntry entry = getOrCreateEntry(id);
            long deadline = System.nanoTime() + nanosTimeout;
            
            // 自适应自旋
            int spinLimit = spinner.getSpinLimit();
            int spins = 0;
            
            while (spins < spinLimit && nanosTimeout > 0) {
                if (entry.tryLock(thread)) {
                    return true;
                }
                
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                spins++;
                long spinStart = System.nanoTime();
                
                // 短暂自旋或park
                if (nanosTimeout > 1000000L) { // 如果剩余时间>1ms
                    Thread.onSpinWait();
                } else {
                    LockSupport.parkNanos(Math.min(PARK_TIMEOUT_NS, nanosTimeout));
                }
                
                long spinEnd = System.nanoTime();
                nanosTimeout -= (spinEnd - spinStart);
            }
            
            // 带超时的完整获取
            spinner.recordContention();
            
            try {
                return entry.tryLock(thread, nanosTimeout);
            } catch (InterruptedException e) {
                spinner.recordInterrupt();
                throw e;
            }
        }
        
        // 释放锁
        void unlock(T id, Thread thread) {
            int index = indexFor(id);
            AtomicReference<LockEntry> slot = table[index];
            
            // 查找对应的条目
            LockEntry entry = slot.get();
            if (entry != null && entry.id == id) {
                entry.unlock(thread);
                tryCleanupEntry(entry);
                return;
            }
            
            // 线性探测查找
            for (int i = 1; i < TABLE_SIZE; i++) {
                int probeIndex = (index + i) & (TABLE_SIZE - 1);
                slot = table[probeIndex];
                entry = slot.get();
                
                if (entry != null && entry.id == id) {
                    entry.unlock(thread);
                    tryCleanupEntry(entry);
                    return;
                }
            }
        }
        
        // 获取或创建条目
        private LockEntry getOrCreateEntry(T id) {
            int index = indexFor(id);
            AtomicReference<LockEntry> slot = table[index];
            
            while (true) {
                LockEntry entry = slot.get();
                
                if (entry == null) {
                    LockEntry newEntry = entryPool.acquire(id);
                    if (slot.compareAndSet(null, newEntry)) {
                        return newEntry;
                    }
                    // CAS失败，释放并重试
                    entryPool.release(newEntry);
                    continue;
                }
                
                if (entry.id == id) {
                    return entry;
                }
                
                // 哈希冲突，查找其他槽位
                for (int i = 1; i < TABLE_SIZE; i++) {
                    int probeIndex = (index + i) & (TABLE_SIZE - 1);
                    slot = table[probeIndex];
                    entry = slot.get();
                    
                    if (entry != null && entry.id == id) {
                        return entry;
                    }
                    
                    if (entry == null) {
                        LockEntry newEntry = entryPool.acquire(id);
                        if (slot.compareAndSet(null, newEntry)) {
                            return newEntry;
                        }
                        entryPool.release(newEntry);
                    }
                }
                
                // 哈希表满，替换第一个条目（简化处理）
                slot = table[index];
                entry = slot.get();
                if (entry.isIdle()) {
                    LockEntry newEntry = entryPool.acquire(id);
                    if (slot.compareAndSet(entry, newEntry)) {
                        entryPool.release(entry);
                        return newEntry;
                    }
                    entryPool.release(newEntry);
                }
            }
        }
        
        // 尝试清理空闲条目
        private void tryCleanupEntry(LockEntry entry) {
            if (entry.isIdle() && entry.idleTime() > 1000000000L) { // 空闲超过1秒
                // 查找并清理
                for (int i = 0; i < TABLE_SIZE; i++) {
                    AtomicReference<LockEntry> slot = table[i];
                    LockEntry current = slot.get();
                    if (current == entry) {
                        if (slot.compareAndSet(entry, null)) {
                            entryPool.release(entry);
                        }
                        break;
                    }
                }
            }
        }
        
        // 哈希函数
        private static int indexFor(Object id) {
            int hash = System.identityHashCode(id);
            return (hash ^ (hash >>> 16)) & (TABLE_SIZE - 1);
        }
    }
    
    // ==================== 锁条目 ====================
    
    private static final class LockEntry {
        // 使用字段更新器避免对象头开销
        private static final AtomicIntegerFieldUpdater<LockEntry> STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(LockEntry.class, "state");
        private static final AtomicReferenceFieldUpdater<LockEntry, Thread> OWNER_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(LockEntry.class, Thread.class, "owner");
        private static final AtomicReferenceFieldUpdater<LockEntry, WaitNode> WAITERS_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(LockEntry.class, WaitNode.class, "waiters");
        
        // 状态常量
        private static final int FREE = 0;
        private static final int LOCKED = 1;
        private static final int CONTENDED = 2;
        
        // 字段声明（必须volatile）
        private Object id;
        private volatile int state = FREE;
        private volatile Thread owner;
        private volatile int holdCount = 0;
        private volatile WaitNode waiters;
        private volatile long lastUsed;
        
        LockEntry(Object id) {
            this.id = id;
            this.lastUsed = System.nanoTime();
        }
        
        // 快速尝试获取锁
        boolean tryLock(Thread thread) {
            // 重入检查
            if (owner == thread) {
                holdCount++;
                lastUsed = System.nanoTime();
                return true;
            }
            
            // CAS获取锁
            if (STATE_UPDATER.compareAndSet(this, FREE, LOCKED)) {
                OWNER_UPDATER.set(this, thread);
                holdCount = 1;
                lastUsed = System.nanoTime();
                return true;
            }
            
            return false;
        }
        
        // 完整获取锁（阻塞）
        void lock(Thread thread) throws InterruptedException {
            // 快速路径：重入
            if (owner == thread) {
                holdCount++;
                lastUsed = System.nanoTime();
                return;
            }
            
            // 自旋尝试
            for (int i = 0; i < INITIAL_SPIN_LIMIT; i++) {
                if (tryLock(thread)) {
                    return;
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                Thread.onSpinWait();
            }
            
            // 进入等待队列
            WaitNode node = new WaitNode(thread);
            WaitNode oldHead;
            
            do {
                oldHead = waiters;
                node.next = oldHead;
            } while (!WAITERS_UPDATER.compareAndSet(this, oldHead, node));
            
            try {
                // 再次尝试
                for (int i = 0; i < INITIAL_SPIN_LIMIT / 2; i++) {
                    if (tryLock(thread)) {
                        return;
                    }
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    LockSupport.parkNanos(PARK_TIMEOUT_NS);
                }
                
                // 阻塞等待
                while (!tryLock(thread)) {
                    LockSupport.park(this);
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                }
            } finally {
                // 从队列中移除
                removeFromQueue(node);
            }
        }
        
        // 带超时的尝试
        boolean tryLock(Thread thread, long nanos) throws InterruptedException {
            if (owner == thread) {
                holdCount++;
                lastUsed = System.nanoTime();
                return true;
            }
            
            long deadline = System.nanoTime() + nanos;
            
            // 快速尝试
            if (tryLock(thread)) {
                return true;
            }
            
            // 进入等待队列
            WaitNode node = new WaitNode(thread);
            WaitNode oldHead;
            
            do {
                oldHead = waiters;
                node.next = oldHead;
            } while (!WAITERS_UPDATER.compareAndSet(this, oldHead, node));
            
            try {
                while (nanos > 0) {
                    if (tryLock(thread)) {
                        return true;
                    }
                    
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    
                    long sleepTime = Math.min(nanos, 1000000L); // 最多1ms
                    LockSupport.parkNanos(sleepTime);
                    nanos = deadline - System.nanoTime();
                }
                
                return false;
            } finally {
                removeFromQueue(node);
            }
        }
        
        // 释放锁
        void unlock(Thread thread) {
            if (owner != thread) {
                throw new IllegalMonitorStateException();
            }
            
            if (--holdCount == 0) {
                OWNER_UPDATER.set(this, null);
                STATE_UPDATER.set(this, FREE);
                lastUsed = System.nanoTime();
                
                // 唤醒等待者
                WaitNode head = waiters;
                if (head != null) {
                    WaitNode next = head.next;
                    if (WAITERS_UPDATER.compareAndSet(this, head, next)) {
                        LockSupport.unpark(head.thread);
                    }
                }
            }
        }
        
        // 从队列中移除节点
        private void removeFromQueue(WaitNode node) {
            WaitNode head = waiters;
            if (head == node) {
                WaitNode next = node.next;
                WAITERS_UPDATER.compareAndSet(this, head, next);
            }
        }
        
        // 检查是否空闲
        boolean isIdle() {
            return state == FREE && waiters == null;
        }
        
        // 空闲时间（纳秒）
        long idleTime() {
            return System.nanoTime() - lastUsed;
        }
        
        // 等待节点
        private static final class WaitNode {
            final Thread thread;
            volatile WaitNode next;
            
            WaitNode(Thread thread) {
                this.thread = thread;
            }
        }
    }
    
    // ==================== ThreadLocal状态 ====================
    
    private static final class FastThreadLocalState {
        // 使用线性探测的小型哈希表
        private static final int CACHE_SIZE = 8;
        private final Object[] keys = new Object[CACHE_SIZE];
        private final int[] values = new int[CACHE_SIZE];
        
        // 记录锁获取
        void recordLock(Object key, int count) {
            // 线性探测
            int start = hash(key);
            for (int i = 0; i < CACHE_SIZE; i++) {
                int index = (start + i) & (CACHE_SIZE - 1);
                
                if (keys[index] == key) {
                    values[index] = count;
                    return;
                }
                
                if (keys[index] == null) {
                    keys[index] = key;
                    values[index] = count;
                    return;
                }
            }
            
            // 缓存满，替换第一个位置（LRU简化）
            keys[start] = key;
            values[start] = count;
        }
        
        // 记录锁释放，返回新的计数
        int recordUnlock(Object key) {
            int start = hash(key);
            for (int i = 0; i < CACHE_SIZE; i++) {
                int index = (start + i) & (CACHE_SIZE - 1);
                
                if (keys[index] == key) {
                    int newCount = --values[index];
                    if (newCount <= 0) {
                        keys[index] = null;
                        values[index] = 0;
                    }
                    return newCount;
                }
                
                if (keys[index] == null) {
                    break;
                }
            }
            return 0;
        }
        
        // 获取重入计数
        int getReentrantCount(Object key) {
            int start = hash(key);
            for (int i = 0; i < CACHE_SIZE; i++) {
                int index = (start + i) & (CACHE_SIZE - 1);
                
                if (keys[index] == key) {
                    return values[index];
                }
                
                if (keys[index] == null) {
                    return 0;
                }
            }
            return 0;
        }
        
        // 简单的哈希函数
        private static int hash(Object key) {
            return System.identityHashCode(key) & (CACHE_SIZE - 1);
        }
    }
    
    // ==================== 内存池 ====================
    
    private static final class EntryPool {
        private final LockEntry[] pool;
        private final AtomicInteger top = new AtomicInteger(0);
        
        EntryPool(int size) {
            pool = new LockEntry[size];
            for (int i = 0; i < size; i++) {
                pool[i] = new LockEntry(null);
            }
        }
        
        LockEntry acquire(Object id) {
            int index = top.getAndIncrement() & (pool.length - 1);
            LockEntry entry = pool[index];
            entry.id = id;  // 重用对象
            entry.state = LockEntry.FREE;
            entry.owner = null;
            entry.holdCount = 0;
            entry.waiters = null;
            entry.lastUsed = System.nanoTime();
            return entry;
        }
        
        void release(LockEntry entry) {
            // 简单的释放，实际可以更复杂
            entry.id = null;
        }
    }
    
    // ==================== 自适应自旋器 ====================
    
    private static final class AdaptiveSpinner {
        private volatile int spinLimit = INITIAL_SPIN_LIMIT;
        private volatile long lastContentionTime = 0;
        private final AtomicInteger contentionCount = new AtomicInteger(0);
        
        int getSpinLimit() {
            long now = System.nanoTime();
            long elapsed = now - lastContentionTime;
            
            // 如果没有竞争，增加自旋
            if (elapsed > 1000000000L) { // 1秒内无竞争
                spinLimit = Math.min(spinLimit * 2, MAX_SPIN_LIMIT);
                lastContentionTime = now;
            }
            
            return spinLimit;
        }
        
        void recordContention() {
            contentionCount.incrementAndGet();
            lastContentionTime = System.nanoTime();
            
            // 如果竞争激烈，减少自旋
            if (contentionCount.get() > 100) {
                spinLimit = Math.max(spinLimit / 2, INITIAL_SPIN_LIMIT / 4);
                contentionCount.set(0);
            }
        }
        
        void recordInterrupt() {
            // 中断时稍微降低自旋
            spinLimit = Math.max(spinLimit - 8, 16);
        }
    }
    
}
