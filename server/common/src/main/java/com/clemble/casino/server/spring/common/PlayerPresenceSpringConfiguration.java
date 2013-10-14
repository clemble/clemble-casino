package com.clemble.casino.server.spring.common;

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

import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.player.presence.StringRedisPlayerStateManager;
import com.clemble.casino.player.PlayerPresence;

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
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory();
        }

    }

}
