package com.clemble.casino.server.spring.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceListenerService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.player.presence.StringRedisPlayerPresenceListenerService;
import com.clemble.casino.server.player.presence.StringRedisPlayerStateManager;

@Configuration
@Import({ RabbitSpringConfiguration.class })
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Autowired
    public PlayerNotificationService<PlayerPresence> playerPresenceNotificationService;

    @Bean
    @Autowired
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @Autowired
    public PlayerPresenceServerService playerStateManager(StringRedisTemplate redisTemplate, RedisMessageListenerContainer playerStateListenerContainer) {
        return new StringRedisPlayerStateManager(redisTemplate, playerStateListenerContainer, playerPresenceNotificationService);
    }

    @Bean
    @Autowired
    public PlayerPresenceListenerService presenceListenerService(StringRedisTemplate redisTemplate, RedisMessageListenerContainer listenerContainer) {
        return new StringRedisPlayerPresenceListenerService(redisTemplate, listenerContainer);
    }

    @Bean
    @Autowired
    public RedisMessageListenerContainer playerStateListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        final RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        return listenerContainer;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

}
