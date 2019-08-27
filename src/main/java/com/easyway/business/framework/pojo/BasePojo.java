package com.easyway.business.framework.pojo;

import com.alibaba.fastjson.JSONObject;
import com.easyway.business.framework.json.ToJson;
import com.easyway.business.framework.json.util.JsonUtil;

public class BasePojo extends ToString implements ToJson {
	
	/**
     * 
     */
    private static final long serialVersionUID = 650834348738142653L;

    @Override
	public JSONObject toJSONObject() {
		return JsonUtil.toJSONObject(this);
	}

}
