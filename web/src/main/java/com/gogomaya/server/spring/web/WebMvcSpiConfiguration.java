package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.user.SocialConnectionData;
import com.gogomaya.server.web.GenericSchemaController;
import com.gogomaya.server.web.social.SocialConnectionDataController;

@Configuration
public class WebMvcSpiConfiguration extends WebMvcConfigurationSupport {

    @Inject
    SocialConnectionDataAdapter connectionDataAdapter;

    @Bean
    public SocialConnectionDataController connectionDataController() {
        return new SocialConnectionDataController(connectionDataAdapter);
    }

    @Bean
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    public GenericSchemaController jsonSchemaController() {
        GenericSchemaController genericSchemaController = new GenericSchemaController();
        genericSchemaController.addSchemaMapping("social", SocialConnectionData.class);
        return genericSchemaController;
    }

    @Bean
    public MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter() {
        return new MappingJacksonHttpMessageConverter();
    }

}
