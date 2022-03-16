package com.open.qa.common;

import com.open.qa.junit.core.SpringTestingRunner;
import com.open.qa.junit.rule.Assertor;
import com.open.qa.junit.rule.DriverRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Map;

/**
 * 基础测试类，所有测试类继承此类
 * Update  by liang.chen on 2021/06/08
 */
@RunWith(SpringTestingRunner.class)
public class BaseTest {

    @Rule
    public DriverRule driverRule = new DriverRule();

    @Rule
    public  Assertor assertor = new Assertor();


    public Map<String, String> getCaseParam() {
        return driverRule.getCaseParam();
    }


    public Map<String, String> getEnvParam() {
        return InitializeManager.envProper;
    }


}
