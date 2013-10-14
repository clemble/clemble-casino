package com.clemble.casino.server.spring.common;

import javax.validation.Validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.player.lock.JavaPlayerLockService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.error.GogomayaValidationService;

@Configuration
@Import({ PropertiesSpringConfiguration.class,
        RabbitSpringConfiguration.class,
        JPASpringConfiguration.class,
        CouchbaseSpringConfiguration.class })
public class CommonSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerLockService playerLockService() {
        return new JavaPlayerLockService();
    }

    @Bean
    public GogomayaValidationService gogomayaValidationService() {
        return new GogomayaValidationService(Validation.buildDefaultValidatorFactory());
    }

}
