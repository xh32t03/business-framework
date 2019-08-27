package com.easyway.business.framework.mybatis.query.condition;

import com.easyway.business.framework.mybatis.query.ConditionQuery;

public class WithoutValueCondition extends Condition {
	private String sql;

	public void addConditionToQuery(ConditionQuery imp) {
        imp.addWithoutValueCondition(this);
    }
	
	public WithoutValueCondition(String sql) {
		this.sql = sql;
	}

	public String getColumn() {
		return null;
	}

	public String toSql() {
		return this.sql;
	}
	
	public String toString() {
        return this.sql;
    }
}