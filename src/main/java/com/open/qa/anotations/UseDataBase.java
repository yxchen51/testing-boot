package com.open.qa.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于数据源的切换
 * @author : liang.chen
 * create in 2018/7/25 下午4:56
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseDataBase {

    /**
     * 使用的数据库名称
     * @return
     */
    String value();
}
