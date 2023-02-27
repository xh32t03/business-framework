package com.easyway.business.framework.pojo;

import com.alibaba.fastjson.annotation.JSONType;
import com.easyway.business.framework.json.annotion.NotJsonData;
import com.easyway.business.framework.mybatis.query.ConditionQuery;

@JSONType(ignores = {"sortname", "sortorder"})
public class Grid extends Page {
    private String sortname;
    private String sortorder;

    public Grid() {
        
    }

    public Grid(Page page) {
        super(page);
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    @NotJsonData
    public String getSortname() {
        return this.sortname;
    }

    @NotJsonData
    public String getSortorder() {
        return this.sortorder;
    }

    protected String buildSortname() {
        String ret = this.sortname;
        if (ret == null) {
            return null;
        }
        ret = ret.replaceAll("'", "");
        ret = ret.replaceAll("\\*", "");
        ret = ret.replaceAll("--", "");
        ret = ret.replaceAll(" ", "");
        ret = ret.replaceAll("\\(", "");
        ret = ret.replaceAll("\\)", "");
        if (ret.length() > 20) {
            ret = ret.substring(0, 20);
        }
        return ret;
    }

    protected String buildSortorder() {
        if (this.sortorder == null) {
            return null;
        }
        this.sortorder = this.sortorder.toLowerCase();
        if (("asc".equals(this.sortorder)) || ("desc".equals(this.sortorder))) {
            return this.sortorder;
        }
        return null;
    }

    public String buildOrderCol() {
        String name = buildSortname();
        if ((name == null) || ("".equals(name))) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        ret.append(name);
        String order = buildSortorder();
        if ((order != null) && (!"".equals(order))) {
            ret.append(" ");
            ret.append(order);
        }
        return ret.toString();
    }

    public ConditionQuery buildConditionQuery() {
        ConditionQuery query = super.buildConditionQuery();
        String orderCol = buildOrderCol();
        if (orderCol != null) {
            query.addParam("orderCol", orderCol);
        }
        return query;
    }
}
