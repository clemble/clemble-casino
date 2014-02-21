package com.clemble.casino.server.spring.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 21/02/14.
 */
@Configuration
public class RedisSpringConfiguration implements SpringConfiguration {

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool("127.0.0.1");
    }

}
