package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public class RedisGameTableQueue<State extends GameState> implements GameTableQueue<State> {

    final private RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public RedisGameTableQueue(final RedisTemplate<byte[], Long> redisTemplate) {
        this.redisTemplate = checkNotNull(redisTemplate);
    }

    @Override
    public Long poll(final GameSpecification gameSpecification) {
        byte[] key = gameSpecification.getName().toByteArray();
        // Step 1. Fetching associated Set
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        // Step 2. Fetching available table
        return boundSetOperations.pop();
    }

    @Override
    public void add(GameTable<State> gameTable) {
        if (gameTable == null)
            throw new IllegalArgumentException("Game table can't be null");
        if (gameTable.getSpecification() == null)
            throw new IllegalArgumentException("Game specification can't be null");
        // Step 1. Adding table id as available to specification
        byte[] key = gameTable.getSpecification().getName().toByteArray();
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        boundSetOperations.add(gameTable.getTableId());
    }

}
