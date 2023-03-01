package com.easyway.business.framework.common.enums;

/**
 * 枚举基类
 * 
 * @author xl.liu
 */
public interface BaseEnum {

    /**
     * 获取枚举名
     * 
     * @return String
     */
    public String code();

    /**
     * 获取枚举消息
     * 
     * @return String
     */
    public String message();

    /**
     * 获取枚举值
     * 
     * @return Number
     */
    public Number value();
}
