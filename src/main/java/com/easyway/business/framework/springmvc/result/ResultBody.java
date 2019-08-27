package com.easyway.business.framework.springmvc.result;

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
	 * 响应代码(0成功，非0代表失败)
	 */
	protected String code = "0";

	/**
	 * 响应消息(正常情况返回ok，失败的时候返回错误的描述信息)
	 */
	protected String msg = "ok";

	public ResultBody() {
	}

	public ResultBody(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResultBody success() {
		ResultBody resultMessage = new ResultBody();
		resultMessage.setCode("0");
		resultMessage.setMsg("ok");
		return resultMessage;
	}
	
	public static ResultBody error(String msg) {
		ResultBody resultBody = new ResultBody();
		resultBody.setCode("-1");
		resultBody.setMsg(msg);
		return resultBody;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
