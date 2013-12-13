package com.clemble.casino.server.spring.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import redis.clients.jedis.JedisPool;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.JedisPlayerPresenceServerService;
import com.clemble.casino.server.player.presence.JedisSystemNoficiationServiceListener;
import com.clemble.casino.server.player.presence.JedisSystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import({ RabbitSpringConfiguration.class })
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Bean
    @Autowired
    public SystemNotificationService systemNotificationService(JedisPool jedisPool, ObjectMapper objectMapper) {
        return new JedisSystemNotificationService(jedisPool, objectMapper);
    }

    @Bean
    @Autowired
    public JedisPlayerPresenceServerService playerPresenceServerService(JedisPool jedisPool, PlayerNotificationService<PlayerPresence> playerPresenceNotificationService, SystemNotificationService systemNotificationService) {
        return new JedisPlayerPresenceServerService(jedisPool, playerPresenceNotificationService, systemNotificationService);
    }

    @Bean
    @Autowired
    public SystemNotificationServiceListener presenceListenerService(JedisPool jedisPool, ObjectMapper mapper) {
        return new JedisSystemNoficiationServiceListener(jedisPool, mapper);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool("127.0.0.1");
    }

}
