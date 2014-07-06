package com.clemble.casino.server.spring.web.player;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.*;
import com.clemble.casino.server.player.presence.listener.PlayerPresenceGameEndedListener;
import com.clemble.casino.server.player.presence.listener.PlayerPresenceGameStartedEventListener;
import com.clemble.casino.server.repository.player.PlayerSessionRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.PresenceServiceSpringConfiguration;
import com.clemble.casino.server.web.management.PlayerSessionController;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.web.player.PlayerPresenceController;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;

@Configuration
@Import(value = {CommonSpringConfiguration.class, PresenceServiceSpringConfiguration.class})
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public PlayerPresenceGameEndedListener playerPresenceGameEndedListener(ServerPlayerPresenceService presenceService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerPresenceGameEndedListener networkCreationService = new PlayerPresenceGameEndedListener(presenceService);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerPresenceGameStartedEventListener playerPresenceGameStartedListener(ServerPlayerPresenceService presenceService, SystemNotificationServiceListener notificationServiceListener) {
        PlayerPresenceGameStartedEventListener networkCreationService = new PlayerPresenceGameStartedEventListener(presenceService);
        notificationServiceListener.subscribe(networkCreationService);
        return networkCreationService;
    }

    @Bean
    public PlayerSessionRepository playerSessionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerSessionRepository.class);
    }

    @Bean
    public PlayerPresenceController playerPresenceController(ServerPlayerPresenceService playerPresenceServerService) {
        return new PlayerPresenceController(playerPresenceServerService);
    }

    @Bean(destroyMethod = "close")
    public PlayerPresenceCleaner playerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService playerPresenceService) {
        return new JedisPlayerPresenceCleaner(jedisPool, playerPresenceService);
    }

    @Bean
    public PlayerSessionController playerSessionController(
            ResourceLocationService resourceLocationService,
            PlayerSessionRepository playerSessionRepository,
            ServerPlayerPresenceService playerStateManager,
            SystemNotificationService notificationService) {
        return new PlayerSessionController(resourceLocationService, playerSessionRepository, playerStateManager, notificationService);
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
