package com.easyway.business.framework.pojo;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <pre>
 *  ToString基类
 *  如果作为入参对象继承该类时，请重写toString方法，用于摘要日志打印
 * </pre>
 * 
 * @author xl.liu
 */
public abstract class ToString implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -4525992547629575445L;

    /**
     * 重写toString方法
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this,
                new SerializerFeature[] {SerializerFeature.WriteNullListAsEmpty,
                        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.QuoteFieldNames,
                        SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect});
    }
}
