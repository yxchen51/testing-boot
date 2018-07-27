package com.open.qa.junit.spring;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/12 下午5:30
 */
public class TestSuiteObserver implements Observer{



    @Override
    public void update(Observable o, Object arg) {
        List<Object> objectList = SpringTestContext.getTestClassPool();
        objectList.remove(o);
        SpringTestContext.setTestClassPool(objectList);
    }
}


