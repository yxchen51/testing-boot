package com.open.qa.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 测试日志收集类
 * @author : liang.chen
 * create in 2018/7/26 下午3:53
 */
public class SuiteLogDTO implements Serializable {


    private String suiteName;
    private RunResult runResult;
    private Long suiteStartTime;
    private Long suiteStopTime;
    private Integer passCaseNum;
    private Integer failCaseNum;
    private Integer passModuleNum;
    private Integer failModuleNum;
    private List<ModuleLogDTO> moduleInfoList;


    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public Long getSuiteStartTime() {
        return suiteStartTime;
    }

    public void setSuiteStartTime(Long suiteStartTime) {
        this.suiteStartTime = suiteStartTime;
    }

    public Long getSuiteStopTime() {
        return suiteStopTime;
    }

    public void setSuiteStopTime(Long suiteStopTime) {
        this.suiteStopTime = suiteStopTime;
    }

    public Integer getPassCaseNum() {
        return passCaseNum;
    }

    public void setPassCaseNum(Integer passCaseNum) {
        this.passCaseNum = passCaseNum;
    }

    public Integer getFailCaseNum() {
        return failCaseNum;
    }

    public void setFailCaseNum(Integer failCaseNum) {
        this.failCaseNum = failCaseNum;
    }

    public Integer getPassModuleNum() {
        return passModuleNum;
    }

    public void setPassModuleNum(Integer passModuleNum) {
        this.passModuleNum = passModuleNum;
    }

    public Integer getFailModuleNum() {
        return failModuleNum;
    }

    public void setFailModuleNum(Integer failModuleNum) {
        this.failModuleNum = failModuleNum;
    }

    public List<ModuleLogDTO> getModuleInfoList() {
        return moduleInfoList;
    }

    public void setModuleInfoList(List<ModuleLogDTO> moduleInfoList) {
        this.moduleInfoList = moduleInfoList;
    }
}
