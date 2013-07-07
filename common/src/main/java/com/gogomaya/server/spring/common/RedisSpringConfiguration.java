package com.gogomaya.server.spring.common;

import java.util.Collection;

import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.keyvalue.CloudRedisConnectionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Configuration
@Import(value = { RedisSpringConfiguration.Cloud.class, RedisSpringConfiguration.DefaultAndTest.class })
public class RedisSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory;

    @Bean @Singleton
    public RedisTemplate<byte[], Long> tableQueueTemplate() {
        RedisTemplate<byte[], Long> redisTemplate = new RedisTemplate<byte[], Long>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean @Singleton
    public RedisTemplate<Long, Long> playerQueueTemplate() {
        RedisTemplate<Long, Long> redisTemplate = new RedisTemplate<Long, Long>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Configuration
    @Profile(value = "cloud")
    public static class Cloud {

        @Autowired
        CloudEnvironment cloudEnvironment;

        @Bean @Singleton
        public RedisConnectionFactory redisConnectionFactory() {
            CloudRedisConnectionFactoryBean cloudRedisFactory = new CloudRedisConnectionFactoryBean(cloudEnvironment);
            try {
                Collection<ServiceNameTuple<RedisConnectionFactory>> redisConnectionFactories = cloudRedisFactory.createInstances();
                redisConnectionFactories = Collections2.filter(redisConnectionFactories, new Predicate<ServiceNameTuple<RedisConnectionFactory>>() {
                    public boolean apply(ServiceNameTuple<RedisConnectionFactory> input) {
                        return input.name.equalsIgnoreCase("gogomaya-redis");
                    }
                });
                assert redisConnectionFactories.size() == 1 : "Returned illegal ConnectionFactory";
                return redisConnectionFactories.iterator().next().service;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Configuration
    @Profile(value = { "default", "test" })
    public static class DefaultAndTest {

        @Bean @Singleton
        public RedisConnectionFactory redisConnectionFactory() {
            return new JedisConnectionFactory();
        }

    }

}
