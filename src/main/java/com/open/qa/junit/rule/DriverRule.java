package com.open.qa.junit.rule;

import com.open.qa.common.InitializeManager;
import com.open.qa.junit.core.TestingStatement;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;

/** 重写
 * Created by liang.chen on 2017/3/21.
 */
 public class DriverRule implements TestRule {

    private Map<String,String> caseParam = new HashMap<>();

    @Override
   public Statement apply(Statement base, Description desc){
        return new TestingStatement(base,desc,this);
    }

    public Map<String, String> getCaseParam() {
        return caseParam;
    }

    public void setCaseParam(Map<String, String> caseParam) {
        this.caseParam = caseParam;
    }

    public Map<String, String> getEnvProperMap() {
        return InitializeManager.envProper;
    }

}
