package com.open.qa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 配置文件加载的工具类
 * Created by liang.chen on 2017/3/22.
 */
public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 加载配置文件
     * @param filePath
     * @return
     */
    public static Map<String,String> loadProperties(String filePath){
        Map<String,String> envProperties = new HashMap<>();
        Properties prop = new Properties();
        InputStream is = prop.getClass().getResourceAsStream(filePath);
        try {
            prop.load(is);
        } catch (IOException e) {
            logger.error("加载配置文件:{}发生异常",filePath,e);
            e.printStackTrace();
        }finally {
            try {
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        for (Object key : prop.keySet()) {
            envProperties.put((String) key, (String) prop.get(key));
        }
        return envProperties;
    }
    /**
     * 加载配置文件
     * @param filePath
     * @return
     */
    public static Properties loadPropertiesFile(String filePath){
        Properties prop = new Properties();
        InputStream is = prop.getClass().getResourceAsStream(filePath);
        try {
            prop.load(is);
        } catch (IOException e) {
            logger.error("加载配置文件:{}发生异常",filePath,e);
        }finally {
            try {
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return prop;
    }

}
