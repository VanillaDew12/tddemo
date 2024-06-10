package com.example.tddemo.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Data
public class tdconfig implements InitializingBean {

    @Value("${Spring.jdbcUrl}")
    private String jdbcUrl;

    public static String jdbcurl;

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcurl = jdbcUrl;
    }
}
