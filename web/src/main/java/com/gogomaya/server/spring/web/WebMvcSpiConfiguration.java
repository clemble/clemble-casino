package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.SocialConnectionData;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.GenericSchemaController;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;

@Configuration
@Import({WebGameConfiguration.class, WebPlayerConfiguration.class})
public class WebMvcSpiConfiguration extends WebMvcConfigurationSupport {

    @Inject
    GogomayaValidationService validationService;

    @Inject
    ObjectMapper objectMapper;

    @Bean
    public MappingJacksonHttpMessageConverter jacksonHttpMessageConverter() {
        MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    public GenericSchemaController jsonSchemaController() {
        GenericSchemaController genericSchemaController = new GenericSchemaController();
        genericSchemaController.addSchemaMapping("profile", PlayerProfile.class);
        genericSchemaController.addSchemaMapping("social", SocialConnectionData.class);
        genericSchemaController.addSchemaMapping("identity", PlayerIdentity.class);
        genericSchemaController.addSchemaMapping("credentials", PlayerCredential.class);
        genericSchemaController.addSchemaMapping("registration", RegistrationRequest.class);
        genericSchemaController.addSchemaMapping("error", GogomayaError.class);
        return genericSchemaController;
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

}
