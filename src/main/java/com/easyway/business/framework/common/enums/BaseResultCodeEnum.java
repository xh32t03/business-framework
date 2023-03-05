package com.easyway.business.framework.common.enums;

public enum BaseResultCodeEnum implements EnumBase {

    /** 操作成功 */
    SUCCESS(200, "操作成功"),

    /** 操作失败 */
    FAILURE(400, "操作失败"),

    /** 未登录 */
    NO_AUTH(401, "未登录"),

    /** 请求不合法 */
    REQUEST_NOT_MATCH(403, "请求不合法"),

    /** 未找到该资源 */
    NOT_FOUND(404, "未找到该资源"),

    /** 服务器异常 */
    INTERNAL_SERVER_ERROR(500, "服务器异常"),

    /** 服务器正忙 */
    SERVER_BUSY(503, "服务器正忙，请稍后再试！"),

    /** SESSION超时 */
    SESSION_TIMEOUT(1000, "登录超时"),

    /** 无操作权限 */
    NO_PERMISSION(1001, "无操作权限"),

    /** 操作不正确 */
    ILLEGAL_OPERATION(1002, "操作不正确"),

    /** 参数为空 */
    NULL_ARGUMENT(1003, "参数为空"),

    /** 参数不正确 */
    ILLEGAL_ARGUMENT(1004, "参数不正确"),

    /** 接口调用异常 */
    INTERFACE_SYSTEM_ERROR(1005, "接口调用异常"),

    /** 逻辑错误 */
    LOGIC_ERROR(1006, "逻辑错误"),

    /** 数据异常 */
    DATA_ERROR(1007, "数据异常"),

    /** 业务连接处理超时 */
    CONNECT_TIME_OUT(1008, "系统超时，请稍后再试！"),

    /** 系统异常 */
    SYSTEM_ERROR(9999, "系统异常，请联系管理员！"),

    ;

    /** 枚举编号 */
    private int    code;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     * 
     * @param code 枚举编号
     * @param message 枚举详情
     */
    private BaseResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     * 
     * @param code 枚举编号
     * @return BaseResultCodeEnum
     */
    public static BaseResultCodeEnum getExceptionEnum(int code) {
        for (BaseResultCodeEnum param : values()) {
            if (param.getCode() == code) {
                return param;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
