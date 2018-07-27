package com.open.qa.domain;

import java.io.Serializable;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/13 下午6:08
 */
public class StepLogDTO implements Serializable {
    private Integer stepId;
    private String stepName;
    private RunResult runResult;
    private StepType stepType;
    private Long stepTime;
    private String stepDesc;
    private String failReason;

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    public Long getStepTime() {
        return stepTime;
    }

    public void setStepTime(Long stepTime) {
        this.stepTime = stepTime;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
