package com.easyway.business.framework.springmvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Spring上下文
 * 
 * 在Spring配置文件中注册该工具类
 * <bean id="springContext" class="com.easyway.business.framework.springmvc.SpringContext" />
 * 
 * @author xl.liu
 */
@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /*
     * 实现了ApplicationContextAware接口，必须实现该方法；通过传递applicationContext参数初始化成员变量applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean,自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean,自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        checkApplicationContext();
        return (T) applicationContext.getBean(name, clazz);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContext");
        }
    }

    /**
     * 实现DisposableBean接口,重写destroy方法,相当于destroy-method,bean被销毁的时候被调用,实现在Context关闭时清理静态变量的目的.
     */
    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

}
