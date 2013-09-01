package com.gogomaya.server.spring.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.spring.common.JsonSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaRESTErrorHandler;

@Configuration
@Import({ JsonSpringConfiguration.class })
public class ClientRestCommonSpringConfiguration {

    @Inject
    @Named("objectMapper")
    public ObjectMapper objectMapper;

    @Bean
    @Singleton
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) messageConverter).setObjectMapper(objectMapper);
            }
        }

        restTemplate.setErrorHandler(new GogomayaRESTErrorHandler(objectMapper));
        return restTemplate;
    }

}
