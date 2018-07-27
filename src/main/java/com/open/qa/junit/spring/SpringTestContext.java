package com.open.qa.junit.spring;

import java.util.List;
import java.util.Observable;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/12 下午5:23
 */
public class SpringTestContext extends Observable {

    /**
     * 用于存储当前测试类的
     */
    private static ThreadLocal<List<Object>> testClassPool = new ThreadLocal<>();


    public static List<Object> getTestClassPool() {
        return testClassPool.get();
    }

    public static void setTestClassPool(List<Object> testList) {
        SpringTestContext.testClassPool.set(testList);
    }
}
