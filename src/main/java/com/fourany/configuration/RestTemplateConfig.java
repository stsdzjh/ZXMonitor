package com.fourany.configuration;

import com.fourany.factory.HttpsClientRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
 
 
@Configuration
@Slf4j
public class RestTemplateConfig {
 
    @Bean
    public RestTemplate restTemplate(){
        HttpsClientRequestFactory factory = new HttpsClientRequestFactory();
        //单位为ms (部分接口数据量大,读取改为60秒)
        factory.setReadTimeout(60000);
        //单位为ms
        factory.setConnectTimeout(10000);
        return new RestTemplate (factory);
    }
}