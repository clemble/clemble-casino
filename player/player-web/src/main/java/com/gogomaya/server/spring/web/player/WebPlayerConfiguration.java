package com.gogomaya.server.spring.web.player;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.registration.RegistrationSocialConnectionController;

@Configuration
@Import(value = {SocialModuleSpringConfiguration.class})
public class WebPlayerConfiguration extends WebMvcConfigurationSupport {

    @Inject
    SocialConnectionDataAdapter connectionDataAdapter;

    @Inject
    PlayerProfileRepository playerProfileRepository;

    @Inject
    PlayerCredentialRepository playerCredentialRepository;

    @Inject
    PlayerIdentityRepository playerIdentityRepository;
    
    @Inject
    PlayerRegistrationService playerRegistrationService;

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
    public RegistrationSocialConnectionController registrationSocialConnectionController() {
        return new RegistrationSocialConnectionController(connectionDataAdapter, playerIdentityRepository, validationService);
    }

    @Bean
    public RegistrationSignInContoller registrationSignInContoller() {
        return new RegistrationSignInContoller(playerRegistrationService, validationService);
    }

    @Bean
    public RegistrationLoginController registrationLoginController() {
        return new RegistrationLoginController(playerCredentialRepository, playerIdentityRepository);
    }

    @Bean
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

}
