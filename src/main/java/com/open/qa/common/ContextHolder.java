package com.open.qa.common;

import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 用于整个测试执行过程中的参数传递
 * @author : liang.chen
 * create in 2021/4/15 下午2:38
 */
public class ContextHolder {
    private static ThreadLocal<Map<String,Object>> contextParma = new ThreadLocal<>();

    public static Map<String,Object> geContextParma(){
        return contextParma.get();
    }

    public static void setContextParma(Map<String,Object> parma){
        contextParma.set(parma);
    }

    public static Object getParmaByKey(String key){
        if (ObjectUtils.isEmpty(contextParma.get())){
            return null;
        }
        return contextParma.get().get(key);
    }

    public static void  setParma(String key,Object value){
        if (ObjectUtils.isEmpty(contextParma.get())){
            contextParma.set(new HashMap<String,Object>());
        }
        contextParma.get().put(key,value);
    }

    /**
     * 删除某个键
     * @param key
     * @return
     */
    public static Boolean removeKey(String key){
        if (!ObjectUtils.isEmpty(contextParma.get()) && contextParma.get().containsKey(key)){
            contextParma.get().remove(key);
            return true;
        }
        return false;
    }

    /**
     * 清除所有键
     * @return
     */
    public static Boolean removeAll(){
        if (ObjectUtils.isEmpty(contextParma.get())){
            contextParma.remove();
            return true;
        }
        return false;
    }


    /**
     * 终中断测试
     */
    public static void interruptTest(){
        setParma("interrupt",true);
    }

    /**
     * 判断当前是否中断测试
     * @return
     */
    public static Boolean isTestInterrupted(){
        return ObjectUtils.isEmpty(getParmaByKey("interrupt")) ? false : (Boolean) getParmaByKey("interrupt");
    }
}
