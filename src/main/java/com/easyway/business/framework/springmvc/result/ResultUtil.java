package com.easyway.business.framework.springmvc.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.easyway.business.framework.common.enums.EnumBase;
import com.easyway.business.framework.json.JsonClothProcessor;
import com.easyway.business.framework.json.util.JsonUtil;
import com.easyway.business.framework.pojo.Page;
import com.easyway.business.framework.util.StringUtil;

public final class ResultUtil {

    private ResultUtil() {

    }

    public static ResultBody success(Page page, JsonClothProcessor clothProcessor) {
        List<JSONObject> jsonObjList = wearCloth(page.getList(), clothProcessor);
        page.setList(jsonObjList);
        return success(page);
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

    public static ResultBody error(String code, String msg) {
        if (StringUtil.isBlank(code))
            code = "-1";
        return new ResultBody(code, msg);
    }

    public static ResultBody error(EnumBase errorInfo) {
        ResultBody resultBody = new ResultBody();
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
