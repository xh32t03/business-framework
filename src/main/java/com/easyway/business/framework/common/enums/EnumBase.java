package com.easyway.business.framework.common.enums;

/**
 * 枚举基类
 * 
 * @author xl.liu
 */
public interface EnumBase {

    /**
     * 获取枚举名
     * 
     * @return int
     */
    public int code();

    /**
     * 获取枚举消息
     * 
     * @return String
     */
    public String message();

}
