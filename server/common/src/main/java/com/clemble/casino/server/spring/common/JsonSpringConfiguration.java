package com.clemble.casino.server.spring.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemble.casino.json.ObjectMapperUtils;

@Configuration
public class JsonSpringConfiguration implements SpringConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperUtils.OBJECT_MAPPER;
    }

}
