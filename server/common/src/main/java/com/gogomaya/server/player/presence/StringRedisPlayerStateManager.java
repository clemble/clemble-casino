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
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;
import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.Presence;
import com.gogomaya.server.player.notification.PlayerNotificationListener;
import com.gogomaya.server.player.notification.PlayerNotificationService;

public class StringRedisPlayerStateManager implements PlayerPresenceServerService {

    final private Logger LOGGER = LoggerFactory.getLogger(StringRedisPlayerStateManager.class);

    final private long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(20);
    final private String ZERO_SESSION = ":";

    final private StringRedisTemplate redisTemplate;
    final private RedisSerializer<String> stringRedisSerializer;
    final private RedisMessageListenerContainer listenerContainer;
    final private PlayerNotificationService<PlayerPresence> presenceNotification;

    public StringRedisPlayerStateManager(
            StringRedisTemplate redisTemplate,
            RedisMessageListenerContainer listenerContainer,
            PlayerNotificationService<PlayerPresence> presenceNotification) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.stringRedisSerializer = redisTemplate.getStringSerializer();
        this.listenerContainer = checkNotNull(listenerContainer);
        this.presenceNotification = checkNotNull(presenceNotification);
    }

    public GameSessionKey getActiveSession(String player) {
        String session = redisTemplate.boundValueOps(String.valueOf(player)).get();
        if (session == null)
            return null;
        if (session.length() == 1)
            return SessionAware.DEFAULT_SESSION;
        String[] splittedSession = session.split(":");
        return splittedSession[0].equals("") ? SessionAware.DEFAULT_SESSION : new GameSessionKey(Game.valueOf(splittedSession[0]), Long.valueOf(splittedSession[1]));
    }

    @Override
    public PlayerPresence getPresence(String player) {
        GameSessionKey session = getActiveSession(player);
        if (session == null) {
            return new PlayerPresence(player, SessionAware.DEFAULT_SESSION, Presence.offline);
        } else if (session.equals(SessionAware.DEFAULT_SESSION)) {
            return new PlayerPresence(player, SessionAware.DEFAULT_SESSION, Presence.online);
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
        GameSessionKey activePlayerSession = getActiveSession(player);
        // Step 2. Only if player has session 0, it is available
        return activePlayerSession != null && activePlayerSession.equals(SessionAware.DEFAULT_SESSION);
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
    public boolean markPlaying(final String player, final GameSessionKey sessionId) {
        if (!isAvailable(player))
            throw GogomayaException.fromError(GogomayaError.PlayerSessionTimeout);
        return markPlaying(Collections.singleton(player), sessionId);
    }

    @Override
    public boolean markPlaying(final Collection<String> players, final GameSessionKey session) {
        // Step 1. Performing atomic update of the state
        boolean updated = redisTemplate.execute(new SessionCallback<Boolean>() {

            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // Step 1. Checking all players are available
                for (String player : players) {
                    operations.watch(String.valueOf(player));
                    if (!isAvailable(player)) {
                        return false;
                    }
                }
                // Step 2. Performing atomic operation
                operations.multi();
                String sessionKeyPresentation = session.getGame().name() + ":" + session.getSession();
                for (String player : players) {
                    operations.boundValueOps(String.valueOf(player)).set(sessionKeyPresentation);
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
            for(String player: players) {
                notifyStateChange(PlayerPresence.playing(player, session));
            }
        }
        // Step 3. Returning result of operation
        return updated;
    }

    @Override
    public void markOffline(String player) {
        // Step 1. Removing associated key from the list
        redisTemplate.delete(String.valueOf(player));
        // Step 2. Notifying listeners of state change
        notifyStateChange(PlayerPresence.offline(player));
    }

    @Override
    public Date markOnline(final String player) {
        // Step 1. Setting new expiration time
        Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(String.valueOf(player));
        valueOperations.set(ZERO_SESSION);
        valueOperations.expireAt(newExpirationTime);
        // Step 2. Sending notification, for player state update
        notifyStateChange(PlayerPresence.online(player));
        return newExpirationTime;
    }

    @Override
    public void subscribe(final String player, final PlayerNotificationListener<Presence> messageListener) {
        subscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void subscribe(Collection<String> players, PlayerNotificationListener<Presence> messageListener) {
        // Step 1. Add message listener
        listenerContainer.addMessageListener(new PresenceListenerWrapper(redisTemplate.getStringSerializer(), messageListener), toTopics(players));
        // Step 2. Checking if listener container is alive, and starting it if needed
        if (!listenerContainer.isActive() || !listenerContainer.isRunning())
            listenerContainer.start();
    }

    @Override
    public void unsubscribe(final String player, final PlayerNotificationListener<Presence> messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<String> players, PlayerNotificationListener<Presence> playerStateListener) {
        listenerContainer.removeMessageListener(new PresenceListenerWrapper(stringRedisSerializer, playerStateListener), toTopics(players));
    }

    private Collection<Topic> toTopics(Collection<String> players) {
        Collection<Topic> playerTopics = new ArrayList<Topic>(players.size());
        for (String player : players)
            playerTopics.add(new ChannelTopic(player));
        return playerTopics;
    }

    private void notifyStateChange(final PlayerPresence newPresence) {
        // Step 1. Notifying through native Redis mechanisms
        Long numUpdatedClients = redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // Step 1. Generating channel byte array from player identifier
                byte[] channel = redisTemplate.getStringSerializer().serialize(String.valueOf(newPresence.getPlayer()));
                byte[] message = redisTemplate.getStringSerializer().serialize(newPresence.getPresence().name());
                // Step 2. Performing actual publish
                return connection.publish(channel, message);
            }
        });
        LOGGER.debug("Notified of change in {} state {} listeners", newPresence.getPlayer(), numUpdatedClients);
        presenceNotification.notify(newPresence.getPlayer(), newPresence);
    }

}
