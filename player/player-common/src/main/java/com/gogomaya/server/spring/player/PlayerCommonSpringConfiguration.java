package com.gogomaya.server.spring.player;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.state.RedisPlayerStateManager;
import com.gogomaya.server.spring.common.RedisSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import({ RedisSpringConfiguration.class })
public class PlayerCommonSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerQueueTemplate")
    public RedisTemplate<Long, Long> playerQueueTemplate;

    @Bean
    @Singleton
    public RedisMessageListenerContainer playerStateListenerContainer() {
        final RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(playerQueueTemplate.getConnectionFactory());
        return listenerContainer;
    }

    @Bean
    @Singleton
    public PlayerStateManager playerStateManager() {
        return new RedisPlayerStateManager(playerQueueTemplate, playerStateListenerContainer());
    }
}
