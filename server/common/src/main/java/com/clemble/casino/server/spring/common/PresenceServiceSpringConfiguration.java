package com.clemble.casino.server.spring.common;

import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.JedisServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 7/4/14.
 */
// TODO this should be concern of single module
@Configuration
@Import(RedisSpringConfiguration.class)
public class PresenceServiceSpringConfiguration implements SpringConfiguration {

    @Bean
    public ServerPlayerPresenceService playerPresenceServerService(JedisPool jedisPool, PlayerNotificationService playerPresenceNotificationService, SystemNotificationService systemNotificationService) {
        return new JedisServerPlayerPresenceService(jedisPool, playerPresenceNotificationService, systemNotificationService);
    }

}
