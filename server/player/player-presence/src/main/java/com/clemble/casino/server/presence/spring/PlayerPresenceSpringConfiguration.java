package com.clemble.casino.server.presence.spring;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.player.presence.*;
import com.clemble.casino.server.presence.JedisPlayerPresenceCleaner;
import com.clemble.casino.server.presence.PlayerPresenceCleaner;
import com.clemble.casino.server.presence.listener.PlayerPresenceGameEndedListener;
import com.clemble.casino.server.presence.listener.PlayerPresenceGameStartedEventListener;
import com.clemble.casino.server.presence.repository.PlayerSessionRepository;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.PresenceServiceSpringConfiguration;
import com.clemble.casino.server.presence.controller.PlayerSessionServiceController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.presence.controller.PlayerPresenceServiceController;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

@Configuration
@Import(value = {CommonSpringConfiguration.class, PresenceServiceSpringConfiguration.class, MongoSpringConfiguration.class})
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

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
    public PlayerSessionRepository playerSessionRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(PlayerSessionRepository.class);
    }

    @Bean
    public PlayerPresenceServiceController playerPresenceController(ServerPlayerPresenceService playerPresenceServerService) {
        return new PlayerPresenceServiceController(playerPresenceServerService);
    }

    @Bean(destroyMethod = "close")
    public PlayerPresenceCleaner playerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService playerPresenceService) {
        return new JedisPlayerPresenceCleaner(jedisPool, playerPresenceService);
    }

    @Bean
    public PlayerSessionServiceController playerSessionController(
            PlayerSessionRepository playerSessionRepository,
            ServerPlayerPresenceService playerStateManager,
            SystemNotificationService notificationService) {
        return new PlayerSessionServiceController(playerSessionRepository, playerStateManager, notificationService);
    }

}
