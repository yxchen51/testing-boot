package com.open.qa.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/13 下午5:59
 */
public class TestingLogDTO implements Serializable {

    /**
     * 本次构建的Id
     */
    private String buildId;

    /**
     * 测试项目的名称
     */
    private String projectName;

    /**
     * 测试允许开始时间
     */
    private Long projectStartTime;

    /**
     * 测试允许结束时间
     */
    private Long projectStopTime;

    /**
     * 测试的执行人
     */
    private String runner;

    /**
     * 测试执行的环境
     */
    private String runEnvironment;

    /**
     * 套件日志列表
     */
    private List<SuiteLogDTO> suiteLogDTOList;


    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(Long projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public Long getProjectStopTime() {
        return projectStopTime;
    }

    public void setProjectStopTime(Long projectStopTime) {
        this.projectStopTime = projectStopTime;
    }

    public String getRunner() {
        return runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public List<SuiteLogDTO> getSuiteLogDTOList() {
        return suiteLogDTOList;
    }

    public void setSuiteLogDTOList(List<SuiteLogDTO> suiteLogDTOList) {
        this.suiteLogDTOList = suiteLogDTOList;
    }

    public String getRunEnvironment() {
        return runEnvironment;
    }

    public void setRunEnvironment(String runEnvironment) {
        this.runEnvironment = runEnvironment;
    }
}
