package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.specification.GameSpecification;

public class RedisGameSessionQueue implements PendingSessionQueue {

    final private RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public RedisGameSessionQueue(final RedisTemplate<byte[], Long> redisTemplate) {
        this.redisTemplate = checkNotNull(redisTemplate);
    }

    @Override
    public Long poll(final GameSpecification specification) {
        byte[] key = specification.getName().toByteArray();
        // Step 1. Fetching associated Set
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        // Step 2. Fetching available table
        return boundSetOperations.pop();
    }

    @Override
    public void add(long tableId, GameSpecification specification) {
        if (specification == null)
            throw new IllegalArgumentException("Game table can't be null");
        // Step 1. Adding table id as available to specification
        byte[] key = specification.getName().toByteArray();
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        boundSetOperations.add(tableId);
    }

    @Override
    public void invalidate(long session, GameSpecification specification) {
        if (specification == null)
            throw new IllegalArgumentException("Game table can't be null");
        // Step 1. Adding table id as available to specification
        byte[] key = specification.getName().toByteArray();
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        boundSetOperations.remove(session);
    }

}
