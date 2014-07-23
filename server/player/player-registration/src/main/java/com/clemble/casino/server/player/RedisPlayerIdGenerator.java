package com.clemble.casino.server.player;

import com.clemble.casino.game.GameSessionKey;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 7/23/14.
 */
public class RedisPlayerIdGenerator implements PlayerIdGenerator {

    final private String KEY = "PLAYER_COUNTER";
    final private JedisPool jedisPool;

    public RedisPlayerIdGenerator(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public String newId() {
        Jedis jedis = jedisPool.getResource();
        try {
            return "U" + jedis.incr(KEY);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}
