package com.example.tddemo.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/TDengine")
public class tdconfig implements InitializingBean {

    @Value("${jdbcUrl}")
    private String jdbcUrl;

    public static String jdbcurl;

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcurl = jdbcUrl;
    }
}
