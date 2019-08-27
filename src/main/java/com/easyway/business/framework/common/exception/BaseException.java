package com.easyway.business.framework.common.exception;

import com.easyway.business.framework.common.enums.EnumBase;

/**
 * 运行时异常
 * 
 * @author liuxl
 */
public class BaseException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = -6108023129642050288L;

    protected String          code;

    protected String          message;

    // 异常枚举
    protected EnumBase        errorEnum;

    /**
     * 空构造器
     */
    public BaseException(){
        super();
    }

    /**
     * 构造器
     * 
     * @param message 消息
     */
    public BaseException(String message){
        super(message);
        this.message = message;
    }

    /**
     * 构造器
     * 
     * @param message 消息
     */
    public BaseException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造器
     * 
     * @param baseEnum 异常枚举
     */
    public BaseException(EnumBase baseEnum){
        super(baseEnum.message());
        this.code = baseEnum.code();
        this.message = baseEnum.message();
        this.errorEnum = baseEnum;
    }

    /**
     * 构造器
     * 
     * @param baseEnum 异常枚举
     * @param message 消息
     */
    public BaseException(EnumBase baseEnum, String message){
        super(message);
        this.code = baseEnum.code();
        this.message = message;
        this.errorEnum = baseEnum;
    }

    /**
     * 构造器
     * 
     * @param baseEnum 异常枚举
     * @param cause 原因
     */
    public BaseException(EnumBase baseEnum, Throwable cause){
        super(baseEnum.message(), cause);
        this.code = baseEnum.code();
        this.message = baseEnum.message();
        this.errorEnum = baseEnum;
    }

    /**
     * 构造器
     * 
     * @param cause 原因
     */
    public BaseException(Throwable cause){
        super(cause);
    }

    /**
     * 构造器
     * 
     * @param message 消息
     * @param cause 原因
     */
    public BaseException(String message, Throwable cause){
        super(message, cause);
        this.message = message;
    }

    /**
     * 构造器
     * 
     * @param message 消息
     * @param cause 原因
     */
    public BaseException(String code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public EnumBase getErrorEnum() {
        return errorEnum;
    }
}
