package com.open.qa.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级别的测试清理
 * 可以执行清理测现场的sql，shell脚本等
 * @author : liang.chen
 * create in 2018/7/13 下午3:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scavenger {

    String sql() default "";

    String shell() default "";


}
