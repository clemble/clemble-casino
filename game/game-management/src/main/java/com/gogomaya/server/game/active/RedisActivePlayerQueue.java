package com.gogomaya.server.game.active;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisActivePlayerQueue implements ActivePlayerQueue {

    final private RedisTemplate<Long, Long> redisTemplate;

    public RedisActivePlayerQueue(RedisTemplate<Long, Long> redisTemplate) {
        this.redisTemplate = checkNotNull(redisTemplate);
    }

    @Override
    public Long isActive(long playerId) {
        return redisTemplate.boundValueOps(playerId).get();
    }

    @Override
    public boolean markActive(long playerId, long sessionId) {
        return redisTemplate.boundValueOps(playerId).setIfAbsent(sessionId);
    }

    @Override
    public void markInActive(long playerId) {
        redisTemplate.delete(playerId);
    }

    @Override
    public void markInActive(List<Long> playerIds) {
        redisTemplate.delete(playerIds);
    }

}
