package com.easyway.business.framework.mybatis.query.condition;

import java.util.List;

public class ConditionFactory {
    public static Condition buildSqlCondition(String sql) {
        return new WithoutValueCondition(sql);
    }

    public static Condition buildSingleValueCondition(String tableAlias, String column, String equal, Object value) {
        return new SingleValueCondition(tableAlias, column, equal, value);
    }

    public static Condition buildSingleValueCondition(String column, String equal, Object value) {
        return new SingleValueCondition(column, equal, value);
    }
    
    public static Condition buildBetweenCondition(String tableAlias, String column, Object beginValue,
            Object endValue) {
        return new BetweenValueCondition(tableAlias, column, beginValue, endValue);
    }

    public static Condition buildBetweenCondition(String column, Object beginValue, Object endValue) {
        return new BetweenValueCondition(column, beginValue, endValue);
    }
    
    public static Condition buildListCondition(String tableAlias, String column, String equal, List<Object> list) {
        return new ListValueCondition(tableAlias, column, equal, list);
    }

    public static Condition buildListCondition(String tableAlias, String column, String equal, Object[] list) {
        return new ListValueCondition(tableAlias, column, equal, list);
    }

    public static Condition buildListCondition(String column, String equal, List<Object> list) {
        return new ListValueCondition(column, equal, list);
    }

    public static Condition buildListCondition(String column, String equal, Object[] list) {
        return new ListValueCondition(column, equal, list);
    }
    
    public static Condition buildListCondition(String column, List<Object> list) {
        return new ListValueCondition(column, list);
    }

    public static Condition buildListCondition(String column, Object[] list) {
        return new ListValueCondition(column, list);
    }
}
