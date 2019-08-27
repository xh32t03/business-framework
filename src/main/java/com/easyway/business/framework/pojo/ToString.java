package com.easyway.business.framework.pojo;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * <pre> ToString基类
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
     * @param String 字符串
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        StringBuilder sb = new StringBuilder("[");
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append(field.getName()).append(":").append(field.get(this)).append(",");
            } catch (Exception e) {
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
