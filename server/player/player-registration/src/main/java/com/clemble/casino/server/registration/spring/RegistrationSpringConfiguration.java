package com.clemble.casino.server.registration.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.server.id.IdGenerator;
import com.clemble.casino.server.id.RedisIdGenerator;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.registration.repository.PlayerCredentialRepository;
import com.clemble.casino.server.registration.security.ClembleConsumerDetailsService;
import com.clemble.casino.server.registration.security.SimpleClembleConsumerDetailsService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.registration.controller.PlayerBaseRegistrationController;
import com.clemble.casino.server.registration.controller.PlayerManualRegistrationController;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mavarazy on 7/4/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, OAuthSpringConfiguration.class, PlayerTokenSpringConfiguration.class, RedisSpringConfiguration.class})
public class RegistrationSpringConfiguration implements SpringConfiguration {

    @Bean
    public IdGenerator playerIdGenerator(JedisPool jedisPool) {
        return new RedisIdGenerator("PLAYER_COUNTER", "P", jedisPool);
    }

    @Bean
    public MongoRepositoryFactory playerRegistrationRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public PlayerCredentialRepository playerCredentialRepository(@Qualifier("playerRegistrationRepositoryFactory") MongoRepositoryFactory playerRegistrationRepositoryFactory) {
        return playerRegistrationRepositoryFactory.getRepository(PlayerCredentialRepository.class);
    }

    @Bean
    public ClembleConsumerDetailsService clembleConsumerDetailsService() {
        return new SimpleClembleConsumerDetailsService();
    }

    @Bean
    public PlayerManualRegistrationController playerRegistrationController(
            @Qualifier("playerIdGenerator") IdGenerator idGenerator,
            PlayerCredentialRepository playerCredentialRepository,
            ClembleConsumerDetailsService clembleConsumerDetailsService,
            ClembleCasinoValidationService clembleValidationService,
            PlayerTokenFactory playerTokenFactory,
            SystemNotificationService systemNotificationService) throws NoSuchAlgorithmException {
        return new PlayerManualRegistrationController(idGenerator, playerTokenFactory, playerCredentialRepository,
                clembleConsumerDetailsService, clembleValidationService, systemNotificationService);
    }

    @Bean
    public PlayerBaseRegistrationController playerBaseRegistrationController(@Qualifier("playerRegistrationController") PlayerManualRegistrationService manualRegistrationService) {
        return new PlayerBaseRegistrationController(manualRegistrationService);
    }
}
