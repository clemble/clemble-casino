package com.gogomaya.server.spring.common;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.json.ObjectMapperUtils;

@Configuration
public class JsonSpringConfiguration implements SpringConfiguration {

    @Bean @Singleton
    public ObjectMapper objectMapper() {
        return ObjectMapperUtils.createObjectMapper();
    }

}
