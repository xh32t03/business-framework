package com.easyway.business.framework.json.util;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Collection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyway.business.framework.json.ToJson;
import com.easyway.business.framework.json.annotion.JsonData;
import com.easyway.business.framework.json.annotion.NotJsonData;

public final class JsonUtil {

    private static final DecimalFormat dfm = new DecimalFormat("##0.00");

    public static JSONObject toJSONObject(Object target) {
        if (target == null) {
            return null;
        }
        JSONObject jsObject = (JSONObject) JSON.toJSON(target);
        String methodName = null;
        try {
            Method[] methods = target.getClass().getMethods();
            for (Method method : methods) {
                methodName = method.getName();
                if ((methodName.startsWith("get") && (method.getParameterTypes().length == 0)
                        && (!methodName.equals("getClass"))) && (!methodName.equals("getHandler"))
                        && (!method.isAnnotationPresent(NotJsonData.class))) {
                    String field =
                            methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    Object value = method.invoke(target, new Object[0]);
                    boolean isJsonData = false;
                    if (method.isAnnotationPresent(JsonData.class)) {
                        isJsonData = true;
                        JsonData setJs = (JsonData) method.getAnnotation(JsonData.class);
                        if ((setJs.field() != null) && (!"".equals(setJs.field()))) {
                            field = setJs.field();
                        }
                    }
                    if ((value instanceof Collection) && isJsonData) {
                        Collection<?> set = (Collection<?>) value;
                        JSONArray jsonArray = new JSONArray();
                        for (Object obj : set) {
                            if ((obj instanceof ToJson)) {
                                JSONObject jsonObj = ((ToJson) obj).toJSONObject();
                                jsonArray.add(jsonObj);
                            } else {
                                jsonArray.add(obj);
                            }
                        }
                        jsObject.put(field, jsonArray);
                    } else if ((value instanceof Number)) {
                        if ((value instanceof Double)) {
                            jsObject.put(field, dfm.format(value));
                        }
                    } else if ((value instanceof ToJson)) {
                        jsObject.put(field, ((ToJson) value).toJSONObject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsObject;
    }

    public static JSONArray toJSONArray(Collection<?> target) {
        if (target == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (Object obj : target) {
            jsonArray.add(toJSONObject(obj));
        }
        return jsonArray;
    }

    public static JSONArray toJSONArray(Object[] target) {
        if (target == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        Object[] arrayOfObject = target;
        int j = target.length;
        for (int i = 0; i < j; i++) {
            Object obj = arrayOfObject[i];
            jsonArray.add(toJSONObject(obj));
        }
        return jsonArray;
    }

}
