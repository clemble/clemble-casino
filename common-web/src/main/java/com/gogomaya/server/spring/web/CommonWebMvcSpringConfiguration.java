package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CommonWebMvcSpringConfiguration extends WebMvcConfigurationSupport {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MappingJackson2HttpMessageConverter jacksonHttpMessageConverter;

}
