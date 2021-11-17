package com.easyway.business.framework.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 数据库批量操作
 * 
 * @author xl.liu
 */
public class BatchSupport {

//    @Autowired(required=false)
//    private SqlSessionFactory sqlSessionFactory;
    
    public static void insertBatch(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass, String mapperId,
                                   List<?> objs) {
        new BatchTemplate() {

            @Override
            protected void singleOperation(SqlSession session, String statement, Object obj) {
                session.insert(statement, obj);
            }
        }.doBatch(sqlSessionFactory, mapperClass, mapperId, objs);
    }

    public static void updateBatch(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass, String mapperId,
                                   List<?> objs) {
        new BatchTemplate() {

            @Override
            protected void singleOperation(SqlSession session, String statement, Object obj) {
                session.update(statement, obj);
            }
        }.doBatch(sqlSessionFactory, mapperClass, mapperId, objs);
    }

}
