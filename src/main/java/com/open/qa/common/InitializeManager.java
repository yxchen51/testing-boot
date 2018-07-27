package com.open.qa.common;


import com.open.qa.domain.ModuleLogDTO;
import com.open.qa.domain.SuiteLogDTO;
import com.open.qa.domain.TestingLogDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 用于存储每次初始化的对象
 * Created by liang.chen on 2017/3/21.
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
