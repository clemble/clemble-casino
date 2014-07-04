package com.clemble.casino.server.spring.web.player;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.*;
import com.clemble.casino.server.repository.player.PlayerSessionRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.clemble.casino.server.spring.web.OAuthSpringConfiguration;
import com.clemble.casino.server.web.management.PlayerSessionController;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerPresenceController;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;

@Configuration
@Import(value = {CommonSpringConfiguration.class, RedisSpringConfiguration.class})
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "player");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public PlayerSessionRepository playerSessionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerSessionRepository.class);
    }


    @Bean
    public PlayerPresenceController playerPresenceController(ServerPlayerPresenceService playerPresenceServerService) {
        return new PlayerPresenceController(playerPresenceServerService);
    }

    @Bean
    public ServerPlayerPresenceService playerPresenceServerService(JedisPool jedisPool, PlayerNotificationService playerPresenceNotificationService, SystemNotificationService systemNotificationService) {
        return new JedisServerPlayerPresenceService(jedisPool, playerPresenceNotificationService, systemNotificationService);
    }

    @Bean(destroyMethod = "close")
    public PlayerPresenceCleaner playerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService presenceService) {
        return new JedisPlayerPresenceCleaner(jedisPool, presenceService);
    }


    @Bean
    public PlayerSessionController playerSessionController(
            ResourceLocationService resourceLocationService,
            PlayerSessionRepository playerSessionRepository,
            ServerPlayerPresenceService playerStateManager) {
        return new PlayerSessionController(resourceLocationService, playerSessionRepository, playerStateManager);
    }

    @Bean
    public ServerRegistryConfiguration serverRegistryConfiguration(
        @Value("${clemble.management.configuration.notification}") String notificationBase,
        @Value("${clemble.management.configuration.player}") String playerBase,
        @Value("${clemble.management.configuration.payment}") String paymentBase,
        @Value("${clemble.management.configuration.game}") String gameBase) {
        return new ServerRegistryConfiguration(notificationBase, playerBase, paymentBase, gameBase);
    }

    @Bean
    public ResourceLocationService resourceLocationService(ServerRegistryConfiguration serverRegistryConfiguration) {
        SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
        return new SimpleResourceLocationService(configurationService, serverRegistryConfiguration);
    }

}