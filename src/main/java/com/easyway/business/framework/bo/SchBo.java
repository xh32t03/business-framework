package com.easyway.business.framework.bo;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.easyway.business.framework.dao.SchDao;
import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.pojo.BasePojo;
import com.easyway.business.framework.pojo.Grid;
import com.easyway.business.framework.pojo.Page;
import com.easyway.business.framework.pojo.QueryPojo;

/**
 * 业务层查询通用父类,适用仅需查询功能需求
 * 
 * @author xl.liu
 */
public class SchBo<T extends BasePojo, Dao extends SchDao<T>> {

    protected Dao dao;

    /**
     * 注入相应持久层操作对象
     * 
     * @param dao
     */
    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Dao getDao() {
        return dao;
    }
    
    public Page list(Page page) {
        ConditionQuery query = page.buildConditionQuery();
        return queryAndSetPage(page, query);
    }

    public Page list(Page page, Condition appendCondition) {
        ConditionQuery query = page.buildConditionQuery();
        if (appendCondition != null) {
            query.add(appendCondition);
        }
        return queryAndSetPage(page, query);
    }
    
    public Page list(Page page, List<Condition> appendConditions) {
        ConditionQuery query = page.buildConditionQuery();
        if ((appendConditions != null) && (appendConditions.size() > 0)) {
            query.addAll(appendConditions);
        }
        return queryAndSetPage(page, query);
    }

    protected Page queryAndSetPage(Page page, ConditionQuery query) {
        int cnt = this.dao.queryCnt(query);
        page.setTotal(cnt);
        if (cnt > 0) {
            List<T> newsList = this.dao.query(query);
            page.setList(newsList);
        }
        return page;
    }

    public List<T> query(QueryPojo queryPojo) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        return this.dao.query(query);
    }

    public int queryCnt(QueryPojo queryPojo) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        return this.dao.queryCnt(query);
    }

    public List<T> query(Condition condition) {
        ConditionQuery query = new ConditionQuery();
        query.add(condition);
        return this.dao.query(query);
    }

    public int queryCnt(Condition condition) {
        ConditionQuery query = new ConditionQuery();
        query.add(condition);
        return this.dao.queryCnt(query);
    }

    public List<T> query(List<Condition> conditions) {
        ConditionQuery query = new ConditionQuery();
        if ((conditions != null) && (conditions.size() > 0)) {
            query.addAll(conditions);
        }
        return this.dao.query(query);
    }

    public int queryCnt(List<Condition> conditions) {
        ConditionQuery query = new ConditionQuery();
        if ((conditions != null) && (conditions.size() > 0)) {
            query.addAll(conditions);
        }
        return this.dao.queryCnt(query);
    }
    
    public List<T> query(QueryPojo queryPojo, Condition appendCondition) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        if (appendCondition != null) {
            query.add(appendCondition);
        }
        return this.dao.query(query);
    }
    
    public int queryCnt(QueryPojo queryPojo, Condition appendCondition) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        if (appendCondition != null) {
            query.add(appendCondition);
        }
        return this.dao.queryCnt(query);
    }
    
    public List<T> query(QueryPojo queryPojo, List<Condition> appendConditions) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        if ((appendConditions != null) && (appendConditions.size() > 0)) {
            query.addAll(appendConditions);
        }
        return this.dao.query(query);
    }

    public int queryCnt(QueryPojo queryPojo, List<Condition> appendConditions) {
        ConditionQuery query = queryPojo.buildConditionQuery();
        if ((appendConditions != null) && (appendConditions.size() > 0)) {
            query.addAll(appendConditions);
        }
        return this.dao.queryCnt(query);
    }

    public T get(Serializable id) {
        return this.dao.get(id);
    }
    
    public List<T> queryList(Grid grid) {
        if (grid != null) {
            grid.setPageSize(0);
            return dao.query(grid.buildConditionQuery());
        }
        return null;
    }
    
    public List<T> queryAll() {
        Grid grid = new Grid();
        grid.setPageSize(0);
        List<T> list = dao.query(grid.buildConditionQuery());
        return list;
    }
}
