package com.easyway.business.framework.springmvc;

import java.time.Instant;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import com.easyway.business.framework.constant.Constant;

/**
 * 系统全局类型转换器
 * 1、注册到配置文件SpringMVC.xml中
 * <!-- 配置 全局日期类型转换器 -->
 * <bean id="dateConverter" 
 *     class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
 *     <property name="converters" >
 *         <list>
 *             <bean class="com.easyway.business.framework.springmvc.StringToDateConverter" />
 *         </list>
 *     </property>
 * </bean>
 * 
 * 2、并且还要把全局日期转换器注册到驱动里面，不然不会生效
 * <!-- 配置全局日期类型转换器 -->
 * <mvc:annotation-driven conversion-service="dateConverter" />
 * 
 * @author xl.liu
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {
        try {
            if (StringUtils.hasText(value)) {
                value = value.trim();
                if (value.contains("-")) {
                    if (value.contains(":")) {
                        return Constant.NORM_DATETIME_FORMAT.get().parse(value);
                    } else {
                        return Constant.NORM_DATE_FORMAT.get().parse(value);
                    }
                } else if (value.matches("^\\d+$")) {
                    return Date.from(Instant.ofEpochMilli(Long.valueOf(value)));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("parser %s to Date failed", value));
        }
        return null;
    }

}
