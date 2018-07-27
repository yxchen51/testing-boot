package com.open.qa.common;

import com.open.qa.junit.core.SpringTestingRunner;
import com.open.qa.junit.rule.Assertor;
import com.open.qa.junit.rule.DriverRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Map;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/16 下午2:38
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
