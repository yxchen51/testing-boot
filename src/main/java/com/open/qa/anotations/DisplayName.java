package com.open.qa.anotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于测试类或者测试方法上
 * 便于写中文名方便日志记录类似Junit5
 * Create by liang.chen on 2020/03/10
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {

    /**
     * 类或者方法对应的测试名称
     * @return
     */
    String value();
}
