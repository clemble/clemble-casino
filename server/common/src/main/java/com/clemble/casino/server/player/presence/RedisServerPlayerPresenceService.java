package com.clemble.casino.server.player.presence;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.event.PlayerPresenceChangedEvent;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.ServerNotificationService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class RedisServerPlayerPresenceService implements ServerPlayerPresenceService {

    final private Logger LOG = LoggerFactory.getLogger(RedisServerPlayerPresenceService.class);

    final private long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(20);

    final private StringRedisTemplate redisTemplate;
    final private ServerNotificationService presenceNotification;

    public RedisServerPlayerPresenceService(StringRedisTemplate redisTemplate, RedisMessageListenerContainer listenerContainer,
            ServerNotificationService presenceNotification) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.presenceNotification = checkNotNull(presenceNotification);
    }

    public String getActiveSession(String player) {
        String session = redisTemplate.boundValueOps(player).get();
        if (session == null)
            return null;
        if (session.length() == 1)
            return GameSessionAware.DEFAULT_SESSION;
        return session;
    }

    @Override
    public PlayerPresence getPresence(String player) {
        String session = getActiveSession(player);
        if (session == null) {
            return new PlayerPresence(player, GameSessionAware.DEFAULT_SESSION, Presence.offline);
        } else if (session.equals(GameSessionAware.DEFAULT_SESSION)) {
            return new PlayerPresence(player, GameSessionAware.DEFAULT_SESSION, Presence.online);
        }
        return new PlayerPresence(player, session, Presence.playing);
    }

    @Override
    public List<PlayerPresence> getPresences(Collection<String> presences) {
        List<PlayerPresence> playerPresences = new ArrayList<>();
        for (String presence : presences)
            playerPresences.add(getPresence(presence));
        return playerPresences;
    }

    @Override
    public boolean isAvailable(final String player) {
        // Step 1. Fetch active session
        String activePlayerSession = getActiveSession(player);
        // Step 2. Only if player has session 0, it is available
        return activePlayerSession != null && activePlayerSession.equals(GameSessionAware.DEFAULT_SESSION);
    }

    @Override
    public boolean areAvailable(final Collection<String> players) {
        for (String player : players) {
            if (!isAvailable(player))
                return false;
        }
        return true;
    }

    @Override
    public boolean markPlaying(final String player, final String sessionId) {
        if (!isAvailable(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionTimeout, player, sessionId);
        return markPlaying(Collections.singleton(player), sessionId);
    }

    @Override
    public boolean markPlaying(final Collection<String> players, final String sessionKey) {
        // Step 1. Performing atomic update of the state
        boolean updated = redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // Step 1. Checking all players are available
                for (String player : players) {
                    operations.watch(player);
                    if (!isAvailable(player)) {
                        return false;
                    }
                }
                // Step 2. Performing atomic operation
                operations.multi();
                String sessionKeyPresentation = sessionKey;
                for (String player : players) {
                    operations.boundValueOps(player).set(sessionKeyPresentation);
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
            for (String player : players) {
                notifyStateChange(PlayerPresence.playing(player, sessionKey));
            }
        }
        // Step 3. Returning result of operation
        return updated;
    }

    @Override
    public void markOffline(String player) {
        // Step 1. Removing associated key from the list
        redisTemplate.delete(player);
        // Step 2. Notifying listeners of state change
        notifyStateChange(PlayerPresence.offline(player));
    }

    @Override
    public DateTime markAvailable(String player) {
        return markOnline(player);
    }

    @Override
    public DateTime markOnline(final String player) {
        // Step 1. Setting new expiration time
        DateTime newExpirationTime = new DateTime(System.currentTimeMillis() + EXPIRATION_TIME);
        BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(player);
        valueOperations.set(GameSessionAware.DEFAULT_SESSION);
        valueOperations.expireAt(newExpirationTime.toDate());
        // Step 2. Sending notification, for player state update
        notifyStateChange(PlayerPresence.online(player));
        return newExpirationTime;
    }

    private void notifyStateChange(final PlayerPresence newPresence) {
        // Step 1. Notifying through native Redis mechanisms
        Long numUpdatedClients = redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // Step 1. Generating channel byte array from player identifier
                byte[] channel = redisTemplate.getStringSerializer().serialize(newPresence.getPlayer());
                byte[] message = redisTemplate.getStringSerializer().serialize(newPresence.getPresence().name());
                // Step 2. Performing actual publish
                return connection.publish(channel, message);
            }
        });
        LOG.debug("{} update to {}, notified {} listeners", newPresence.getPlayer(), newPresence.getPresence(), numUpdatedClients);
        presenceNotification.send(newPresence.getPlayer(), new PlayerPresenceChangedEvent(newPresence));
    }

}
