package com.clemble.casino.server.spring.common;

import javax.validation.Validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.player.lock.JavaPlayerLockService;
import com.clemble.casino.server.player.lock.PlayerLockService;

@Configuration
@Import({ PropertiesSpringConfiguration.class,
        RabbitSpringConfiguration.class})
public class CommonSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerLockService playerLockService() {
        return new JavaPlayerLockService();
    }

    @Bean
    public ClembleCasinoValidationService gogomayaValidationService() {
        return new ClembleCasinoValidationService(Validation.buildDefaultValidatorFactory());
    }

}
