package com.gogomaya.server.spring.player;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.state.StringRedisPlayerStateManager;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
public class PlayerRedisSpringConfiguration  implements SpringConfiguration {

    @Autowired
    @Qualifier("playerQueueTemplate")
    public StringRedisTemplate playerQueueTemplate;

    @Bean
    @Singleton
    public PlayerStateManager playerStateManager() {
        return new StringRedisPlayerStateManager(playerQueueTemplate, playerStateListenerContainer());
    }

    /**
     * @Autowired
     * @Qualifier("playerQueueTemplate")
     *                                   public RedisTemplate<String, Long> playerQueueTemplate;
     * @Bean
     * @Singleton
     *            public PlayerStateManager playerStateManager() {
     *            return new RedisPlayerStateManager(playerQueueTemplate, playerStateListenerContainer());
     *            }
     */

    @Bean
    @Singleton
    public RedisMessageListenerContainer playerStateListenerContainer() {
        final RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(playerQueueTemplate.getConnectionFactory());
        return listenerContainer;
    }
}
