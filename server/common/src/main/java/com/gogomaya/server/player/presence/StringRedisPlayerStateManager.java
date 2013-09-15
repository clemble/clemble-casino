package com.gogomaya.server.player.presence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.game.SessionAware;
import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.Presence;
import com.gogomaya.server.player.notification.PlayerNotificationListener;

public class StringRedisPlayerStateManager implements PlayerPresenceServerService {

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

    public Long getActiveSession(long playerId) {
        String session = redisTemplate.boundValueOps(String.valueOf(playerId)).get();
        return session != null ? Long.valueOf(session) : null;
    }

    @Override
    public PlayerPresence getPresence(long player) {
        Long session = getActiveSession(player);
        if (session == null) {
            return new PlayerPresence(player, SessionAware.DEFAULT_SESSION, Presence.offline);
        } else if (session == SessionAware.DEFAULT_SESSION) {
            return new PlayerPresence(player, SessionAware.DEFAULT_SESSION, Presence.online);
        }
        return new PlayerPresence(player, session, Presence.playing);
    }

    @Override
    public List<PlayerPresence> getPresences(Collection<Long> presences) {
        List<PlayerPresence> playerPresences = new ArrayList<>();
        for (Long presence : presences)
            playerPresences.add(getPresence(presence));
        return playerPresences;
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
    public boolean markPlaying(final long playerId, final long sessionId) {
        if (!isAvailable(playerId))
            throw GogomayaException.fromError(GogomayaError.PlayerSessionTimeout);
        return markPlaying(Collections.singleton(playerId), sessionId);
    }

    @Override
    public boolean markPlaying(final Collection<Long> players, final long sessionId) {
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
            notifyStateChange(players, Presence.playing);
        }
        // Step 3. Returning result of operation
        return updated;
    }

    @Override
    public void markOffline(long player) {
        redisTemplate.delete(String.valueOf(player));
        notifyStateChange(player, Presence.offline);
    }

    @Override
    public Date markOnline(final long playerId) {
        // Step 1. Setting new expiration time
        Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(String.valueOf(playerId));
        valueOperations.set(ZERO_SESSION);
        valueOperations.expireAt(newExpirationTime);
        // Step 2. Sending notification, for player state update
        notifyStateChange(playerId, Presence.online);
        return newExpirationTime;
    }

    @Override
    public void subscribe(final long playerId, final PlayerNotificationListener<Presence> messageListener) {
        subscribe(Collections.singleton(playerId), messageListener);
    }

    @Override
    public void subscribe(Collection<Long> players, PlayerNotificationListener<Presence> messageListener) {
        // Step 1. Add message listener
        listenerContainer.addMessageListener(new PresenceListenerWrapper(redisTemplate.getStringSerializer(), messageListener), toTopics(players));
        // Step 2. Checking if listener container is alive, and starting it if needed
        if (!listenerContainer.isActive() || !listenerContainer.isRunning())
            listenerContainer.start();
    }

    @Override
    public void unsubscribe(final long player, final PlayerNotificationListener<Presence> messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<Long> players, PlayerNotificationListener<Presence> playerStateListener) {
        listenerContainer.removeMessageListener(new PresenceListenerWrapper(stringRedisSerializer, playerStateListener), toTopics(players));
    }

    private Collection<Topic> toTopics(Collection<Long> players) {
        Collection<Topic> playerTopics = new ArrayList<Topic>(players.size());
        for (Long player : players)
            playerTopics.add(new ChannelTopic(String.valueOf(player)));
        return playerTopics;
    }

    private void notifyStateChange(final Collection<Long> players, final Presence state) {
        for (Long player : players)
            notifyStateChange(player, state);
    }

    private void notifyStateChange(final long playerId, final Presence state) {
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

}
