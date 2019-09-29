package com.easyway.business.framework.mybatis.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.easyway.business.framework.mybatis.annotion.factory.AnnotationConditionFactory;
import com.easyway.business.framework.mybatis.query.condition.Condition;

public final class ConditionUtil {
    private final static Logger logger = LoggerFactory.getLogger(ConditionUtil.class);
    
	public static List<Condition> getConditions(Object target) {
		if (target == null) {
			return null;
		}
		List<Condition> retList = new ArrayList<Condition>();
		String methodName = null;
		try {
			Method[] methods = target.getClass().getMethods();
			for (Method method : methods) {
				methodName = method.getName();
				Annotation[] annotations = method.getAnnotations();
				if ((methodName.startsWith("get")) && (AnnotationConditionFactory.couldBuild(annotations))) {
					Object value = method.invoke(target, new Object[0]);
					for (Annotation annotation : annotations) {
						Condition condition = AnnotationConditionFactory.buildCondition(annotation, value);
						if (condition != null) {
						    retList.add(condition);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(methodName);
            logger.error(e.getMessage(), e);
		}
		return retList;
	}
}
