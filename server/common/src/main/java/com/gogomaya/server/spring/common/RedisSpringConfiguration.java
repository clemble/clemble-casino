package com.gogomaya.server.spring.common;

import java.nio.ByteBuffer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

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
@Import(value = { RedisSpringConfiguration.DefaultAndTest.class })
public class RedisSpringConfiguration implements SpringConfiguration {

    @Inject
    @Named("redisConnectionFactory")
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
