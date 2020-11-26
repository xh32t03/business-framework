package com.easyway.business.framework.mybatis.query.condition;

import com.easyway.business.framework.mybatis.query.ConditionQuery;

public class BetweenValueCondition extends Condition {
	private String column;
	private Object beginValue;
	private Object endValue;

	public void addConditionToQuery(ConditionQuery imp) {
        imp.addBetweenValueCondition(this);
    }
	
	public BetweenValueCondition(String column, Object beginValue, Object endValue) {
		this.column = column;
		this.beginValue = beginValue;
		this.endValue = endValue;
	}

	public BetweenValueCondition(String tableAlias, String column, Object beginValue, Object endValue) {
        this(column, beginValue, endValue);
        this.tableAlias = tableAlias;
    }
	
	public String getColumn() {
		return this.column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getBeginValue() {
		return this.beginValue;
	}

	public void setBeginValue(Object beginValue) {
		this.beginValue = beginValue;
	}

	public Object getEndValue() {
		return this.endValue;
	}

	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}

	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getColumnWithTableAlias());
		sb.append(" between ");
		sb.append("'");
		sb.append(this.beginValue);
		sb.append("' and '");
		sb.append(this.endValue);
		sb.append("'");
		return sb.toString();
	}
}
