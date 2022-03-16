package com.open.qa.spring.rpc;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;


/**
 * 接口请求拦截器
 * @author liang.chen
 * */
@Slf4j
public class DynamicInvokeInterceptor implements RequestInterceptor {


    /**
     * 拦截请求加上请求日志
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        String url = template.url();
        if (!ObjectUtils.isEmpty(template.body())){
            String body = new String(template.body());
            log.info("请求接口==>{},请求参数==>{}",url,body);
        }else {
            log.info("请求接口==>{},请求参数为空",url);
        }
    }

}
