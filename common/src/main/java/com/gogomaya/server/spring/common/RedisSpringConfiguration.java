package com.gogomaya.server.spring.common;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.keyvalue.CloudRedisConnectionFactoryBean;
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
@Import(value = { RedisSpringConfiguration.RedisCloudFoundryConfigurations.class, RedisSpringConfiguration.RedisDefaultConfigurations.class })
public class RedisSpringConfiguration {

    @Inject
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<byte[], Long> tableQueueTemplate() {
        RedisTemplate<byte[], Long> redisTemplate = new RedisTemplate<byte[], Long>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<Long, Long> playerQueueTemplate() {
        RedisTemplate<Long, Long> redisTemplate = new RedisTemplate<Long, Long>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Configuration
    @Profile(value = "cloud")
    static class RedisCloudFoundryConfigurations {

        @Inject
        CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
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
    static class RedisDefaultConfigurations {

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new JedisConnectionFactory();
        }

    }

}
