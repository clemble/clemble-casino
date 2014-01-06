package com.clemble.casino.server.spring.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import redis.clients.jedis.JedisPool;

import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.JedisPlayerPresenceCleaner;
import com.clemble.casino.server.player.presence.JedisServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.JedisSystemNoficiationServiceListener;
import com.clemble.casino.server.player.presence.JedisSystemNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceCleaner;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import({ RabbitSpringConfiguration.class })
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Bean
    public SystemNotificationService systemNotificationService(JedisPool jedisPool, ObjectMapper objectMapper) {
        return new JedisSystemNotificationService(jedisPool, objectMapper);
    }

    @Bean
    public JedisServerPlayerPresenceService playerPresenceServerService(JedisPool jedisPool, PlayerNotificationService playerPresenceNotificationService, SystemNotificationService systemNotificationService) {
        return new JedisServerPlayerPresenceService(jedisPool, playerPresenceNotificationService, systemNotificationService);
    }

    @Bean(destroyMethod = "close")
    public SystemNotificationServiceListener presenceListenerService(JedisPool jedisPool, ObjectMapper mapper) {
        return new JedisSystemNoficiationServiceListener(jedisPool, mapper);
    }

    @Bean(destroyMethod = "close")
    public PlayerPresenceCleaner playerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService presenceService) {
        return new JedisPlayerPresenceCleaner(jedisPool, presenceService);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool("127.0.0.1");
    }

}
