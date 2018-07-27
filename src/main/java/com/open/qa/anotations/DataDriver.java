package com.open.qa.anotations;


import com.open.qa.domain.TestingDataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提供数据驱动文件的注解
 * 支持{@link TestingDataType}里面的数据驱动方式
 * Excel和Db方式请严格按照规范来编写数据驱动
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataDriver {
    /**
     * 数据驱动的文件路径和名称，默认在src/test/resources/data下
     * @return
     */
    String filePath()  default "";

    /**
     * 采用Excel驱动的时候需要读取的表格名称
     * @return
     */
    String sheetName() default "";

    /**
     * 采用DB方式驱动时需要可执行的sql,并返回List<Object>
     * @return
     */
    String sql() default "";

    /**
     * 需要加载的数据驱动文件类型，根据不同类型加载不同的数据驱动文件
     * 查看{@link TestingDataType}
     * @return
     */
    TestingDataType dataType() default TestingDataType.EXCEL;

}
