package com.easyway.business.framework.springmvc.controller;

import com.easyway.business.framework.json.JsonClothProcessor;
import com.easyway.business.framework.pojo.Grid;
import com.easyway.business.framework.springmvc.result.ResultBody;
import com.easyway.business.framework.springmvc.result.ResultUtil;

public class SuperController {
    /**
     * 渲染失败数据
     *
     * @param msg 需要返回的消息
     * @return json对象
     */
    protected ResultBody renderError(String msg) {
        return ResultUtil.error(msg);
    }

    /**
     * 渲染成功数据
     */
    protected ResultBody renderSuccess() {
        return ResultUtil.success();
    }

    /**
     * 渲染成功数据
     *
     * @param result 需要返回的对象
     * @return json对象
     */
    protected ResultBody renderSuccess(Object result) {
        return ResultUtil.success(result);
    }

    /**
     * 对象穿衣
     * 
     * @param grid
     * @param clothProcessor
     */
    public ResultBody list(Grid grid, JsonClothProcessor clothProcessor) {
        return ResultUtil.success(grid, clothProcessor);
    }

}
