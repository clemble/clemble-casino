package com.clemble.casino.server.spring.common;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceListenerService;
import com.clemble.casino.server.player.presence.JedisPlayerPresenceListenerService;
import com.clemble.casino.server.player.presence.JedisPlayerPresenceServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import redis.clients.jedis.JedisPool;

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
    public JedisPlayerPresenceServerService playerPresenceServerService(JedisPool jedisPool) {
        return new JedisPlayerPresenceServerService(jedisPool, playerPresenceNotificationService);
    }

    @Bean
    @Autowired
    public PlayerPresenceListenerService presenceListenerService(JedisPool jedisPool) {
        JedisPlayerPresenceListenerService listenerService = new JedisPlayerPresenceListenerService(jedisPool);
        return listenerService;
    }

    @Bean
    @Autowired
    public RedisMessageListenerContainer playerStateListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        final RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        return listenerContainer;
    }

    @Bean
    public RedisConnectionFactory letucceRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool("127.0.0.1");
    }

}
