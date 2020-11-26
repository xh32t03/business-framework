package com.easyway.business.framework.mybatis.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.easyway.business.framework.mybatis.query.condition.Condition;

public class ConditionQuery {

    private List<Condition>     criteriaWithBetweenValue = new ArrayList<Condition>();
    private List<Condition>     criteriaWithoutValue     = new ArrayList<Condition>();
    private List<Condition>     criteriaWithSingleValue  = new ArrayList<Condition>();
    private List<Condition>     criteriaWithListValue    = new ArrayList<Condition>();
    private Map<String, Object> paramMap                 = new LinkedHashMap<String, Object>();

    public List<Condition> getCriteriaWithBetweenValue() {
        return this.criteriaWithBetweenValue;
    }

    public void setCriteriaWithBetweenValue(List<Condition> criteriaWithBetweenValue) {
        this.criteriaWithBetweenValue = criteriaWithBetweenValue;
    }

    public List<Condition> getCriteriaWithoutValue() {
        return this.criteriaWithoutValue;
    }

    public void setCriteriaWithoutValue(List<Condition> criteriaWithoutValue) {
        this.criteriaWithoutValue = criteriaWithoutValue;
    }

    public List<Condition> getCriteriaWithSingleValue() {
        return this.criteriaWithSingleValue;
    }

    public void setCriteriaWithSingleValue(List<Condition> criteriaWithSingleValue) {
        this.criteriaWithSingleValue = criteriaWithSingleValue;
    }

    public List<Condition> getCriteriaWithListValue() {
        return this.criteriaWithListValue;
    }

    public void setCriteriaWithListValue(List<Condition> criteriaWithListValue) {
        this.criteriaWithListValue = criteriaWithListValue;
    }

    public void addBetweenValueCondition(Condition condition) {
        this.criteriaWithBetweenValue.add(condition);
    }

    public void addWithoutValueCondition(Condition condition) {
        this.criteriaWithoutValue.add(condition);
    }

    public void addSingleValueCondition(Condition condition) {
        this.criteriaWithSingleValue.add(condition);
    }

    public void addListValueCondition(Condition condition) {
        this.criteriaWithListValue.add(condition);
    }

    public void add(Condition condition) {
        if (condition != null) {
            condition.addConditionToQuery(this);
        }
    }

    public void addAll(List<Condition> list) {
        if (list != null) {
            for (Condition condition : list) {
                add(condition);
            }
        }
    }
    
    public List<Condition> getAllCondition() {
        List<Condition> ret = new ArrayList<Condition>();
        ret.addAll(this.criteriaWithBetweenValue);
        ret.addAll(this.criteriaWithoutValue);
        ret.addAll(this.criteriaWithSingleValue);
        ret.addAll(this.criteriaWithListValue);
        return ret;
    }

    public Map<String, Object> getParamMap() {
        return this.paramMap;
    }

    public void addParam(String key, Object value) {
        this.paramMap.put(key, value);
    }

    public void addAllParam(Map<String, Object> map) {
        this.paramMap.putAll(map);
    }

    public ConditionQuery cloneConditionQuery(ConditionQuery query) {
        ConditionQuery m_query = new ConditionQuery();
        m_query.addAll(query.getAllCondition());
        m_query.addAllParam(query.getParamMap());
        return m_query;
    }
}
