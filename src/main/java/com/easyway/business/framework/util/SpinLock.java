package com.easyway.business.framework.util;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock {

    // java中原子（CAS）操作
    AtomicReference<Thread> owner = new AtomicReference<Thread>();// 持有自旋锁的线程对象

    public void lock() {
        Thread current = Thread.currentThread();
        // lock函数将owner设置为当前线程，并且预测原来的值为空。unlock函数将owner设置为null，并且预测值为当前线程。当有第二个线程调用lock操作时由于owner值不为空，导致循环
        // 一直被执行，直至第一个线程调用unlock函数将owner设置为null，第二个线程才能进入临界区。
        while (!owner.compareAndSet(null, current)) {
        }
    }

    public void unLock() {
        Thread current = Thread.currentThread();
        owner.compareAndSet(current, null);
    }
}
