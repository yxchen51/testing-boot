package com.open.qa.junit.core;

import com.google.common.collect.Lists;
import com.open.qa.anotations.DataDriver;
import com.open.qa.domain.CaseLogDTO;
import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.StepType;
import com.open.qa.domain.TestingDataType;
import com.open.qa.junit.log.StepLogger;
import com.open.qa.junit.rule.DriverRule;
import com.open.qa.utils.ExcelUtil;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.open.qa.common.Constant.CASEDESC;
import static com.open.qa.common.Constant.CASENAME;
import static com.open.qa.common.Constant.CASEPROPER;
import static com.open.qa.common.Constant.CASERUN;
import static com.open.qa.common.Constant.CASETYPE;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/16 上午9:49
 */
public class TestingStatement extends Statement {

    private static final Logger logger = LoggerFactory.getLogger(TestingStatement.class);

    private static final String BASE_DATA_FILE_PATH = "src/test/resources/data/";

    private final Statement base;

    private final Description desc;

    private final DriverRule driverRule;




    public TestingStatement(Statement statement,Description description,DriverRule rule) {
        this.base = statement;
        this.desc = description;
        this.driverRule = rule;
        initDriverData();
    }

    @Override
    public void evaluate() throws Throwable {
        ArrayList<CaseLogDTO> caseInfoList = (ArrayList<CaseLogDTO>) RunObserver.getModuleInfo().getCaseInfoList();
        if (CollectionUtils.isEmpty(caseInfoList)){
            return;
        }
        ArrayList<CaseLogDTO> caseInfosTemp  = (ArrayList<CaseLogDTO>)caseInfoList.clone();
        for (CaseLogDTO caseInfo :caseInfosTemp){
            try{
                driverRule.setCaseParam(caseInfo.getCaseParam());
                if (!caseInfo.getRun()){
                    logger.debug("用例:{},被设置成不执行，跳过...");
//                    System.out.println("用例："+caseInfo.getCaseName()+",被设置成不执行，跳过...");
                    //从模块中移除不执行的用例信息
//                    caseInfoList.remove(caseInfo);
                    continue;
                }
                RunObserver.caseRunStart(caseInfo);
                base.evaluate();
                RunObserver.caseRunStop();
            }catch (Throwable throwable){
                StepLogger.logStepFail(StepType.ACTION,"执行测试用例:"+caseInfo.getCaseName()+"失败！",throwable.toString());
                RunObserver.caseRunStop();
                throwable.printStackTrace();
            }
        }
        //测试模块执行结束
        RunObserver.moduleRunStop(RunObserver.getModuleInfo());
    }


    /**
     * 初始化数据驱动需要的测试参数
     */
    private void  initDriverData(){
        ModuleLogDTO moduleInfo = new ModuleLogDTO();
        List<CaseLogDTO> caseInfoList = new ArrayList<>();
        moduleInfo.setModuleName(desc.getTestClass().getSimpleName()+"."+desc.getMethodName());
        /**
         * 判断是否使用了数据驱动
         */
        DataDriver dataDriver = desc.getAnnotation(DataDriver.class);
        if (dataDriver != null){
            if (dataDriver.dataType() == TestingDataType.EXCEL){
                String filePath = BASE_DATA_FILE_PATH + dataDriver.filePath();
                String sheetName = dataDriver.sheetName();
                ExcelUtil excelUtil = new ExcelUtil(filePath);
                List<Map<String,String>> caseDataList =  excelUtil.excelDatas(sheetName);
                if (caseDataList == null) {
                    return;
                }
                for (Map<String, String> map : caseDataList) {
                    String caseName = map.containsKey(CASENAME) ? map.get(CASENAME) : "caseName";
                    String caseType = map.containsKey(CASETYPE) ? map.get(CASETYPE) : "正常流程";
                    String casePropertiy = map.containsKey(CASEPROPER) ? map.get(CASEPROPER) : "P0";
                    String caseDescriptions = map.containsKey(CASEDESC) ? map.get(CASEDESC) : "用例默认的描述信息";
                    boolean isRun = false;
                    if (map.containsKey(CASERUN)) {
                        isRun = map.get(CASERUN).equalsIgnoreCase("Y");
                    }
                    CaseLogDTO caseInfo = new CaseLogDTO();
                    caseInfo.setCaseName(caseName);
                    caseInfo.setCaseDesc(caseDescriptions);
                    caseInfo.setCaseParam(map);
                    caseInfo.setRun(isRun);
                    caseInfoList.add(caseInfo);
                }
                moduleInfo.setCaseInfoList(caseInfoList);
            }else {
                //TODO 其他数据驱动读取的支持
                logger.warn("暂不支持其他的数据驱动");
            }
        }else {
            CaseLogDTO caseInfo = new CaseLogDTO();
            caseInfo.setCaseName(desc.getMethodName());
            caseInfo.setCaseDesc("用例的默认描述信息");
            caseInfo.setRun(true);
            moduleInfo.setCaseInfoList(Lists.newArrayList(caseInfo));
        }
        //测试方法开始运行
        RunObserver.moduleRunStart(moduleInfo);
    }
}
