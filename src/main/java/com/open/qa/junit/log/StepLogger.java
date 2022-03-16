package com.open.qa.junit.log;


import com.open.qa.domain.RunResult;
import com.open.qa.domain.StepLogDTO;
import com.open.qa.domain.StepType;
import com.open.qa.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户记录日志的步骤信息
 * Update by liang.chen on 2022/02/01
 */
public class StepLogger {


    private static final Logger logger = LoggerFactory.getLogger(StepLogger.class);

    public static Boolean isFail = false;
    public static List<StepLogDTO> logStepInfoList = new ArrayList<>();

    /**
     * 记录日志
     * @param logStepInfo
     * @return
     */
    public static List<StepLogDTO> onLogStep(StepLogDTO logStepInfo) {
        logStepInfo.setStepId(logStepInfoList.size() + 1);
        logStepInfoList.add(logStepInfo);
        return logStepInfoList;
    }



    /**
     * 记录失败日志信息
     * @param stepType
     * @param stepDesc
     * @param failReason
     * @return
     */
    public static List<StepLogDTO> logStepFail(StepType stepType, String stepDesc, String failReason) {
        //失败标志位设置为true
        isFail = true;
        StepLogDTO stepInfo = new StepLogDTO();
        stepInfo.setStepType(stepType);
        stepInfo.setStepDesc(stepDesc);
        stepInfo.setRunResult(RunResult.FAILED);
        logger.error("{},执行失败!原因为:\n{}",stepDesc,failReason);
//        System.err.println(stepDesc + ",执行失败!原因为:\n" + failReason);
        /**
         * 防止数据量太大，失败原因长度超过100时，截取长度为100
         */
//        if (failReason.length() > 1000){
//            failReason = failReason.substring(0,100);
//        }
        stepInfo.setFailReason(failReason);
        stepInfo.setStepTime(System.currentTimeMillis());
        return onLogStep(stepInfo);
    }

    /**
     * 记录通过步骤日志
     * @param stepType
     * @param stepDesc
     * @return
     */
    public static List<StepLogDTO> logStepPass(StepType stepType,String stepDesc) {
       return logStep(stepType,stepDesc,RunResult.PASS);
    }

    /**
     * 自定义记录步骤信息
     * @param stepType
     * @param stepDesc
     * @param stepResult
     * @return
     */
    public static List<StepLogDTO> logStep(StepType stepType, String stepDesc, RunResult stepResult) {
        StepLogDTO stepInfo = new StepLogDTO();
        stepInfo.setStepType(stepType);
        stepInfo.setStepDesc(stepDesc);
        stepInfo.setRunResult(stepResult);
        String url = "";
        String stepTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
        stepInfo.setStepTime(System.currentTimeMillis());
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(stepDesc);
//        System.out.println(stepDesc);
        return onLogStep(stepInfo);
    }

}
