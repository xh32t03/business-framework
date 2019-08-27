package com.easyway.business.framework.dao;

import java.io.Serializable;
import java.util.List;

import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.pojo.BasePojo;

/**
 * 持久层查询通用父接口,适用仅需查询功能需求
 * 
 * @author xl.liu
 */
public interface SchDao<T extends BasePojo> {

    /**
     * 根据主键ID获得数据库对应记录
     * 
     * @param id
     * @return
     */
    public T get(Serializable id);

    /**
     * 根据条件查询数据库记录列表
     * 
     * @param query
     * @return
     */
    public List<T> query(ConditionQuery query);

    /**
     * 根据条件查询数据库记录条数
     * 
     * @param query
     * @return
     */
    public int queryCnt(ConditionQuery query);
}
