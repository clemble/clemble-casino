package com.clemble.casino.server.player.presence;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 05/12/13
 * Time: 06:54
 * To change this template use File | Settings | File Templates.
 */
public class JedisPlayerPresenceServerService implements PlayerPresenceServerService {

    final private Logger LOG = LoggerFactory.getLogger(JedisPlayerPresenceServerService.class);

    final private int EXPIRATION_TIME = (int) TimeUnit.MINUTES.toMillis(20);
    final private String ZERO_SESSION = ":";

    final private String UPDATE_SCRIPT;
    final private JedisPool jedisPool;
    final private PlayerNotificationService<PlayerPresence> presenceNotification;

    public JedisPlayerPresenceServerService(
            JedisPool jedisPool,
            PlayerNotificationService<PlayerPresence> presenceNotification) {
        this.jedisPool = checkNotNull(jedisPool);
        this.presenceNotification = checkNotNull(presenceNotification);
        Jedis jedis = jedisPool.getResource();
        try {
            this.UPDATE_SCRIPT = jedis.scriptLoad(
            " for i = 1,table.getn(KEYS) do " +
                " local state = redis.call('get', KEYS[i])" +
                " if (state ~= ':') then " +
                    " redis.log(redis.LOG_DEBUG, ARGV[1] .. ' > ' .. KEYS[i] .. ' is in ' .. state); " +
                    " return 'false'" +
                " end " +
            " end " +
            " redis.log(redis.LOG_DEBUG, 'Starting ' .. ARGV[1]) " +
            " for i = 1,table.getn(KEYS) do " +
                " redis.call('set', KEYS[i], ARGV[1])" +
            " end " +
            " return 'true'");
        } finally {
          jedisPool.returnResource(jedis);
        }
    }

    public GameSessionKey getActiveSession(String player) {
        Jedis jedis = jedisPool.getResource();
        LOG.trace("Available connection + {}", jedis);
        try {
            return GameSessionKey.fromString(jedis.get(player));
        } finally {
            LOG.trace("Available connection - {}", jedis);
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public PlayerPresence getPresence(String player) {
        GameSessionKey session = getActiveSession(player);
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
        GameSessionKey activePlayerSession = getActiveSession(player);
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
    public boolean markPlaying(final String player, final GameSessionKey sessionId) {
        if (!isAvailable(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionTimeout);
        return markPlaying(Collections.singleton(player), sessionId);
    }

    @Override
    public boolean markPlaying(final Collection<String> players, final GameSessionKey sessionKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            Boolean updated = Boolean.valueOf(String.valueOf(jedis.evalsha(UPDATE_SCRIPT, new ArrayList<>(players), Collections.singletonList(sessionKey.toString()))));
            // Step 2. If atomic update success, then move on
            if (updated) {
                for(String player: players) {
                    notifyStateChange(PlayerPresence.playing(player, sessionKey), jedis);
                }
            }
            // Step 3. Returning result of operation
            return updated;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void markOffline(String player) {
        Jedis jedis = jedisPool.getResource();
        try {
            // Step 1. Removing associated key from the list
            jedis.del(player);
            // Step 2. Notifying listeners of state change
            notifyStateChange(PlayerPresence.offline(player), jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public Date markOnline(final String player) {
        Jedis jedis = jedisPool.getResource();
        LOG.trace("Online connection + {}", jedis);
        try {
            // Step 1. Setting new expiration time
            Date newExpirationTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
            jedis.set(player, ZERO_SESSION);
            jedis.expireAt(player, newExpirationTime.getTime());
            // Step 2. Sending notification, for player state update
            notifyStateChange(PlayerPresence.online(player), jedis);
            return newExpirationTime;
        } finally {
            LOG.trace("Online connection - {}", jedis);
            jedisPool.returnResource(jedis);
        }
    }

    private void notifyStateChange(final PlayerPresence newPresence, Jedis jedis) {
        // Step 1. Notifying through native Redis mechanisms
        Long numUpdatedClients = jedis.publish(newPresence.getPlayer(), newPresence.getPresence().name());
        // Step 2. Notifying through native Rabbit mechanisms
        Boolean notified = presenceNotification.notify(newPresence.getPlayer(), newPresence);
        LOG.info("{} update to {}, notified {} Redis listeners, and notified Rabbit {}", newPresence.getPlayer(), newPresence.getPresence(), numUpdatedClients, notified);
    }
}
