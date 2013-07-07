package com.gogomaya.server.player.state;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerState;

public class RedisPlayerStateManager implements PlayerStateManager {

    final private Logger LOGGER = LoggerFactory.getLogger(RedisPlayerStateManager.class);

    final private RedisTemplate<Long, Long> redisTemplate;
    final private RedisMessageListenerContainer listenerContainer;

    public RedisPlayerStateManager(RedisTemplate<Long, Long> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.listenerContainer = listenerContainer;
    }

    @Override
    public Long getActiveSession(long playerId) {
        return redisTemplate.boundValueOps(playerId).get();
    }

    @Override
    public boolean isAvailable(final long player) {
        Long activePlayerSession = getActiveSession(player);
        return activePlayerSession != null && activePlayerSession == SessionAware.DEFAULT_SESSION;
    }

    @Override
    public boolean areAvailable(final Collection<Long> players) {
        return redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (Long player : players) {
                    if (!isAvailable(player))
                        return false;
                }
                return true;
            }
        });
    }

    @Override
    public boolean markBusy(final long playerId, final long sessionId) {
        if (!isAvailable(playerId))
            throw GogomayaException.fromError(GogomayaError.PlayerSessionTimeout);
        return markBusy(Collections.singleton(playerId), sessionId).size() == 0;
    }

    @Override
    public Collection<Long> markBusy(final Collection<Long> players, final long sessionId) {
        final Collection<Long> busyPlayers = new ArrayList<Long>();
        // Step 1. Performing atomic update of the state
        boolean updated = redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // Step 1. Checking all players are available
                for (Long player : players)
                    if (!isAvailable(player))
                        return false;
                // Step 2. Performing atomic operation
                operations.watch(players);
                operations.multi();
                for (Long player : players) {
                    BoundValueOperations valueOperations = operations.boundValueOps(player);
                    if (SessionAware.DEFAULT_SESSION == ((Long) valueOperations.get()).longValue()) {
                        valueOperations.set(sessionId);
                    } else {
                        busyPlayers.add(player);
                        break;
                    }
                }
                // Step 3. If operation failed discard it
                if (busyPlayers.size() != 0) {
                    operations.discard();
                    return false;
                } else if (operations.exec() != null) {
                    return true;
                }
                return false;
            }
        });
        // Step 2. If atomic update success, then move on
        if (updated) {
            notifyStateChange(players, PlayerState.busy);
        }
        // Step 3. Returning result of operation
        return busyPlayers;
    }

    @Override
    public void markAvailable(final long playerId) {
        // Step 1. Specifying null state as identifier that player is active, and available
        redisTemplate.boundValueOps(playerId).set(SessionAware.DEFAULT_SESSION, 30, TimeUnit.MINUTES);
        // Step 2. Sending notification, for player state update
        notifyStateChange(playerId, PlayerState.available);
    }

    @Override
    public void subscribe(final long playerId, final MessageListener messageListener) {
        subscribe(Collections.singleton(playerId), messageListener);
    }

    @Override
    public void subscribe(Collection<Long> players, MessageListener messageListener) {
        // Step 1. Add message listener
        listenerContainer.addMessageListener(messageListener, toTopics(players));
        // Step 2. Checking if listener container is alive, and starting it if needed
        if (!listenerContainer.isActive() || !listenerContainer.isRunning())
            listenerContainer.start();
    }

    @Override
    public void unsubscribe(final long player, final MessageListener messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<Long> players, MessageListener messageListener) {
        listenerContainer.removeMessageListener(messageListener, toTopics(players));
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
        long numUpdatedClients = redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // Step 1. Generating channel byte array from player identifier
                byte[] channel = redisTemplate.getStringSerializer().serialize(String.valueOf(playerId));
                // Step 2. Performing actual publish
                return connection.publish(channel, new byte[] { (byte) state.ordinal() });
            }
        });

        LOGGER.debug("Notified of change in {} state {} listeners", playerId, numUpdatedClients);
    }

}
