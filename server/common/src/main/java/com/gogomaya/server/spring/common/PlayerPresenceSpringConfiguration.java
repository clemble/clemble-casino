package com.gogomaya.server.spring.common;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.gogomaya.player.PlayerPresence;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;
import com.gogomaya.server.player.presence.StringRedisPlayerStateManager;

@Configuration
@Import(RabbitSpringConfiguration.class)
public class PlayerPresenceSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public PlayerNotificationService<PlayerPresence> playerPresenceNotificationService;

    @Bean
    public PlayerPresenceServerService playerStateManager() {
        return new StringRedisPlayerStateManager(new StringRedisTemplate(redisConnectionFactory), playerStateListenerContainer(), playerPresenceNotificationService);
    }

    @Bean
    public RedisMessageListenerContainer playerStateListenerContainer() {
        final RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        return listenerContainer;
    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory();
        }

    }

}
