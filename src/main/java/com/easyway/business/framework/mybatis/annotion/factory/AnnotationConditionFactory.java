package com.easyway.business.framework.mybatis.annotion.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;

import com.easyway.business.framework.mybatis.query.condition.Condition;

public abstract class AnnotationConditionFactory {
    public static HashMap<String, AnnotationConditionFactory> factorys =
            new HashMap<String, AnnotationConditionFactory>();

    static {
        factorys.put("ConditionValue", new ConditionValueFactory());
        factorys.put("SingleValue", new SingleValueFactory());
        factorys.put("ListValue", new ListValueFactory());
    }

    public static boolean couldBuild(Annotation[] annotations) {
        if ((annotations == null) || (annotations.length == 0)) {
            return false;
        }
        Annotation[] arrayOfAnnotation = annotations;
        int j = annotations.length;
        for (int i = 0; i < j; i++) {
            Annotation annotation = arrayOfAnnotation[i];
            if (factorys.get(annotation.annotationType().getSimpleName()) != null) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static Condition buildCondition(Annotation annotation, Object value) {
        AnnotationConditionFactory factory =
                (AnnotationConditionFactory) factorys.get(annotation.annotationType().getSimpleName());
        if ((factory == null) || (value == null) || ((value instanceof String) && ((String) value).equals(""))
                || (value.getClass().isArray() && (Array.getLength(value) == 0))
                || (value instanceof Collection && ((Collection) value).isEmpty())) {
            return null;
        }
        return factory.build(annotation, value);
    }

    public abstract Condition build(Annotation paramAnnotation, Object paramObject);
}
