package com.open.qa.spring.rpc;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import feign.Feign;
import feign.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * RPC调用接口的客户端
 * @author liang.chen
 */
public class RpcInvokeClient {

    /**
     * 构建一个fastJson的Converter
     * @return
     */
    public static  ObjectFactory<HttpMessageConverters> buildFastJsonConverter(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        HttpMessageConverters messageConverter = new HttpMessageConverters(fastJsonHttpMessageConverter);
        return () -> messageConverter;
    }

    /**
     * 创建可以调用的api对象
     * @param apiUrl
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T buildApi(String apiUrl,Class<T> clazz){
        Object service = null;
        if (clazz != null) {
            service = Feign.builder().logLevel(Logger.Level.FULL).logger(new Logger.JavaLogger()).contract(new SpringMvcContract())
                    .encoder(new SpringEncoder(buildFastJsonConverter()))
                    .decoder(new TraceDecoder(buildFastJsonConverter()))
                    .requestInterceptor(new DynamicInvokeInterceptor())
                    .target(new DynamicInvokeTarget<>(clazz, apiUrl));
        }
        return ((T) (service));
    }


    /**
     * 上传文件接口
     * @param url
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String uploadFile(String url,String filePath,String type)  {
        File file = new File(filePath);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("attachment", new FileSystemResource(file));
        bodyMap.add("type",type);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "*/*");
        headers.add(HttpHeaders.CONNECTION, "Keep-Alive");
        headers.add(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        headers.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE+";boundary="+ UUID.randomUUID().toString());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (HttpStatus.OK.equals(response.getStatusCode())){
            String body = response.getBody();
            return body;
        }
        System.err.println("上传图片失败,返回结果==>"+response.getBody());
        return null;
    }

    /**
     * 发送消息
     * @param url
     * @param object
     * @return
     */
    public static String sendRequest(String url,HttpHeaders headers,Object object){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> requestEntity = new HttpEntity<>(object,headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (HttpStatus.OK.equals(response.getStatusCode())){
            return response.getBody();
        }else {
            System.err.println("请求接口失败,返回吗为==>"+response.getStatusCode().value());
            return null;
        }
    }
    public static ResponseEntity<String>  sendRequest1(String url, Map<String,String> params,HttpHeaders headers){
        MultiValueMap<String, String> sendParams = new LinkedMultiValueMap<>();
        sendParams.setAll(params);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(sendParams, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (HttpStatus.OK.equals(response.getStatusCode())){
            return response;
        }else {
            System.err.println("请求接口失败,返回吗为==>"+response.getStatusCode().value());
            return response;
        }
    }


}
