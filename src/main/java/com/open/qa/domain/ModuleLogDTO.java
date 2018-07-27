package com.open.qa.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/13 下午6:07
 */
public class ModuleLogDTO implements Serializable {

    private String moduleName;
    private Long moduleStartTime;
    private Long moduleStopTime;
    private RunResult runResult;
    private List<CaseLogDTO> caseInfoList;
    private Integer passCaseNum;
    private Integer failCaseNum;


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getModuleStartTime() {
        return moduleStartTime;
    }

    public void setModuleStartTime(Long moduleStartTime) {
        this.moduleStartTime = moduleStartTime;
    }

    public Long getModuleStopTime() {
        return moduleStopTime;
    }

    public void setModuleStopTime(Long moduleStopTime) {
        this.moduleStopTime = moduleStopTime;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public List<CaseLogDTO> getCaseInfoList() {
        return caseInfoList;
    }

    public void setCaseInfoList(List<CaseLogDTO> caseInfoList) {
        this.caseInfoList = caseInfoList;
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
}
