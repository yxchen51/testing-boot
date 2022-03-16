package com.open.qa.junit.core;

import com.open.qa.junit.rule.DriverRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 跳过测试的Statement
 * Update by liang.chen on 2022/02/01
 */
public class IgnoreStatement extends Statement {

    private final Statement base;

    private final Description desc;

    private final DriverRule driverRule;

    public IgnoreStatement(Statement statement, Description description, DriverRule rule) {
        this.base = statement;
        this.desc = description;
        this.driverRule = rule;
    }

    @Override
    public void evaluate() {
        System.out.println(desc.getMethodName()+"方法被条件判断为跳过...");
    }


}
