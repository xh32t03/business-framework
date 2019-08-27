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
     * @return
     */
    public String code();

    /**
     * 获取枚举消息
     * 
     * @return
     */
    public String message();

    /**
     * 获取枚举值
     * 
     * @return
     */
    public Number value();
}
