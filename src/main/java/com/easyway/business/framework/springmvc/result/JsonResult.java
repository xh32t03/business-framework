package com.easyway.business.framework.springmvc.result;

/**
 * 返回结果
 * 
 * @author xl.liu
 */
public final class JsonResult extends ResultBody {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 响应结果
	 */
	private Object data;

	public JsonResult() {
	}

	public JsonResult(int code, String msg) {
	    this.status = Boolean.TRUE;
		this.code = code;
		this.msg = msg;
	}

	public JsonResult(int code, String msg, Object data) {
	    this.status = Boolean.TRUE;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static JsonResult success(Object result) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setStatus(Boolean.TRUE);
		jsonResult.setCode(SUCCESS_CODE);
		jsonResult.setData(result);
		return jsonResult;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
