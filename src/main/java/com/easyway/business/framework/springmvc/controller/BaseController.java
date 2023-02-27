package com.easyway.business.framework.springmvc.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.easyway.business.framework.json.JsonClothProcessor;
import com.easyway.business.framework.pojo.Grid;
import com.easyway.business.framework.springmvc.StringToDateConverter;
import com.easyway.business.framework.springmvc.result.ResultBody;
import com.easyway.business.framework.springmvc.result.ResultUtil;

public class BaseController {
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
     * 渲染数据穿衣
     * 
     * @param grid
     * @param clothProcessor
     */
    public ResultBody list(Grid grid, JsonClothProcessor clothProcessor) {
        return ResultUtil.success(grid, clothProcessor);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new StringToDateConverter().convert(text));
            }
        });
    }
}
