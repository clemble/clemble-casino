package com.clemble.casino.server.player.presence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.event.PlayerPresenceChangedEvent;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.player.SystemPlayerEnteredEvent;
import com.clemble.casino.server.event.player.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class JedisServerPlayerPresenceService implements ServerPlayerPresenceService {

    final private Logger LOG = LoggerFactory.getLogger(JedisServerPlayerPresenceService.class);

    final private int EXPIRATION_TIME = (int) TimeUnit.MINUTES.toMillis(20);

    final private String UPDATE_SCRIPT;
    final private JedisPool jedisPool;
    final private SystemNotificationService systemNotificationService;
    final private PlayerNotificationService presenceNotification;

    public JedisServerPlayerPresenceService(JedisPool jedisPool, PlayerNotificationService presenceNotification,
            SystemNotificationService systemNotificationService) {
        this.jedisPool = checkNotNull(jedisPool);
        this.presenceNotification = checkNotNull(presenceNotification);
        this.systemNotificationService = checkNotNull(systemNotificationService);
        Jedis jedis = jedisPool.getResource();
        try {
            this.UPDATE_SCRIPT = jedis.scriptLoad(" for i = 1,table.getn(KEYS) do " + " local state = redis.call('get', KEYS[i])" + " if (state ~= '') then "
                    + " redis.log(redis.LOG_DEBUG, ARGV[1] .. ' > ' .. KEYS[i] .. ' is in ' .. state); " + " return 'false'" + " end " + " end "
                    + " redis.log(redis.LOG_DEBUG, 'Starting ' .. ARGV[1]) " + " for i = 1,table.getn(KEYS) do " + " redis.call('set', KEYS[i], ARGV[1])"
                    + " end " + " return 'true'");
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public String getActiveSession(String player) {
        Jedis jedis = jedisPool.getResource();
        LOG.trace("Available connection + {}", jedis);
        try {
            return jedis.get(player);
        } finally {
            LOG.trace("Available connection - {}", jedis);
            jedisPool.returnResource(jedis);
        }
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
    public boolean markPlaying(final String player, final String sessionKey) {
        if (!isAvailable(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionTimeout, player, sessionKey);
        return markPlaying(Collections.singleton(player), sessionKey);
    }

    @Override
    public boolean markPlaying(final Collection<String> players, final String sessionKey) {
        Jedis jedis = jedisPool.getResource();
        boolean updated = false;
        try {
            updated = Boolean.valueOf(String.valueOf(jedis.evalsha(UPDATE_SCRIPT, new ArrayList<>(players), Collections.singletonList(sessionKey))));
        } finally {
            jedisPool.returnResource(jedis);
        }
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
        Jedis jedis = jedisPool.getResource();
        try {
            // Step 1. Removing associated key from the list
            jedis.del(player);
        } finally {
            jedisPool.returnResource(jedis);
        }
        // Step 2. Notifying listeners of state change
        notifyStateChange(PlayerPresence.offline(player));
    }

    @Override
    public Date markAvailable(String player) {
        // Step 1. Notifying player entered event
        systemNotificationService.notify(new SystemPlayerEnteredEvent(player));
        // Step 2. Generating expiration date
        return markOnline(player);
    }

    @Override
    public Date markOnline(final String player) {
        Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Jedis jedis = jedisPool.getResource();
        try {
            // Step 1. Setting new expiration time
            jedis.set(player, GameSessionAware.DEFAULT_SESSION);
            jedis.expireAt(player, newExpirationTime.getTime());
        } finally {
            jedisPool.returnResource(jedis);
        }
        // Step 2. Sending notification, for player state update
        notifyStateChange(PlayerPresence.online(player));
        return newExpirationTime;
    }

    private void notifyStateChange(final PlayerPresence newPresence) {
        // Step 1. Notifying through native Redis mechanisms
        systemNotificationService.notify(new SystemPlayerPresenceChangedEvent(newPresence));
        // Step 2. Notifying through native Rabbit mechanisms
        presenceNotification.notify(newPresence.getPlayer(), new PlayerPresenceChangedEvent(newPresence));
    }

}
