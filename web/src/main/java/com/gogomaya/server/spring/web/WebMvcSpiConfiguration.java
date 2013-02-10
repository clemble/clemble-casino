package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.GamerProfileRepository;
import com.gogomaya.server.user.SocialConnectionData;
import com.gogomaya.server.web.GenericSchemaController;
import com.gogomaya.server.web.social.SocialConnectionDataController;
import com.gogomaya.server.web.user.GameProfileContoller;

@Configuration
public class WebMvcSpiConfiguration extends WebMvcConfigurationSupport {

    @Inject
    SocialConnectionDataAdapter connectionDataAdapter;
    
    @Inject
    GamerProfileRepository gamerProfileRepository;

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
        genericSchemaController.addSchemaMapping("profile", GamerProfile.class);
        return genericSchemaController;
    }
    
    @Bean
    public GameProfileContoller gameProfileContoller() {
        return new GameProfileContoller(gamerProfileRepository);
    }

}
