package com.open.qa.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Spring的ApplicationContext的持有者,可以用静态方法的方式获取spring容器中的bean
 * @author : liang.chen
 * create in 2021/4/15 下午2:38
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }


    public static void  setBean(Object bean){
        assertApplicationContext();
    }

    /**
     * 获取当前环境 是dev还是prod
     * @return
     */
    public static String[] getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new RuntimeException("SpringContextHolder属性为null,请检查是否注入了SpringContextHolder!");
        }
    }

}
