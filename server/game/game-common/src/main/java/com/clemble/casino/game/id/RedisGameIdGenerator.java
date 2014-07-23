package com.clemble.casino.game.id;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 7/23/14.
 */
public class RedisGameIdGenerator implements GameIdGenerator {

    final private String KEY = "GAME_COUNTER";
    final private JedisPool jedisPool;

    public RedisGameIdGenerator(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public String newId() {
        Jedis jedis = jedisPool.getResource();
        try {
            return "G" + jedis.incr(KEY);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}
