package com.open.qa.domain;

/**
 * 测试数据驱动的注解,用户选择不同的测试数据驱动方式
 * e.g
 * 选择Excel时将从测试资源目录下读取Excel作为数据驱动文档
 * 选择Database时将执行sql文件
 * 选择Platform时将从测试平台拉取测试数据作为测试数据
 * @author : chenliang@tsfinance.com
 * create in 2018/7/13 下午3:30
 */
public enum TestingDataType {

    EXCEL,
    DB,
    PLATFORM;

}
