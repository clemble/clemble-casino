package com.clemble.casino.server.spring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 21/02/14.
 */
@Configuration
public class RedisSpringConfiguration implements SpringConfiguration {

    @Bean
    public JedisPool jedisPool(@Value("${clemble.db.redis.host}") String host) {
        return new JedisPool(host);
    }

}
