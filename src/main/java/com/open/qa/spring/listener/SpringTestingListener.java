package com.open.qa.spring.listener;

import com.alibaba.fastjson.JSON;
import com.open.qa.common.InitializeManager;
import com.open.qa.domain.TestingLogDTO;
import com.open.qa.junit.core.RunObserver;
import com.open.qa.utils.PropertiesUtil;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestContextManager;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Spring test启动相关
 * @author : liang.chen
 * create in 2018/7/12 下午4:58
 */
public class SpringTestingListener implements ApplicationContextAware,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SpringTestingListener.class);

    private static final String GLOBAL_PROPERTIES_PATH = "/conf/config.properties";

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


    public Producer<String, String>  producer(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, InitializeManager.globalProper.get("kafka.server.domain"));
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.ACKS_CONFIG,"0");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;
    }



    /**
     * 测试完成的后置监听器:
     * 1.停止相关的测试进程
     * 2.收集测试日志发送到平台
     * 3.生成本次测试的报告
     */
    public void destroy(){
        logger.debug("testingFinishListener stop ....");
        TestingLogDTO testingLogDTO = RunObserver.getTestingLogDTO();
        testingLogDTO.setProjectStopTime(System.currentTimeMillis());
        if ("true".equals(InitializeManager.globalProper.get("log.push"))){
            String topic = InitializeManager.globalProper.get("kafka.log.topic");
            Future<RecordMetadata>  result = producer().send(new ProducerRecord<String, String>(topic,testingLogDTO.getBuildId(), JSON.toJSONString(testingLogDTO)));
            try {
                RecordMetadata metadata = result.get();
            }catch (Exception e){
                e.printStackTrace();
            }
            logger.info("日志发送Kafka成功，BuildId==>{}",testingLogDTO.getBuildId());
//            new RestTemplate().postForEntity(logServerDomain + LOG_PUSH_PATH,RunObserver.getTestingLogDTO(),null);
        }
        logger.info("运行的日志为:\n"+JSON.toJSONString(RunObserver.getTestingLogDTO()));
    }


    /**
     * 在runner运行前运行
     * 用于初始化测试配置，注入测试所需的实例等
     */
    protected void beforeRunner(){
        String buildId = UUID.randomUUID().toString().replace("-","").substring(0,16);
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

    /**
     * 整个测试项目的测试前置初始化：
     * 1.初始化测试框架的日志监听器
     * 2.初始化测试项目的环境信息
     * 3.初始化全局变量
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //关闭系统前处理日志相关
        Runtime.getRuntime().addShutdownHook(new Thread(() -> destroy()));
    }
}
