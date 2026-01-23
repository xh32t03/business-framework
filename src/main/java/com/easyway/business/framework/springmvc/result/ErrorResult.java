package com.easyway.business.framework.springmvc.result;

import java.util.Map;

public class ErrorResult extends ResultBody {

    /**
     * 
     */
    private static final long   serialVersionUID = 1L;
    private Map<String, Object> details;

    // 静态工厂方法
    public static ErrorResult of(String code, String msg) {
        ErrorResult response = new ErrorResult();
        response.setStatus(Boolean.FALSE);
        response.code = code;
        response.msg = msg;
        return response;
    }
    
    public static ErrorResult of(String msg) {
        ErrorResult response = new ErrorResult();
        response.setStatus(Boolean.FALSE);
        response.msg = msg;
        return response;
    }

    // 添加详情信息
    public ErrorResult addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    // 链式调用方法
    public ErrorResult withCode(String code) {
        this.code = code;
        return this;
    }

    public ErrorResult withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
