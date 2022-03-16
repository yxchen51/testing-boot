package com.open.qa.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 测试方法级别的权重,weight值越小的最先执行
 * e.g
 * </p>@RunWeight(weight=1)
 * </p>testMethod1();
 * </p>@RunWeight
 * </p>testMethod2();
 * </p>testMethod3();
 * 则方法的执行顺序为:
 * testMethod1->testMethod3->testMethod2
 * Update  by liang.chen on 2021/06/08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RunWeight {

    /**
     * 默认运行权重
     * @return
     */
    int value() default Integer.MAX_VALUE;

}
