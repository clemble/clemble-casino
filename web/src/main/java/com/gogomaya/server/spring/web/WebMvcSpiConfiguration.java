package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.web.SocialConnectionDataController;

@Configuration
public class WebMvcSpiConfiguration extends WebMvcConfigurationSupport{

    @Inject SocialConnectionDataAdapter connectionDataAdapter;
    
    @Bean
    public SocialConnectionDataController connectionDataController(){
        return new SocialConnectionDataController(connectionDataAdapter);
    }
    
    @Bean
    public MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter(){
        return new MappingJacksonHttpMessageConverter();
    }
    
}
