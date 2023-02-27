package com.easyway.business.framework.springmvc.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.easyway.business.framework.bo.SchBo;
import com.easyway.business.framework.dao.SchDao;
import com.easyway.business.framework.json.JsonClothProcessor;
import com.easyway.business.framework.mybatis.query.condition.Condition;
import com.easyway.business.framework.pojo.BasePojo;
import com.easyway.business.framework.pojo.Grid;
import com.easyway.business.framework.springmvc.result.ResultBody;
import com.easyway.business.framework.springmvc.result.ResultUtil;

public abstract class SchController<Pojo extends BasePojo, Bo extends SchBo<Pojo, ? extends SchDao<Pojo>>>
        extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Bo           bo;

    @Autowired
    private void setBo(Bo bo) {
        this.bo = bo;
    }

    public Grid listGrid(Grid grid, List<Condition> appendConditions) {
        return new Grid(this.bo.list(grid, appendConditions));
    }

    public Grid listGrid(Grid grid, Condition appendCondition) {
        return new Grid(this.bo.list(grid, appendCondition));
    }

    public Grid listGrid(Grid grid) {
        return new Grid(this.bo.list(grid));
    }

    /**
     * 返回对象
     */
    public ResultBody list(Grid grid, List<Condition> appendConditions) {
        return ResultUtil.success(listGrid(grid, appendConditions));
    }

    public ResultBody list(Grid grid, Condition appendCondition) {
        return ResultUtil.success(listGrid(grid, appendCondition));
    }

    public ResultBody list(Grid grid) {
        return ResultUtil.success(listGrid(grid));
    }

    /**
     * 对象穿衣
     */
    public ResultBody list(Grid grid, List<Condition> appendConditions,
            JsonClothProcessor clothProcessor) {
        return ResultUtil.success(listGrid(grid, appendConditions), clothProcessor);
    }

    public ResultBody list(Grid grid, Condition appendCondition,
            JsonClothProcessor clothProcessor) {
        return ResultUtil.success(listGrid(grid, appendCondition), clothProcessor);
    }

    public ResultBody list(Grid grid, JsonClothProcessor clothProcessor) {
        return ResultUtil.success(listGrid(grid), clothProcessor);
    }
}
