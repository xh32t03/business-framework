package com.easyway.business.framework.mybatis.query.condition;

import com.easyway.business.framework.constant.Constant;
import com.easyway.business.framework.mybatis.query.ConditionQuery;

public class SingleValueCondition extends Condition {
	private String column;
	private String equal;
	private Object value;

	public void addConditionToQuery(ConditionQuery imp) {
        imp.addSingleValueCondition(this);
    }
	
	public SingleValueCondition(String condition, Object value) {
        this(condition, Constant.EQUAL, value);
    }

    public SingleValueCondition(String condition, String equal, Object value) {
        this.column = condition;
        this.equal = equal;
        this.value = value;
    }

    public SingleValueCondition(String tableAlias, String condition,
            String equal, Object value) {
        this(condition, equal, value);
        this.tableAlias = tableAlias;
    }
    
	public String getColumn() {
		return this.column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getEqual() {
		return this.equal;
	}

	public void setEqual(String equal) {
		this.equal = equal;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getColumnWithTableAlias());
		sb.append(" ");
		sb.append(this.equal);
		sb.append(" '");
		sb.append(this.value);
		sb.append("'");
		return sb.toString();
	}
}
