package com.open.qa.common;


import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.SuiteLogDTO;
import com.open.qa.domain.TestingLogDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于每次初始化对象缓存
 * @author : liang.chen
 * create in 2018/7/26 下午3:53
 */
public class InitializeManager {

    /**
     * 需要初始化的实例
     */
    public static Map<String,Object> initializeMap = new HashMap<>();

    /**
     * 全局配置变量
     */
    public static Map<String,String> globalProper = new HashMap<>();

    /**
     * 环境配置
     */
    public static Map<String,String> envProper = new HashMap<>();

    /**
     * 所有待运行的Module
     */
    public static List<ModuleLogDTO> waitRunModuleList = new ArrayList<>();

    /**
     * 全局运行的SuiteInfo信息
     */
    public static List<SuiteLogDTO> suiteInfoList = new ArrayList<>();

    /**
     * 全局运行的项目日志
     */
    public static TestingLogDTO projectLogDTO = new TestingLogDTO();

    /**
     * 是否已经初始化数据
     */
    public static Boolean isInit = false;

}
