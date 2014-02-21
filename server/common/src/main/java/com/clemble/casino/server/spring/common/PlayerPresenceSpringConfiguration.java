package com.clemble.casino.server.spring.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import redis.clients.jedis.JedisPool;

import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.JedisPlayerPresenceCleaner;
import com.clemble.casino.server.player.presence.JedisServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.PlayerPresenceCleaner;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.SystemNotificationService;

@Configuration
@Import({ RabbitSpringConfiguration.class, RedisSpringConfiguration.class })
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Bean
    public JedisServerPlayerPresenceService playerPresenceServerService(JedisPool jedisPool, PlayerNotificationService playerPresenceNotificationService, SystemNotificationService systemNotificationService) {
        return new JedisServerPlayerPresenceService(jedisPool, playerPresenceNotificationService, systemNotificationService);
    }

    @Bean(destroyMethod = "close")
    public PlayerPresenceCleaner playerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService presenceService) {
        return new JedisPlayerPresenceCleaner(jedisPool, presenceService);
    }

}
