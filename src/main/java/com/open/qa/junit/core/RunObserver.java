package com.open.qa.junit.core;


import com.open.qa.common.InitializeManager;
import com.open.qa.domain.CaseLogDTO;
import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.RunResult;
import com.open.qa.domain.StepLogDTO;
import com.open.qa.domain.SuiteLogDTO;
import com.open.qa.domain.TestingLogDTO;
import com.open.qa.junit.log.StepLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 测试方法和用例的组织运行过程
 */
public class RunObserver {

    private static final Logger logger = LoggerFactory.getLogger(RunObserver.class);

    private static SuiteLogDTO suiteInfo ;
    private static ModuleLogDTO moduleInfo ;
    private static CaseLogDTO caseInfo ;
    private static TestingLogDTO testingLogDTO;


    public static void setTestingLogDTO(TestingLogDTO testingInfo){
        testingLogDTO = testingInfo;
    }

    public static TestingLogDTO getTestingLogDTO() {
        return testingLogDTO;
    }

    public static ModuleLogDTO getModuleInfo(){
        return moduleInfo;
    }

    /**
     * 计算通过率的函数
     * @param pass
     * @param fail
     * @return
     */
    private static Double calculatePassPercent(Integer pass,Integer fail){
        try {
            Double passPercent = ((double) pass / (pass + fail)) * 100.00;
            BigDecimal b = new BigDecimal(passPercent);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }catch (Exception e){
            e.printStackTrace();
            return 0.00;
        }
    }

    /**
     * case开始运行
     * @param paramCaseInfo
     */
    public static void caseRunStart(CaseLogDTO paramCaseInfo) {
        logger.info("测试用例:{},开始执行...",paramCaseInfo.getCaseName());
        //清空LogModule的步骤数据
        paramCaseInfo.setRunResult(RunResult.RUNNING);
        paramCaseInfo.setRun(true);
        paramCaseInfo.setCaseStartTime(System.currentTimeMillis());
        caseInfo = paramCaseInfo;
    }

    /**
     * case结束运行
     * @param
     */
    public static void caseRunStop() {
        List<StepLogDTO> logStepInfoList  = new ArrayList<>();
        logStepInfoList.addAll(StepLogger.logStepInfoList);
        caseInfo.setCaseStopTime(System.currentTimeMillis());
        caseInfo.setStepInfoList(logStepInfoList);
        if (!StepLogger.isFail){
            caseInfo.setRunResult(RunResult.PASS);
        }else {
            caseInfo.setRunResult(RunResult.FAILED);
        }
//        moduleInfo.getCaseInfoList().add(caseInfo);
        StepLogger.logStepInfoList.clear();
        StepLogger.isFail = false;
        logger.info("测试用例:{},执行完成!",caseInfo.getCaseName());

        //清楚缓存的Case信息
        caseInfo = null;
    }

    /**
     * 测试方法开始运行
     * @param paramModuleInfo
     */
    public static void moduleRunStart(ModuleLogDTO paramModuleInfo) {
        logger.info("测试模块:{},开始执行...",paramModuleInfo.getModuleName());

        paramModuleInfo.setRunResult(RunResult.RUNNING);
        paramModuleInfo.setModuleStartTime(System.currentTimeMillis());
//        paramModuleInfo.setCaseInfoList(new ArrayList<CaseInfo>());
        moduleInfo = paramModuleInfo;
    }

    /**
     * 测试方法结束
     */
    public static void moduleRunStop(ModuleLogDTO paramModuleInfo) {
        int passCaseNum = 0;
        int failCaseNum = 0;
        moduleInfo.setModuleStopTime(System.currentTimeMillis());
        for (CaseLogDTO caseInfo :moduleInfo.getCaseInfoList()){
            if (RunResult.PASS.equals(caseInfo.getRunResult())){
                passCaseNum += 1;
            }else {
                failCaseNum += 1;
            }
        }
        moduleInfo.setFailCaseNum(failCaseNum);
        moduleInfo.setPassCaseNum(passCaseNum);
        if (failCaseNum == 0){
            moduleInfo.setRunResult(RunResult.PASS);
        }else {
            moduleInfo.setRunResult(RunResult.FAILED);
        }
        suiteInfo.getModuleInfoList().add(moduleInfo);
//        if (!"debug".equals(GlobalConfig.runMode)){
//            LogModule.moduleNum -= 1;
//        }
        logger.info("测试模块:{},执行完成!",moduleInfo.getModuleName());

        //清理缓存的模块运行数据
        moduleInfo = null;
        //如果还存在没有运行的方法则删除
        if (InitializeManager.waitRunModuleList.size() > 0){
            InitializeManager.waitRunModuleList.remove(0);
        }

    }

    /**
     * 测试套件开始运行
     * @param paramSuiteInfo
     */
    public static void suiteRunStart(SuiteLogDTO paramSuiteInfo) {
        String runMode = InitializeManager.globalProper.get("runmode");
        //判断项目的运行方式,是否需要扫描
        if ("debug".equals(runMode)){
            logger.info("测试套件:{},开始执行...",paramSuiteInfo.getSuiteName());
            paramSuiteInfo.setSuiteStartTime(System.currentTimeMillis());
            paramSuiteInfo.setRunResult(RunResult.RUNNING);
            paramSuiteInfo.setModuleInfoList(new ArrayList<ModuleLogDTO>());
            suiteInfo = paramSuiteInfo;
            return ;
        }
        paramSuiteInfo.setRunResult(RunResult.RUNNING);
        paramSuiteInfo.setSuiteStartTime(System.currentTimeMillis());
        suiteInfo = paramSuiteInfo;
        logger.info("测试套件:{},开始执行...",paramSuiteInfo.getSuiteName());

    }

    /**
     * 测试套件结束运行
     */
    public static void suiteRunStop() {
        int passCaseNum = 0;
        int failCaseNum = 0;
        SuiteLogDTO paramSuiteInfo = suiteInfo;
        paramSuiteInfo.setSuiteStopTime(System.currentTimeMillis());
        for (ModuleLogDTO moduleInfo:paramSuiteInfo.getModuleInfoList()){
            failCaseNum += moduleInfo.getFailCaseNum();
            passCaseNum += moduleInfo.getPassCaseNum();
        }
        if (failCaseNum == 0) {
            paramSuiteInfo.setRunResult(RunResult.PASS);
        }else {
            paramSuiteInfo.setRunResult(RunResult.FAILED);
        }

        testingLogDTO.getSuiteLogDTOList().add(paramSuiteInfo);
        logger.info("测试套件:{},执行完成!",paramSuiteInfo.getSuiteName());
        //清楚缓存数据
        suiteInfo = null;
    }
}
