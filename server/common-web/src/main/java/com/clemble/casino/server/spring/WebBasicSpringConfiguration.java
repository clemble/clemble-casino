package com.clemble.casino.server.spring;

import com.clemble.casino.server.error.ClembleCasinoHandlerExceptionResolver;
import com.clemble.casino.server.security.AuthenticationHandleInterceptor;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Created by mavarazy on 8/9/14.
 */
@Configuration
@Import({ WebJsonSpringConfiguration.class })
public class WebBasicSpringConfiguration extends WebMvcConfigurationSupport implements SpringConfiguration {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter;

    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {

        RequestMappingHandlerAdapter handlerAdapter = super.requestMappingHandlerAdapter();
        handlerAdapter.setMessageConverters(ImmutableList.<HttpMessageConverter<?>> of(jacksonHttpMessageConverter));

        return handlerAdapter;
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new ClembleCasinoHandlerExceptionResolver(objectMapper);
    }

}
