package com.easyway.business.framework.mybatis.annotion.factory;

import java.lang.annotation.Annotation;

import com.easyway.business.framework.mybatis.query.condition.Condition;

public class ConditionValueFactory extends AnnotationConditionFactory {
    public Condition build(Annotation annotation, Object value) {
        if (value == null) {
            return null;
        }
        return (Condition) value;
    }
}
