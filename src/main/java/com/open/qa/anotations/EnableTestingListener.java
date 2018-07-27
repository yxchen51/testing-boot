package com.open.qa.anotations;

import com.open.qa.common.DynamicDataSourceAspect;
import com.open.qa.spring.SpringTestingListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启整个测试框架的监听
 * @author : chenliang@tsfinance.com
 * create in 2018/7/12 下午4:57
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({SpringTestingListener.class, DynamicDataSourceAspect.class})
public @interface EnableTestingListener {

    String value() default "";

}
