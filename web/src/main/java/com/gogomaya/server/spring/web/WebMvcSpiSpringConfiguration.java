package com.gogomaya.server.spring.web;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.gogomaya.server.spring.web.player.PlayerWebSpringConfiguration;

@Configuration
@Import({ WebCommonSpringConfiguration.class, TicTacToeWebSpringConfiguration.class, PlayerWebSpringConfiguration.class, PaymentWebSpringConfiguration.class })
public class WebMvcSpiSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gogomayaValidationService")
    public GogomayaValidationService gogomayaValidationService;

    @Bean
    @Singleton
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

}
