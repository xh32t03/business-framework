package com.easyway.business.framework.springmvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring上下文
 * 
 * @author xl.liu
 */
@Component
@Lazy(false)
@SuppressWarnings("all")
public class SpringContext implements ApplicationContextAware, DisposableBean {

    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        checkApplicationContext();
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(name, clazz);
    }

    public static boolean containsBean(String name) {
        checkApplicationContext();
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        checkApplicationContext();
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        checkApplicationContext();
        return applicationContext.getType(name);
    }

    private static void checkApplicationContext() {
        Assert.notNull(applicationContext,
                "applicaitonContext未注入,请在applicationContext.xml中定义SpringContext");
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

}
