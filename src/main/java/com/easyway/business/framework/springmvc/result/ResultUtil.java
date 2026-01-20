package com.easyway.business.framework.springmvc.result;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static <T> JSONObject wearCloth(T pojo, JsonClothProcessor processor) {
        if (pojo == null) {
            return new JSONObject();
        }

        return processor.wearCloth(pojo, JsonUtil.toJSONObject(pojo));
    }
    
    /**
     * 返回数据穿衣
     * 
     * List<JSONObject> result = ResultUtil.wearCloth(users, (user, json) -> {
     *     json.put("processed", true);
     *     json.put("timestamp", System.currentTimeMillis());
     *     return json;
     * });
     * 
     * @param list
     * @param processor
     * @return
     */
    public static List<JSONObject> wearCloth(List<?> list, JsonClothProcessor processor) {
        if (list == null || list.isEmpty()) {
            list = Collections.emptyList();
        }

        return list.parallelStream()
                .map(pojo -> processor.wearCloth(pojo, JsonUtil.toJSONObject(pojo)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    public static <T> List<JSONObject> transform(Collection<T> collection,
            Function<T, JSONObject> transformer) {

        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }

        return collection.stream().map(transformer).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 建造者模式，提供更灵活的配置
     * 
     * List<JSONObject> result = ResultUtil.builder(User.class)
     *     .collection(users)
     *     .processor((user, json) -> {
     *         json.put("processed", true);
     *         json.put("timestamp", System.currentTimeMillis());
     *         return json;
     *     }).build();
     * @param <T>
     */
    public static class Builder<T> {
        private List<T> collection;
        private JsonClothProcessor processor;
        
        public Builder<T> collection(List<T> collection) {
            this.collection = collection;
            return this;
        }
        
        public Builder<T> processor(JsonClothProcessor processor) {
            this.processor = processor;
            return this;
        }
        
        public List<JSONObject> build() {
            if (processor == null) {
                throw new IllegalArgumentException("Processor must not be null");
            }
            
            if (collection == null || collection.isEmpty()) {
                return Collections.emptyList();
            }
            
            return wearCloth(collection, processor);
        }
    }
    
    // 静态工厂方法
    public static <T> Builder<T> builder(Class<T> clazz) {
        return new Builder<>();
    }
}
