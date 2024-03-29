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
    public static ResultBody renderSuccess() {
        return ResultUtil.success();
    }

    /**
     * 渲染成功数据
     *
     * @param result 需要返回的对象
     * @return ResultBody
     */
    public static ResultBody renderSuccess(Object result) {
        return ResultUtil.success(result);
    }
    
    /**
     * 渲染失败数据
     *
     * @param msg 需要返回的消息
     * @return ResultBody
     */
    public static ResultBody renderError(String msg) {
        return ResultUtil.error(msg);
    }

    public static ResultBody renderError(int code, String msg) {
        return ResultUtil.error(code, msg);
    }
    
    public static ResultBody renderError(EnumBase errorInfo) {
        return ResultUtil.error(errorInfo);
    }
    
    /**
     * 返回消息体
     * 
     * @param code
     * @param msg
     * @return ResultBody
     */
    public static ResultBody renderBody(int code, String msg) {
        return new ResultBody(Boolean.TRUE, code, msg);
    }
    
    public static ResultBody renderBody(EnumBase errorInfo) {
        Assert.assertNotNull(errorInfo);
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(Boolean.TRUE);
        resultBody.setCode(errorInfo.code());
        resultBody.setMsg(errorInfo.message());
        return resultBody;
    }

    public static ResultBody success() {
        return ResultBody.success();
    }

    public static ResultBody success(Object result) {
        return JsonResult.success(result);
    }

    public static ResultBody error(String msg) {
        return ResultBody.error(msg);
    }

    public static ResultBody error(int code, String msg) {
        return new ResultBody(Boolean.FALSE, code, msg);
    }

    public static ResultBody error(EnumBase errorInfo) {
        Assert.assertNotNull(errorInfo);
        ResultBody resultBody = new ResultBody();
        resultBody.setStatus(Boolean.FALSE);
        resultBody.setCode(errorInfo.code());
        resultBody.setMsg(errorInfo.message());
        return resultBody;
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
