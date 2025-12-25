package com.easyway.business.framework.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * // 1. 直接调用（最简单）
 * BatchSupport.insertBatch(sqlSessionFactory, UserMapper.class, "insert", userList);
 * 
 * // 2. 自定义操作
 * BatchSupport.executeBatch(sqlSessionFactory, 
 *     UserMapper.class, "customUpdate", 
 *     userList, 
 *     (session, user) -> {
 *         user.setUpdateTime(new Date());
 *         // 注意：这里需要使用完整的statement，因为operation参数需要知道statement
 *         session.update(UserMapper.class.getName() + ".customUpdate", user);
 *     },
 *     "customUpdate");
 * 
 * // 3. 使用构建器
 * BatchSupport.BatchBuilder.with(sqlSessionFactory)
 *     .mapper(UserMapper.class, "insert")
 *     .batchSize(500)
 *     .insert(userList);
 * 
 * // 4. 使用processInBatch（最灵活，可以完全控制）
 * BatchSupport.processInBatch(sqlSessionFactory, 1000, (session, user) -> {
 *     // 可以在这里构建任何statement
 *     if (user.isNew()) {
 *         session.insert("com.example.UserMapper.insert", user);
 *     } else {
 *         session.update("com.example.UserMapper.update", user);
 *     }
 * }, userList);
 */
public class BatchSupport {

    private static final Logger logger             = LoggerFactory.getLogger(BatchSupport.class);

    // 默认批次大小
    private static final int    DEFAULT_BATCH_SIZE = 1000;

    /**
     * 批量插入数据
     */
    public static <T> void insertBatch(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass,
            String mapperId, List<T> dataList) {
        executeBatch(sqlSessionFactory, dataList,
                (session, data) -> session.insert(buildStatementName(mapperClass, mapperId), data));
    }

    /**
     * 批量更新数据
     */
    public static <T> void updateBatch(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass,
            String mapperId, List<T> dataList) {
        executeBatch(sqlSessionFactory, dataList,
                (session, data) -> session.update(buildStatementName(mapperClass, mapperId), data));
    }

    /**
     * 批量删除数据
     */
    public static <T> void deleteBatch(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass,
            String mapperId, List<T> dataList) {
        executeBatch(sqlSessionFactory, dataList,
                (session, data) -> session.delete(buildStatementName(mapperClass, mapperId), data));
    }

    /**
     * 构造完整的statement名称
     */
    private static <T> String buildStatementName(Class<T> mapperClass, String mapperId) {
        if (mapperClass == null || mapperId == null || mapperId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Mapper class and mapperId must not be null or empty");
        }
        return mapperClass.getName() + "." + mapperId;
    }

    /**
     * 通用的批量操作方法
     */
    public static <T> void executeBatch(SqlSessionFactory sqlSessionFactory, List<T> dataList,
            BiConsumer<SqlSession, T> operation) {
        executeBatch(sqlSessionFactory, dataList, operation, DEFAULT_BATCH_SIZE);
    }

    /**
     * 带批次大小的批量操作方法
     */
    public static <T> void executeBatch(SqlSessionFactory sqlSessionFactory, List<T> dataList,
            BiConsumer<SqlSession, T> operation, int batchSize) {
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("Batch processing skipped: data list is empty");
            return;
        }

        if (batchSize <= 0) {
            batchSize = DEFAULT_BATCH_SIZE;
        }

        SqlSession session = null;

        try {
            session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            int totalCount = dataList.size();

            logger.debug("Starting batch processing for {} records, batch size: {}", 
                    totalCount, batchSize);

            for (int i = 0; i < totalCount; i++) {
                T data = dataList.get(i);
                // 使用封装了statement的操作
                operation.accept(session, data);

                // 分批刷新到数据库
                if (i > 0 && i % batchSize == 0) {
                    session.flushStatements();
                    logger.debug("Flushed statements: {}/{} records processed", i + 1, totalCount);
                }
            }

            // 循环结束后一次性提交事务和清理缓存
            session.commit();
            session.clearCache();

            logger.info("Batch processing completed: {} records processed", totalCount);
        } catch (Exception e) {
            logger.error("Batch processing failed", e);
            if (session != null) {
                session.rollback();
            }
            throw new BatchOperationException("Batch processing failed", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 带自定义处理器的高级批量处理方法
     */
    public static <T> void processInBatch(SqlSessionFactory sqlSessionFactory, int batchSize,
            BatchProcessor<T> batchProcessor, List<T> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("Batch processing skipped: data list is empty");
            return;
        }

        if (batchSize <= 0) {
            batchSize = DEFAULT_BATCH_SIZE;
        }

        SqlSession session = null;

        try {
            session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            int totalCount = dataList.size();

            logger.debug("Starting batch processing for {} records, batch size: {}", totalCount,
                    batchSize);

            for (int i = 0; i < totalCount; i++) {
                T data = dataList.get(i);
                batchProcessor.process(session, data);

                // 分批刷新到数据库
                if (i > 0 && i % batchSize == 0) {
                    session.flushStatements();
                    logger.debug("Flushed statements: {}/{} records processed", i + 1, totalCount);
                }
            }

            // 循环结束后一次性提交事务和清理缓存
            session.commit();
            session.clearCache();

            logger.info("Batch processing completed: {} records processed", totalCount);
        } catch (Exception e) {
            logger.error("Batch processing failed", e);
            if (session != null) {
                session.rollback();
            }
            throw new BatchOperationException("Batch processing failed", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 批量处理器接口
     */
    @FunctionalInterface
    public interface BatchProcessor<T> {
        void process(SqlSession session, T data);
    }

    /**
     * 批量操作异常
     */
    public static class BatchOperationException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public BatchOperationException(String message) {
            super(message);
        }

        public BatchOperationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 批量操作构建器（链式调用）
     */
    public static class BatchBuilder {
        private final SqlSessionFactory sqlSessionFactory;
        private Class<?>                mapperClass;
        private String                  mapperId;
        private int                     batchSize = DEFAULT_BATCH_SIZE;

        private BatchBuilder(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
        }

        public static BatchBuilder with(SqlSessionFactory sqlSessionFactory) {
            return new BatchBuilder(sqlSessionFactory);
        }

        public BatchBuilder mapper(Class<?> mapperClass, String mapperId) {
            this.mapperClass = mapperClass;
            this.mapperId = mapperId;
            return this;
        }

        public BatchBuilder batchSize(int batchSize) {
            this.batchSize = batchSize > 0 ? batchSize : DEFAULT_BATCH_SIZE;
            return this;
        }

        public <T> void insert(List<T> dataList) {
            validateMapper();
            // 直接调用doBatchOperation，让它在内部构建statement
            executeBatch(sqlSessionFactory, dataList, (session, data) -> session
                    .insert(buildStatementName(mapperClass, mapperId), data), batchSize);
        }

        public <T> void update(List<T> dataList) {
            validateMapper();
            executeBatch(sqlSessionFactory, dataList, (session, data) -> session
                    .update(buildStatementName(mapperClass, mapperId), data), batchSize);
        }

        public <T> void delete(List<T> dataList) {
            validateMapper();
            executeBatch(sqlSessionFactory, dataList, (session, data) -> session
                    .delete(buildStatementName(mapperClass, mapperId), data), batchSize);
        }

        /**
         * 自定义批量操作
         */
        public <T> void execute(List<T> dataList, BiConsumer<SqlSession, T> operation,
                String operationType) {
            validateMapper();
            executeBatch(sqlSessionFactory, dataList, operation, batchSize);
        }

        /**
         * 使用自定义处理器执行批量操作
         */
        public <T> void process(List<T> dataList, BatchProcessor<T> processor) {
            processInBatch(sqlSessionFactory, batchSize, processor, dataList);
        }

        private void validateMapper() {
            if (mapperClass == null || mapperId == null) {
                throw new IllegalStateException(
                        "Mapper class and mapperId must be set using mapper() method");
            }
        }
    }
}
