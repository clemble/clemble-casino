package com.gogomaya.server.player.state;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerState;

public class StringRedisPlayerStateManager implements PlayerStateManager {

    final private Logger LOGGER = LoggerFactory.getLogger(StringRedisPlayerStateManager.class);

    final private long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(20);

    final private String ZERO_SESSION = String.valueOf(SessionAware.DEFAULT_SESSION);

    final private StringRedisTemplate redisTemplate;
    final private RedisSerializer<String> stringRedisSerializer;
    final private RedisMessageListenerContainer listenerContainer;

    public StringRedisPlayerStateManager(StringRedisTemplate redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.stringRedisSerializer = redisTemplate.getStringSerializer();
        this.listenerContainer = listenerContainer;
    }

    @Override
    public Long getActiveSession(long playerId) {
        String session = redisTemplate.boundValueOps(String.valueOf(playerId)).get();
        return session != null ? Long.valueOf(session) : null;
    }

    @Override
    public boolean isAvailable(final long player) {
        // Step 1. Fetch active session
        Long activePlayerSession = getActiveSession(player);
        // Step 2. Only if player has session 0, it is available
        return activePlayerSession != null && activePlayerSession == SessionAware.DEFAULT_SESSION;
    }

    @Override
    public boolean areAvailable(final Collection<Long> players) {
        for (Long player : players) {
            if (!isAvailable(player))
                return false;
        }
        return true;
    }

    @Override
    public boolean markBusy(final long playerId, final long sessionId) {
        if (!isAvailable(playerId))
            throw GogomayaException.fromError(GogomayaError.PlayerSessionTimeout);
        return markBusy(Collections.singleton(playerId), sessionId);
    }

    @Override
    public boolean markBusy(final Collection<Long> players, final long sessionId) {
        // Step 1. Performing atomic update of the state
        boolean updated = redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // Step 1. Checking all players are available
                for (Long player : players) {
                    operations.watch(String.valueOf(player));
                    if (!isAvailable(player)) {
                        return false;
                    }
                }
                // Step 2. Performing atomic operation
                operations.multi();
                for (Long player : players) {
                    operations.boundValueOps(String.valueOf(player)).set(String.valueOf(sessionId));
                }
                // Step 3. If operation failed discard it
                try {
                    return operations.exec() != null;
                } catch (Throwable throwable) {
                    return true;
                }
            }
        });
        // Step 2. If atomic update success, then move on
        if (updated) {
            notifyStateChange(players, PlayerState.busy);
        }
        // Step 3. Returning result of operation
        return updated;
    }

    @Override
    public void markAvailable(final long playerId) {
        // Step 1. Specifying null state as identifier that player is active, and available
        redisTemplate.boundValueOps(String.valueOf(playerId)).set(ZERO_SESSION);
        ;
        // Step 2. Sending notification, for player state update
        notifyStateChange(playerId, PlayerState.available);
    }

    @Override
    public void subscribe(final long playerId, final PlayerStateListener messageListener) {
        subscribe(Collections.singleton(playerId), messageListener);
    }

    @Override
    public void subscribe(Collection<Long> players, PlayerStateListener messageListener) {
        // Step 1. Add message listener
        listenerContainer.addMessageListener(new PlayerStateListenerWrapper(redisTemplate.getStringSerializer(), messageListener), toTopics(players));
        // Step 2. Checking if listener container is alive, and starting it if needed
        if (!listenerContainer.isActive() || !listenerContainer.isRunning())
            listenerContainer.start();
    }

    @Override
    public void unsubscribe(final long player, final PlayerStateListener messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<Long> players, PlayerStateListener playerStateListener) {
        listenerContainer.removeMessageListener(new PlayerStateListenerWrapper(stringRedisSerializer, playerStateListener), toTopics(players));
    }

    private Collection<Topic> toTopics(Collection<Long> players) {
        Collection<Topic> playerTopics = new ArrayList<Topic>(players.size());
        for (Long player : players)
            playerTopics.add(new ChannelTopic(String.valueOf(player)));
        return playerTopics;
    }

    private void notifyStateChange(final Collection<Long> players, final PlayerState state) {
        for (Long player : players)
            notifyStateChange(player, state);
    }

    private void notifyStateChange(final long playerId, final PlayerState state) {
        Long numUpdatedClients = redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // Step 1. Generating channel byte array from player identifier
                byte[] channel = redisTemplate.getStringSerializer().serialize(String.valueOf(playerId));
                byte[] message = redisTemplate.getStringSerializer().serialize(state.name());
                // Step 2. Performing actual publish
                return connection.publish(channel, message);
            }
        });

        LOGGER.debug("Notified of change in {} state {} listeners", playerId, numUpdatedClients);
    }

    @Override
    public void markLeft(long player) {
        redisTemplate.delete(String.valueOf(player));
        notifyStateChange(player, PlayerState.left);
    }

    @Override
    public Date refresh(long player) {
        Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        redisTemplate.boundValueOps(String.valueOf(player)).expireAt(newExpirationTime);
        return newExpirationTime;
    }

    @Override
    public Date markAlive(long player) {
        Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(String.valueOf(player));
        valueOperations.set(ZERO_SESSION);
        valueOperations.expireAt(newExpirationTime);
        return newExpirationTime;
    }
}
