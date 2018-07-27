package com.open.qa.spring;

import com.alibaba.fastjson.JSON;
import com.open.qa.common.DynamicDataSourceAspect;
import com.open.qa.common.InitializeManager;
import com.open.qa.domain.TestingLogDTO;
import com.open.qa.junit.core.RunObserver;
import com.open.qa.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContextManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/12 下午4:58
 */
public class SpringTestingListener extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringTestingListener.class);

    private static final String GLOBAL_PROPERTIES_PATH = "/conf/config.properties";

    private static final String LOG_PUSH_PATH = "/api/log/save";

    protected ApplicationContext applicationContext;

    private final TestContextManager testContextManager;


    public SpringTestingListener(){
       this.testContextManager = new TestContextManager(getClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }


    public TestContextManager getTestContextManager(){
        return this.testContextManager;
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    /**
     * 整个测试项目的测试前置初始化：
     * 1.初始化测试框架的日志监听器
     * 2.初始化测试项目的环境信息
     * 3.初始化全局变量
     */
    @PostConstruct
    public void  testingStartListener(){
        String buildId = UUID.randomUUID().toString();
        logger.debug("testingStartListener start,buildId={} ....",buildId);
        System.out.println("本次运行的buildId为:"+buildId);
        //初始化全局配置信息
        InitializeManager.globalProper = PropertiesUtil.loadProperties(GLOBAL_PROPERTIES_PATH);
        //初始化环境配置
        loadEnvProper();
        //初始化RunObserver
        TestingLogDTO testingLogDTO = new TestingLogDTO();
        testingLogDTO.setBuildId(buildId);
        testingLogDTO.setProjectName(InitializeManager.globalProper.get("project.name"));
        testingLogDTO.setProjectStartTime(System.currentTimeMillis());
        testingLogDTO.setRunner(InitializeManager.globalProper.get("runner.name"));
        testingLogDTO.setRunEnvironment(InitializeManager.globalProper.get("run.env"));
        testingLogDTO.setSuiteLogDTOList(new ArrayList<>());
        RunObserver.setTestingLogDTO(testingLogDTO);
    }


    /**
     * 测试完成的后置监听器:
     * 1.停止相关的测试进程
     * 2.收集测试日志发送到平台
     * 3.生成本次测试的报告
     */
    @PreDestroy
    public void testingFinishListener(){
        logger.debug("testingFinishListener stop ....");
        TestingLogDTO testingLogDTO = RunObserver.getTestingLogDTO();
        testingLogDTO.setProjectStopTime(System.currentTimeMillis());
        if ("true".equals(InitializeManager.globalProper.get("log.push"))){
            String logServerDomain = InitializeManager.globalProper.get("log.server.domain");
            new RestTemplate().postForEntity(logServerDomain + LOG_PUSH_PATH,RunObserver.getTestingLogDTO(),null);
        }
        System.out.println("运行的日志为:\n"+JSON.toJSONString(RunObserver.getTestingLogDTO()));
    }


    /**
     * 判断执行的环境加载配置文件
     */
    public void loadEnvProper(){
        //判断是否加载环境配置文件
        /**
         * 如果maven中传入了运行环境的信息，则从System中获取
         * 没有则使用代码中的注解
         */
        String envTag = "";
        if (System.getProperties().containsKey("run.env")){
            envTag = System.getProperty("run.env").trim().toLowerCase();
        }else {
            envTag = InitializeManager.globalProper.get("run.env");
        }
        String propertiesPath = "/env/environment-" + envTag + ".properties";
        try {
            InitializeManager.envProper =  PropertiesUtil.loadProperties(propertiesPath);
            logger.info("---测试环境配置文件加载完成，当前环境为:------{}-----",envTag);
        }catch (Exception e){
            throw new RuntimeException("测试环境配置文件:"+envTag+"加载失败,跳过该测试类...");
        }
    }

}
