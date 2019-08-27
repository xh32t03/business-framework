package com.easyway.business.framework.mybatis.annotion.factory;

import java.lang.annotation.Annotation;
import java.util.List;

import com.easyway.business.framework.mybatis.annotion.ListValue;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.mybatis.query.condition.ConditionFactory;

public class ListValueFactory extends AnnotationConditionFactory {
	@SuppressWarnings("unchecked")
	public Condition build(Annotation annotation, Object value) {
		if (value == null) {
			return null;
		}
		ListValue listValue = (ListValue) annotation;
		String tableAlias = listValue.tableAlias();
		String column = listValue.column();
		String equal = listValue.equal();

		if ((value instanceof List)) {
			return ConditionFactory.buildListCondition(tableAlias, column, equal, (List<Object>) value);
		}

		if ((value instanceof Object[])) {
			return ConditionFactory.buildListCondition(tableAlias, column, equal, (Object[]) value);
		}
		return null;
	}
}