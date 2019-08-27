package com.easyway.business.framework.mybatis;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 数据库批量操作模板类
 * 
 * @author xl.liu
 */
public abstract class BatchTemplate {

	public void doBatch(SqlSessionFactory sqlSessionFactory,
			Class<?> mapperClass, String mapperId, List<?> objs) {
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,
				false);
		try {
			for (Object obj : objs) {
				singleOperation(session,
						mapperClass.getName() + "." + mapperId, obj);
			}
			session.commit();
			session.clearCache();
		} catch (Exception ex) {
			session.rollback();
			throw new RuntimeException("doBatch Exception.", ex);
		} finally {
			session.close();
		}
	}

	protected abstract void singleOperation(SqlSession session,
			String statement, Object obj);
}
