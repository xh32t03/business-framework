package com.easyway.business.framework.springmvc.handler;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import com.easyway.business.framework.constant.Constant;

/**
 * 系统全局类型转换器
 * 1、注册到配置文件SpringMVC.xml中
 * <!-- 配置 全局日期类型转换器 -->
   <bean id="dateConverter" 
       class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
       <property name="converters" >
           <list>
               <bean class="com.easyway.business.framework.springmvc.handler.DateConverter" />
           </list>
       </property>
 * </bean>
 * 
 * 2、并且还要把全局日期转换器注册到驱动里面，不然不会生效
 * <!-- 配置全局日期类型转换器 -->        
   <mvc:annotation-driven conversion-service="dateConverter" />
 *  
 * @author xl.liu
 */
public class DateConverter implements Converter<String, Date> {

    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
    
    @Override
    public Date convert(String source) {
        try {
            if (StringUtils.hasText(source)) {
                if (source.contains(":")
                        || source.indexOf(":") != -1) {
                    return Constant.NORM_DATETIME_FORMAT.parse(source);
                }
                return Constant.NORM_DATE_FORMAT.parse(source);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
