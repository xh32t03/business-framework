package com.easyway.business.framework.springmvc.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.easyway.business.framework.common.enums.EnumBase;
import com.easyway.business.framework.json.JsonClothProcessor;
import com.easyway.business.framework.json.util.JsonUtil;
import com.easyway.business.framework.pojo.Page;
import com.easyway.business.framework.util.Assert;

public final class ResultUtil {

    private ResultUtil() {

    }

    public static ResultBody success(Page page, JsonClothProcessor clothProcessor) {
        Assert.assertNotNull(page);
        Assert.assertNotNull(clothProcessor);
        List<JSONObject> jsonObjList = wearCloth(page.getList(), clothProcessor);
        page.setList(jsonObjList);
        return success(page);
    }

    /**
     * 渲染成功数据
     *
     * @return ResultBody
     */
    private static ResultBody renderSuccess() {
        return ResultBody.success();
    }

    private static ResultBody renderSuccess(Object result) {
        return JsonResult.success(result);
    }
    
    private static ResultBody renderSuccess(int code, String msg) {
        return new ResultBody(Boolean.TRUE, code, msg);
    }
    
    private static ResultBody renderSuccess(EnumBase errorInfo) {
        Assert.assertNotNull(errorInfo);
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(Boolean.TRUE);
        resultBody.setCode(errorInfo.code());
        resultBody.setMsg(errorInfo.message());
        return resultBody;
    }

    /**
     * 渲染失败数据
     *
     * @param msg
     * @return ResultBody
     */
    private static ResultBody renderError(String msg) {
        return ResultBody.error(msg);
    }

    private static ResultBody renderError(int code, String msg) {
        return new ResultBody(Boolean.FALSE, code, msg);
    }
    
    private static ResultBody renderError(EnumBase errorInfo) {
        Assert.assertNotNull(errorInfo);
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(Boolean.FALSE);
        resultBody.setCode(errorInfo.code());
        resultBody.setMsg(errorInfo.message());
        return resultBody;
    }
    
    /**
     * 返回成功
     * 
     * @return
     */
    public static ResultBody ok() {
        return success();
    }

    public static ResultBody ok(Object result) {
        return success(result);
    }
    
    public static ResultBody ok(int code, String msg) {
        return success(code, msg);
    }
    
    public static ResultBody ok(EnumBase errorInfo) {
        return success(errorInfo);
    }
    
    public static ResultBody success() {
        return renderSuccess();
    }

    public static ResultBody success(Object result) {
        return renderSuccess(result);
    }

    public static ResultBody success(int code, String msg) {
        return renderSuccess(code, msg);
    }
    
    public static ResultBody success(EnumBase errorInfo) {
        return renderSuccess(errorInfo);
    }
    
    /**
     * 返回错误
     * 
     * @param msg
     * @return
     */
    public static ResultBody error(String msg) {
        return renderError(msg);
    }

    public static ResultBody error(int code, String msg) {
        return renderError(code, msg);
    }

    public static ResultBody error(EnumBase errorInfo) {
        return renderError(errorInfo);
    }

    public static List<JSONObject> wearCloth(List<?> list, JsonClothProcessor clothProcessor) {
        if (list == null) {
            list = Collections.emptyList();
        }
        List<JSONObject> jsonObjList = new ArrayList<JSONObject>(list.size());
        for (Object pojo : list) {
            jsonObjList.add(clothProcessor.wearCloth(pojo, JsonUtil.toJSONObject(pojo)));
        }
        return jsonObjList;
    }
}
