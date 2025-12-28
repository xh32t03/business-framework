package com.easyway.business.framework.pojo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.mybatis.util.ConditionUtil;
import com.easyway.business.framework.util.ReflectUtil;

public class QueryPojo {

    private Set<Condition>      appendCondition = new ConcurrentSkipListSet<>();
    private Map<String, Object> paramMap        = new ConcurrentHashMap<>();

    @SuppressWarnings("all")
    public ConditionQuery buildConditionQuery() {
        ConditionQuery query = newConditionQuery();
        query.addAll(toConditions());
        try {
            Map<String, Field> fieldMap = ReflectUtil.getClassFields(this.getClass(), true);
            Set<Map.Entry<String, Field>> entryseSet = fieldMap.entrySet();
            for (Map.Entry<String, Field> entry : entryseSet) {
                Field field = entry.getValue();
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Object value = field.get(this);
                    if (value != null) {
                        query.addParam(entry.getKey(), value);
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        if (this.appendCondition != null 
                && !this.appendCondition.isEmpty()) {
            query.addAll(new ArrayList<>(this.appendCondition));
        }
        if (this.paramMap != null 
                && !this.paramMap.isEmpty()) {
            query.addAllParam(this.paramMap);
        }
        return query;
    }
    
    protected ConditionQuery newConditionQuery() {
        return new ConditionQuery();
    }

    protected List<Condition> toConditions() {
        return ConditionUtil.getConditions(this);
    }

    public void appendCondition(Condition condition) {
        this.appendCondition.add(condition);
    }

    public void appendParam(String key, Object value) {
        if (value != null) {
            this.paramMap.put(key, value);
        }
    }
}
