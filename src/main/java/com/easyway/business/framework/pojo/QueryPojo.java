package com.easyway.business.framework.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.mybatis.util.ConditionUtil;

public class QueryPojo {

    private Map<String, Object> queryParamMap = null;

    public ConditionQuery buildConditionQuery() {
        ConditionQuery query = newConditionQuery();
        query.addAll(toConditions());
        if (this.queryParamMap != null) {
            query.addAllParam(this.queryParamMap);
        }
        return query;
    }

    protected ConditionQuery newConditionQuery() {
        return new ConditionQuery();
    }

    protected List<Condition> toConditions() {
        return ConditionUtil.getConditions(this);
    }

    public void appendQueryParam(String key, Object value) {
        if (this.queryParamMap == null) {
            this.queryParamMap = new HashMap<String, Object>();
        }
        this.queryParamMap.put(key, value);
    }

}
