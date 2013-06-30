package com.gogomaya.server.spring.common;

import javax.inject.Singleton;
import javax.validation.Validation;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.lock.JavaPlayerLockService;
import com.gogomaya.server.player.lock.PlayerLockService;

@Configuration
@Import({ CommonSpringConfiguration.Cloud.class, RabbitSpringConfiguration.class, JPASpringConfiguration.class, RedisSpringConfiguration.class })
public class CommonSpringConfiguration {

    @Bean
    @Singleton
    public PlayerLockService playerLockService() {
        return new JavaPlayerLockService();
    }

    @Bean
    @Singleton
    public GogomayaValidationService validationService() {
        return new GogomayaValidationService(Validation.buildDefaultValidatorFactory());
    }

    @Configuration
    @Profile(value = "cloud")
    public static class Cloud {

        @Bean
        @Singleton
        public CloudEnvironment cloudEnvironment() {
            return new CloudEnvironment();
        }

    }

}
