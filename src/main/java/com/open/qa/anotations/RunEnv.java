package com.open.qa.anotations;


import com.open.qa.domain.TestingEnvironment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义运行环境的注解，通过注解的参数加载不同环境的配置
 * Create  by liang.chen on 2021/06/08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RunEnv {

    /**
     * 运行环境参数，默认在Test上运行
     * 查看{@link TestingEnvironment}
     * @return
     */
    TestingEnvironment value() default TestingEnvironment.TEST ;

}
