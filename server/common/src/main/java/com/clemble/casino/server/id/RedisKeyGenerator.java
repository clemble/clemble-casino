package com.clemble.casino.server.id;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 8/2/14.
 */
public class RedisKeyGenerator implements KeyGenerator {

    final private String KEY;
    final private String PREFIX;

    final private JedisPool jedisPool;

    public RedisKeyGenerator(String key, String prefix, JedisPool jedisPool) {
        this.KEY = key;
        this.PREFIX = prefix;
        this.jedisPool = jedisPool;
    }

    @Override
    public String generate() {
        Jedis jedis = jedisPool.getResource();
        try {
            return PREFIX + jedis.incr(KEY);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
