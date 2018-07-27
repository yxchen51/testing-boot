package com.open.qa.junit.core;

import com.alibaba.fastjson.JSON;
import com.open.qa.anotations.RunWeight;
import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.SuiteLogDTO;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/13 下午2:54
 */
public class SpringTestingRunner extends SpringJUnit4ClassRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringTestingRunner.class);

    private final Description description;
    private final TestClass tClazz ;
    private final RunObserver runObserver;

    public SpringTestingRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.description = getDescription();
        this.tClazz = getTestClass();
        this.runObserver = (RunObserver) getTestContextManager().getTestContext().getAttribute("runObserver");
    }


    /**
     * 重写Run方法，加入运行后的操作
     * @param notifier
     */
    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        try {
            Statement statement = classBlock(notifier);
            beforeRunner();
            statement.evaluate();
            afterRunner();
        } catch (AssumptionViolatedException e) {
            testNotifier.addFailedAssumption(e);
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }



    /**
     * 重写classBlock方法，加入需要在执行前后进行的操作
     * @param notifier
     * @return
     */
    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        Statement statement = childrenInvoker(notifier);
        statement = withBeforeClasses(statement);
        statement = withAfterClasses(statement);
//        statement = withClassRules(statement);
        return statement;
    }




    /**
     * 重写获取子类的方法，按权重排序执行
     * @return
     */
    @Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> methodList =  computeTestMethods();
        return  sortMethod(methodList);
    }

    /**
     * 判单是否有注解
     * @param method
     * @return
     */
    public RunWeight getRunSort(FrameworkMethod method){
        if (method.getMethod().isAnnotationPresent(RunWeight.class)){
            return method.getAnnotation(RunWeight.class);
        }else {
            return null;
        }
    }

    /**
     * 根据运行方法的权重进行排序
     * @param methodList
     * @return
     */
    private List<FrameworkMethod> sortMethod(List<FrameworkMethod> methodList){
        //存放有注解的排序
        List<FrameworkMethod> isSortMethodList = new ArrayList<>();
        //权重为Integer.MAX_VALUE的方法
        List<FrameworkMethod> maxWeightMethodList = new ArrayList<>();
        //没有注解的方法
        List<FrameworkMethod> normalMethodList = new ArrayList<>();
        //分离有注解和没有注解的方法
        for (FrameworkMethod frameworkMethod : methodList){
            if (frameworkMethod.getMethod().isAnnotationPresent(RunWeight.class)){
                RunWeight runWeight = getRunSort(frameworkMethod);
                if (Integer.MAX_VALUE == runWeight.weight()){
                    maxWeightMethodList.add(frameworkMethod);
                }else {
                    isSortMethodList.add(frameworkMethod);
                }
            }else {
                normalMethodList.add(frameworkMethod);
            }
        }
        //处理数据格式
        FrameworkMethod[] frameworkMethods = new FrameworkMethod[isSortMethodList.size()];
        int k = 0;
        for (FrameworkMethod method : isSortMethodList){
            frameworkMethods[k] = method;
            k++;
        }
        //排序
        for (int i=0;i<frameworkMethods.length-1;i++){
            for (int j=0;j<frameworkMethods.length-i-1;j++){
                int weight1 = getRunSort(frameworkMethods[j]).weight();
                int weight2 = getRunSort(frameworkMethods[j+1]).weight();
                //逻辑判断
                if (weight1>weight2){
                    FrameworkMethod frameworkMethodTemp = frameworkMethods[j];
                    frameworkMethods[j] = frameworkMethods[j+1];
                    frameworkMethods[j+1] = frameworkMethodTemp;
                }
            }
        }

        List<FrameworkMethod> realMethodList = new ArrayList<>(Arrays.asList(frameworkMethods));
        realMethodList.addAll(normalMethodList);
        realMethodList.addAll(maxWeightMethodList);
        return realMethodList;
    }


    /**
     * 在runner运行前运行
     * 用于初始化测试配置，注入测试所需的实例等
     */
    protected void beforeRunner(){
        logger.debug("Get Run Observer:{}", JSON.toJSONString(RunObserver.getTestingLogDTO()));
        RunObserver.suiteRunStart(initSuiteInfo());
    }

    /**
     * 在runner运行后执行
     * 用于现场清理，判断测试出口，聚合日志等功能
     */
    protected void afterRunner(){
        RunObserver.suiteRunStop();
    }


    /**
     * 初始化测试类的运行信息
     * @return
     */
    private SuiteLogDTO initSuiteInfo(){
        SuiteLogDTO suiteInfoInit = new SuiteLogDTO();
        suiteInfoInit.setSuiteName(tClazz.getJavaClass().getSimpleName());
        suiteInfoInit.setModuleInfoList(new ArrayList<ModuleLogDTO>());
        return suiteInfoInit;
    }

}
