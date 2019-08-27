package com.easyway.business.framework.mybatis.util;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.pojo.Grid;

public class QuerySupport {

    public static String getStatement(Class<?> mapperClass, String mapperId) {
        String name = mapperClass.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(".").append(mapperId);
        return sb.toString();
    }

    /**
     * 默认的query、queryCnt不能满足分页列表（多表关联），可定义frontList、frontListCnt
     * 
     * @param grid
     * @return
     */
    public static Grid queryFrontList(SqlSessionFactory sqlSessionFactory, Class<?> mapperClass,
            String mapperId, final Grid grid) {
        if (sqlSessionFactory == null) {
            throw new IllegalArgumentException(
                    "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
        }
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            ConditionQuery query = grid.buildConditionQuery();
            int cnt = session.selectOne(getStatement(mapperClass, mapperId.concat("Cnt")), query);
            grid.setTotal(cnt);
            if (cnt > 0) {
                List<Object> newsList =
                        session.selectList(getStatement(mapperClass, mapperId), query);
                grid.setList(newsList);
            }
            return new Grid(grid);// 防止脏数据入侵
        } catch (Exception ex) {
            throw new RuntimeException("queryFrontList Exception.", ex);
        } finally {
            session.close();
        }
    }

}
