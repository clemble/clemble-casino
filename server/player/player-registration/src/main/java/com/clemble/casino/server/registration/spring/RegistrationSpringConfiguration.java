package com.clemble.casino.server.registration.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.server.key.SafeKeyFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.registration.PlayerKeyGenerator;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.registration.repository.PlayerCredentialRepository;
import com.clemble.casino.server.registration.security.ClembleConsumerDetailsService;
import com.clemble.casino.server.registration.security.SimpleClembleConsumerDetailsService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.registration.controller.PlayerBaseRegistrationController;
import com.clemble.casino.server.registration.controller.PlayerManualRegistrationController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.security.NoSuchAlgorithmException;

/**
 * Created by mavarazy on 7/4/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, OAuthSpringConfiguration.class, PlayerTokenSpringConfiguration.class, RedisSpringConfiguration.class, MongoSpringConfiguration.class})
public class RegistrationSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerKeyGenerator playerKeyGenerator(PlayerCredentialRepository credentialRepository) {
        return new PlayerKeyGenerator(new SafeKeyFactory<PlayerCredential>(10, credentialRepository));
    }

    @Bean
    public PlayerCredentialRepository playerCredentialRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(PlayerCredentialRepository.class);
    }

    @Bean
    public ClembleConsumerDetailsService clembleConsumerDetailsService() {
        return new SimpleClembleConsumerDetailsService();
    }

    @Bean
    public PlayerManualRegistrationController playerRegistrationController(
            @Qualifier("playerKeyGenerator") PlayerKeyGenerator playerKeyGenerator,
            PlayerCredentialRepository playerCredentialRepository,
            ClembleConsumerDetailsService clembleConsumerDetailsService,
            ClembleCasinoValidationService clembleValidationService,
            PlayerTokenFactory playerTokenFactory,
            SystemNotificationService systemNotificationService) throws NoSuchAlgorithmException {
        return new PlayerManualRegistrationController(playerKeyGenerator, playerTokenFactory, playerCredentialRepository,
                clembleConsumerDetailsService, clembleValidationService, systemNotificationService);
    }

    @Bean
    public PlayerBaseRegistrationController playerBaseRegistrationController(@Qualifier("playerRegistrationController") PlayerManualRegistrationService manualRegistrationService) {
        return new PlayerBaseRegistrationController(manualRegistrationService);
    }
}
