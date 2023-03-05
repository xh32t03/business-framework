package com.easyway.business.framework.springmvc.result;

import com.easyway.business.framework.common.enums.BaseResultCodeEnum;
import com.easyway.business.framework.pojo.ToString;

/**
 * 返回体
 * 
 * @author xl.liu
 */
public class ResultBody extends ToString {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否成功
	 */
	protected Boolean success;
    
    /**
	 * 状态码(0成功，-1代表失败)
	 */
	protected int code = 0;

	/**
	 * 提示信息(正常情况返回ok，失败的时候返回错误的描述信息)
	 */
	protected String msg = "ok";

	public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
	public ResultBody() {
	}

	public ResultBody(Boolean success, int code, String msg) {
	    this.success = success;
		this.code = code;
		this.msg = msg;
	}

	public static ResultBody success() {
		ResultBody resultBody = new ResultBody();
		resultBody.setSuccess(Boolean.TRUE);
		resultBody.setCode(BaseResultCodeEnum.SUCCESS.code());
		resultBody.setMsg("ok");
		return resultBody;
	}
	
	public static ResultBody error(String msg) {
		ResultBody resultBody = new ResultBody();
		resultBody.setSuccess(Boolean.FALSE);
		resultBody.setCode(BaseResultCodeEnum.FAILURE.code());
		resultBody.setMsg(msg);
		return resultBody;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
