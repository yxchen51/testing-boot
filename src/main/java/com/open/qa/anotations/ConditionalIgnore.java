package com.open.qa.anotations;


import com.open.qa.junit.rule.DriverRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据测试执行结果动态调过测试
 * 如：某一个测试方法报错之后停止整个测试框架
 * Create  by liang.chen on 2021/06/08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ConditionalIgnore {

    /**
     * 动态判断的类，需要实现IgnoreCondition接口
     * @return
     */
    Class<? extends DriverRule.IgnoreCondition> value();
}