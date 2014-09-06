package com.clemble.casino.server.key;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 8/2/14.
 */
public class RedisKeyFactory implements KeyFactory {

    final private String KEY;
    final private String PREFIX;

    final private JedisPool jedisPool;

    public RedisKeyFactory(String key, String prefix, JedisPool jedisPool) {
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
