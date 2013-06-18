package com.gogomaya.server.game.active;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import com.gogomaya.server.game.SessionAware;

public class RedisPlayerStateManager implements PlayerStateManager {

    final private RedisTemplate<Long, Long> redisTemplate;

    public RedisPlayerStateManager(RedisTemplate<Long, Long> redisTemplate) {
        this.redisTemplate = checkNotNull(redisTemplate);
    }

    @Override
    public Long isActive(long playerId) {
        return redisTemplate.boundValueOps(playerId).get();
    }

    @Override
    public boolean markBusy(final long playerId, final long sessionId) {
        return redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                    operations.watch(Long.valueOf(playerId));
                    BoundValueOperations valueOperations = operations.boundValueOps(playerId);
                    if (valueOperations.get() == null || SessionAware.DEFAULT_SESSION == ((Long) valueOperations.get()).longValue()) {
                        operations.multi();
                        valueOperations.set(sessionId);
                        if (operations.exec() != null) {
                            return true;
                        }
                    }
                    return false;
            }
        });
    }

    @Override
    public boolean markAvailable(final long playerId) {
        redisTemplate.boundValueOps(playerId).set(SessionAware.DEFAULT_SESSION, 30, TimeUnit.MINUTES);
        return true;
    }
}
