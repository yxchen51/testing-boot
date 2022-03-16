package com.open.qa.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 测试日志收集类
 * @author : liang.chen
 * create in 2018/7/26 下午3:53
 */
public class CaseLogDTO implements Serializable {

    private String caseName;
    private String caseDesc;
    private String caseType;
    private String caseProperties;
    private Boolean run;
    private Long caseStartTime;
    private Long caseStopTime;
    private Long caseTakeTime;
    private RunResult runResult;
    private Map<String, String> caseParam;
    private List<StepLogDTO> stepInfoList;

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseProperties() {
        return caseProperties;
    }

    public void setCaseProperties(String caseProperties) {
        this.caseProperties = caseProperties;
    }

    public Boolean getRun() {
        return run;
    }

    public void setRun(Boolean run) {
        this.run = run;
    }

    public Long getCaseStartTime() {
        return caseStartTime;
    }

    public void setCaseStartTime(Long caseStartTime) {
        this.caseStartTime = caseStartTime;
    }

    public Long getCaseStopTime() {
        return caseStopTime;
    }

    public void setCaseStopTime(Long caseStopTime) {
        this.caseStopTime = caseStopTime;
    }

    public Long getCaseTakeTime() {
        return caseTakeTime;
    }

    public void setCaseTakeTime(Long caseTakeTime) {
        this.caseTakeTime = caseTakeTime;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public Map<String, String> getCaseParam() {
        return caseParam;
    }

    public void setCaseParam(Map<String, String> caseParam) {
        this.caseParam = caseParam;
    }

    public List<StepLogDTO> getStepInfoList() {
        return stepInfoList;
    }

    public void setStepInfoList(List<StepLogDTO> stepInfoList) {
        this.stepInfoList = stepInfoList;
    }
}
