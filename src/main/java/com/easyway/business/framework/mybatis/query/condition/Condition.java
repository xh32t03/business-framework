package com.easyway.business.framework.mybatis.query.condition;

import com.easyway.business.framework.mybatis.query.ConditionQuery;

public abstract class Condition {
	protected String tableAlias = "t";

	public abstract void addConditionToQuery(ConditionQuery paramConditionQuery);

	public String getTableAlias() {
		return this.tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public abstract String getColumn();

	public String getColumnWithTableAlias() {
		if ((this.tableAlias == null) || ("".equals(this.tableAlias))) {
			return getColumn();
		}
		return this.tableAlias + "." + getColumn();
	}

	public abstract String toSql();
}
