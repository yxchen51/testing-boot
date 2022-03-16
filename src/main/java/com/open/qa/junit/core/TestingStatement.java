package com.open.qa.junit.core;

import com.open.qa.anotations.DataDriver;
import com.open.qa.anotations.DisplayName;
import com.open.qa.common.Constant;
import com.open.qa.common.SpringContextHolder;
import com.open.qa.domain.CaseLogDTO;
import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.StepType;
import com.open.qa.domain.TestingDataType;
import com.open.qa.junit.log.StepLogger;
import com.open.qa.junit.rule.DriverRule;
import com.open.qa.utils.ExcelUtil;
import com.google.common.collect.Lists;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 正常执行测试的Statement
 * Update by liang.chen on 2022/02/01
 */
public class TestingStatement extends Statement  {

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
                    logger.debug("用例:{},被设置成不执行，跳过...",caseInfo.getCaseName());
//                    System.out.println("用例："+caseInfo.getCaseName()+",被设置成不执行，跳过...");
                    //从模块中移除不执行的用例信息
                    caseInfoList.remove(caseInfo);
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
        //记录测试运行的名称
        String clazzDisplayName = desc.getTestClass().getSimpleName();
        String moduleDisplayName = desc.getMethodName();
        //判断是否使用了@DisplayName注解
        if (desc.getTestClass().isAnnotationPresent(DisplayName.class)){
            clazzDisplayName = desc.getTestClass().getAnnotation(DisplayName.class).value();
        }
        if (desc.getAnnotation(DisplayName.class) != null){
            moduleDisplayName = desc.getAnnotation(DisplayName.class).value();
        }
        moduleInfo.setModuleName(clazzDisplayName+"_"+moduleDisplayName);
        //判断是否使用了数据驱动
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
                    String caseName = map.containsKey(Constant.CASENAME) ? map.get(Constant.CASENAME) : "caseName";
                    String caseType = map.containsKey(Constant.CASETYPE) ? map.get(Constant.CASETYPE) : "正常流程";
                    String casePropertiy = map.containsKey(Constant.CASEPROPER) ? map.get(Constant.CASEPROPER) : "P0";
                    String caseDescriptions = map.containsKey(Constant.CASEDESC) ? map.get(Constant.CASEDESC) : "用例默认的描述信息";
                    boolean isRun = false;
                    if (map.containsKey(Constant.CASERUN)) {
                        isRun = map.get(Constant.CASERUN).equalsIgnoreCase("Y");
                    }
                    CaseLogDTO caseInfo = new CaseLogDTO();
                    caseInfo.setCaseName(caseName);
                    caseInfo.setCaseDesc(caseDescriptions);
                    caseInfo.setCaseParam(map);
                    caseInfo.setRun(isRun);
                    caseInfoList.add(caseInfo);
                }
                moduleInfo.setCaseInfoList(caseInfoList);
            }else if(TestingDataType.DB.equals(dataDriver.dataType())){
                //DB数据驱动读取的支持
                String sql = dataDriver.sql();
//                Boolean cons = SpringContextHolder.getApplicationContext().containsBean("jdbcTemplate");
                List<Map<String,Object>> resultList = SpringContextHolder.getApplicationContext().getBean(JdbcTemplate.class).queryForList(sql);
                if (CollectionUtils.isEmpty(resultList)) {
                    return;
                }
                for (Map<String, Object> map : resultList) {
                    String caseName = desc.getMethodName();
                    String caseType = "正常流程";
                    String casePropertiy = "P0";
                    String caseDescriptions = "用例默认的描述信息";
                    CaseLogDTO caseInfo = new CaseLogDTO();
                    caseInfo.setCaseName(caseName);
                    caseInfo.setCaseType(caseType);
                    caseInfo.setCaseProperties(casePropertiy);
                    caseInfo.setCaseDesc(caseDescriptions);
                    //转换
                    Map<String,String> newMap =new HashMap<String,String>();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
//                        if(entry.getValue() instanceof String){
                            newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
//                        }
                    }
                    caseInfo.setCaseParam(newMap);
                    caseInfo.setRun(true);
                    caseInfoList.add(caseInfo);
                }
                moduleInfo.setCaseInfoList(caseInfoList);
            }else {
                logger.warn("暂不支持其他的数据驱动");
            }
        }else {
            CaseLogDTO caseInfo = new CaseLogDTO();
            caseInfo.setCaseName(moduleDisplayName);
            caseInfo.setCaseDesc("用例的默认描述信息");
            caseInfo.setRun(true);
            moduleInfo.setCaseInfoList(Lists.newArrayList(caseInfo));
        }
        //测试方法开始运行
        RunObserver.moduleRunStart(moduleInfo);
    }

}
