package com.open.qa.spring.rpc;

import com.open.qa.common.ContextHolder;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

/**
 * 动态请求 主要是请求header拦截
 * @author liang.chen
 * @param <T>
 */
@Slf4j
public class DynamicInvokeTarget<T> implements Target<T> {
    private final Class<T> type;
    private final String name = "";
    private String apiHost;

    public DynamicInvokeTarget(Class<T> type, String apiHost) {
        this.type = type;
        this.apiHost = StringUtils.isNoneEmpty(apiHost) ? apiHost : this.apiHost;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String url() {
        return apiHost;
    }

    @Override
    public Request apply(RequestTemplate input) {
        String method = input.method();
        String url = input.url();
        input.insert(0, apiHost);
        long timeMillis = System.currentTimeMillis();
        if (ContextHolder.getParmaByKey("authorization") != null)
            input.header(HttpHeaders.AUTHORIZATION, ContextHolder.getParmaByKey("authorization").toString());
        if (ContextHolder.getParmaByKey("headerType") != null){
            input.header(HttpHeaders.CONTENT_TYPE,ContextHolder.getParmaByKey("headerType").toString());
        }else {
            input.header(HttpHeaders.CONTENT_TYPE,"application/json");
        }
        return input.request();
    }
}
