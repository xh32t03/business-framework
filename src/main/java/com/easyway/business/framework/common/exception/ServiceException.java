package com.easyway.business.framework.common.exception;

public class ServiceException extends BaseException {

    /** serialVersionUID */
    private static final long serialVersionUID = -6377567047634446727L;

    /**
     * 构造新实例
     */
    public ServiceException() {
        super();
    }

    /**
     * @param errorMessage 异常信息
     */
    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.message = errorMessage;
    }

    /**
     * 用表示异常原因的对象构造新实例
     * 
     * @param cause 异常原因
     */
    public ServiceException(Throwable cause) {
        super((String) null, cause);
    }

    /**
     * @param errorMessage 异常信息
     * @param cause 异常原因
     */
    public ServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.message = errorMessage;
    }

    /**
     * @param errorCode 错误代码
     * @param errorMessage 异常信息
     * @param cause 异常原因
     */
    public ServiceException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code = errorCode;
        this.message = errorMessage;
    }

}
