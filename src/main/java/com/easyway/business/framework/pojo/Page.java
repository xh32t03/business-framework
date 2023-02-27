package com.easyway.business.framework.pojo;

import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import com.easyway.business.framework.json.annotion.JsonData;
import com.easyway.business.framework.json.annotion.NotJsonData;
import com.easyway.business.framework.mybatis.query.ConditionQuery;

public class Page extends QueryPojo {

    // 每页条数
    private int     pageSize = 10;
    // 当前第几页
    private int     pageNum  = 1;
    // 查询集合
    private List<?> list;
    // 总页数
    private int     pages;
    // 总记录数
    private int     total;

    public Page() {
        
    }

    public Page(Page page) {
        this.pageSize = page.pageSize;
        this.pageNum = page.pageNum;
        this.total = page.total;
        this.list = page.list;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPages() {
        // 不分页
        if (this.pageSize <= 0) {
            return 1;
        }
        // 总页数 =（总记录数+每页显示数-1）/ 每页显示数
        this.pages = (this.total + this.pageSize - 1) / this.pageSize;
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 查询偏移量
     * 
     * @return int
     */
    @NotJsonData
    @JSONField(serialize = false)
    public int getPageOffset() {
        // 如果当前页小于第一页，则停留在第一页
        this.pageNum = this.pageNum < 1 ? 1 : this.pageNum;
        return (this.pageNum - 1) * this.pageSize;
    }

    @JsonData
    public List<?> getList() {
        return this.list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public ConditionQuery buildConditionQuery() {
        ConditionQuery query = super.buildConditionQuery();
        query.addParam("pageOffset", Integer.valueOf(getPageOffset()));
        query.addParam("pageSize", Integer.valueOf(getPageSize()));
        return query;
    }
}
