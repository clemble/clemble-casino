package com.gogomaya.server.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTest extends AbstractCommonTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(redisTemplate);
        redisTemplate.opsForList().leftPush("key", "value");
        String value = redisTemplate.opsForList().leftPop("key");
        Assert.assertEquals(value, "value");
        Assert.assertNull(redisTemplate.opsForList().leftPop("key"));
    }

}
