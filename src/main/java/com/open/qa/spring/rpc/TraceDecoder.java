package com.open.qa.spring.rpc;

import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 重写Decoder加入返回内容日志
 * @author liang.chen
 *
 */
@Slf4j
public class TraceDecoder extends SpringDecoder {

    public TraceDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    /**
     * 重写decoder方法
     * @param response
     * @param type
     * @return
     * @throws IOException
     * @throws FeignException
     */
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String bodyString = new BufferedReader(new InputStreamReader(response.body().asInputStream()))
                .lines()
                .parallel()
                .collect(Collectors.joining("\n"));
        String url = response.request().url();
        log.info("请求接口==>{},返回数据==>{}",url,bodyString);
        Response response1 =  response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build();
        return super.decode(response1,type);
    }



}



