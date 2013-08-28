package com.gogomaya.server.spring.common;

import java.nio.ByteBuffer;

import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RedisServiceInfo;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Configuration
@Import(value = { RedisSpringConfiguration.Cloud.class, RedisSpringConfiguration.DefaultAndTest.class })
public class RedisSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory;

    public static class LongRedisSerializer implements RedisSerializer<Long> {

        final static byte[] ZERO = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        @Override
        public byte[] serialize(Long t) throws SerializationException {
            if (t == null) {
                return ZERO;
            } else {
                ByteBuffer byteBuffer = ByteBuffer.allocate(8);
                byteBuffer.putLong(t.longValue());
                return byteBuffer.array();
            }
        }

        @Override
        public Long deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null)
                return null;
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getLong();
        }
    }

    @Bean
    @Singleton
    public StringRedisTemplate playerQueueTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * @Bean
     * @Singleton
     *            public RedisTemplate<String, Long> playerQueueTemplate() {
     *            RedisTemplate<String, Long> redisTemplate = new RedisTemplate<String, Long>();
     *            redisTemplate.setConnectionFactory(redisConnectionFactory);
     *            return redisTemplate;
     *            }
     */

    @Configuration
    @Profile(value = CLOUD)
    public static class Cloud {

        @Autowired
        CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
        public RedisConnectionFactory redisConnectionFactory() {
            try {
                RedisServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("gogomaya-redis", RedisServiceInfo.class);
                RedisServiceCreator serviceCreator = new RedisServiceCreator();
                RedisConnectionFactory connectionFactory = serviceCreator.createService(serviceInfo);
                if (connectionFactory == null)
                    throw new NullPointerException("Redis Connection factory can't be created");
                return connectionFactory;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public RedisConnectionFactory redisConnectionFactory() {
            // return new JedisConnectionFactory();
            // return new JredisConnectionFactory();
            return new LettuceConnectionFactory();
        }

    }

}
