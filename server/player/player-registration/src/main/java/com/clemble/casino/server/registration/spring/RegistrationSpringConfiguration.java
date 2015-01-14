package com.clemble.casino.server.registration.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.server.key.SafeKeyFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.registration.PlayerKeyGenerator;
import com.clemble.casino.server.registration.ServerPlayerCredential;
import com.clemble.casino.server.registration.controller.PlayerSignOutServiceController;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.registration.repository.ServerPlayerCredentialRepository;
import com.clemble.casino.server.registration.security.ClembleConsumerDetailsService;
import com.clemble.casino.server.registration.security.SimpleClembleConsumerDetailsService;
import com.clemble.casino.server.security.PlayerTokenUtils;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.registration.controller.PlayerBaseRegistrationController;
import com.clemble.casino.server.registration.controller.PlayerManualRegistrationController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import scala.beans.BeanDescription;

import java.security.NoSuchAlgorithmException;

/**
 * Created by mavarazy on 7/4/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, OAuthSpringConfiguration.class, PlayerTokenSpringConfiguration.class, RedisSpringConfiguration.class, MongoSpringConfiguration.class})
public class RegistrationSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerKeyGenerator playerKeyGenerator(ServerPlayerCredentialRepository credentialRepository) {
        return new PlayerKeyGenerator(new SafeKeyFactory<ServerPlayerCredential>(10, credentialRepository));
    }

    @Bean
    public ServerPlayerCredentialRepository playerCredentialRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(ServerPlayerCredentialRepository.class);
    }

    @Bean
    public ClembleConsumerDetailsService clembleConsumerDetailsService() {
        return new SimpleClembleConsumerDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PlayerManualRegistrationController playerRegistrationController(
        PasswordEncoder passwordEncoder,
        PlayerTokenUtils tokenUtils,
        @Qualifier("playerKeyGenerator") PlayerKeyGenerator playerKeyGenerator,
        ServerPlayerCredentialRepository playerCredentialRepository,
        ClembleConsumerDetailsService clembleConsumerDetailsService,
        ClembleCasinoValidationService clembleValidationService,
        PlayerTokenFactory playerTokenFactory,
        SystemNotificationService systemNotificationService) throws NoSuchAlgorithmException {
        return new PlayerManualRegistrationController(passwordEncoder, tokenUtils, playerKeyGenerator, playerTokenFactory, playerCredentialRepository,
                clembleConsumerDetailsService, clembleValidationService, systemNotificationService);
    }

    @Bean
    public PlayerBaseRegistrationController playerBaseRegistrationController(
        PlayerTokenUtils tokenUtils,
        @Qualifier("playerRegistrationController") PlayerManualRegistrationService manualRegistrationService) {
        return new PlayerBaseRegistrationController(tokenUtils, manualRegistrationService);
    }

    @Bean
    public PlayerTokenUtils tokenUtils(
        @Value("${clemble.registration.token.host}") String host,
        @Value("${clemble.registration.token.maxAge}") int maxAge
    ){
        return new PlayerTokenUtils(host, maxAge);
    }

    @Bean
    public PlayerSignOutServiceController playerSignOutServiceController(
        @Value("${clemble.registration.token.host}") String host,
        PlayerTokenUtils tokenUtils) {
        return new PlayerSignOutServiceController("http://" + host.substring(1), tokenUtils);
    }

}
