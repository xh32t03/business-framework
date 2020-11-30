package com.easyway.business.framework.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.mybatis.util.ConditionUtil;

public class QueryPojo {

    private Set<Condition>      appendCondition = null;
    private Map<String, Object> queryParamMap   = null;

    public ConditionQuery buildConditionQuery() {
        ConditionQuery query = newConditionQuery();
        query.addAll(toConditions());
        if (this.appendCondition != null) {
            query.addAll(new ArrayList<>(appendCondition));
        }
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

    public void appendCondition(Condition condition) {
        if (this.appendCondition == null) {
            this.appendCondition = new HashSet<Condition>();
        }
        this.appendCondition.add(condition);
    }

}
