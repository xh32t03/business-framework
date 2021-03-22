package com.easyway.business.framework.pojo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.easyway.business.framework.mybatis.query.ConditionQuery;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.mybatis.util.ConditionUtil;
import com.easyway.business.framework.util.CollectionUtil;

@SuppressWarnings("all")
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

        try {
            List<String> filterList = CollectionUtil.arrayToList(new String[] {"sortname", "sortorder", "pageSize", "pageNum",
                    "dataList", "pages", "total", "appendCondition", "queryParamMap"});
            Map<String, Field> fieldMap = getClassFields(this.getClass(), true);
            for (String key : fieldMap.keySet()) {
                Field field = fieldMap.get(key);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    if (!filterList.contains(key)) {
                        Object value = field.get(this);
                        if (value != null) {
                            appendQueryParam(key, value);
                        }
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
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

    public static Map<String, Field> getClassFields(Class clazz, boolean includeParentClass) {
        Map<String, Field> map = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        if (includeParentClass) {
            getParentClassFields(map, clazz.getSuperclass());
        }
        return map;
    }

    private static Map<String, Field> getParentClassFields(Map<String, Field> map, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        if (clazz.getSuperclass() == null) {
            return map;
        }
        getParentClassFields(map, clazz.getSuperclass());
        return map;
    }

}
