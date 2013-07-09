package com.gogomaya.server.player.state;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerState;

public class JedisPlayerStateManager implements PlayerStateManager {

    final static byte[] LONG_ZERO = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

    final private Logger LOGGER = LoggerFactory.getLogger(RedisPlayerStateManager.class);

    final private JedisConnectionFactory jedisConnectionFactory;
    final private RedisSerializer<Long> redisSerializer = new RedisSerializer<Long>() {

        @Override
        public byte[] serialize(Long t) throws SerializationException {
            if (t == null) {
                return LONG_ZERO;
            } else {
                ByteBuffer byteBuffer = ByteBuffer.allocate(8);
                byteBuffer.putLong(t.longValue());
                return byteBuffer.array();
            }
        }

        @Override
        public Long deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null)
                return null;
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getLong();
        }
    };

    public JedisPlayerStateManager(final JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    @Override
    public Long getActiveSession(long playerId) {
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        return redisSerializer.deserialize(jedisConnection.get(redisSerializer.serialize(playerId)));
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
        byte[][] playerBytes = toByteArrays(players);
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        // Step 1. Checking all players are available
        for (Long player : players) {
            jedisConnection.watch(redisSerializer.serialize(player));
            if (!isAvailable(player)) {
                return false;
            }
        }
        // Step 2. Performing atomic operation
        jedisConnection.multi();
        byte[] sessionBytes = redisSerializer.serialize(sessionId);
        for (byte[] player : playerBytes) {
            jedisConnection.set(player, sessionBytes);
        }
        // Step 3. If operation failed discard it
        boolean updated = jedisConnection.exec() != null;
        // Step 4. If atomic update success, then move on
        if (updated) {
            notifyStateChange(toByteArrays(players), PlayerState.busy);
        }
        // Step 5. Returning result of operation
        return updated;
    }

    @Override
    public void markAvailable(final long playerId) {
        byte[] player = redisSerializer.serialize(playerId);
        // Step 1. Specifying null state as identifier that player is active, and available
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        jedisConnection.set(player, LONG_ZERO);
        jedisConnection.expire(player, 30);
        // Step 2. Sending notification, for player state update
        notifyStateChange(player, PlayerState.available);
    }

    @Override
    public void subscribe(final long playerId, final MessageListener messageListener) {
        subscribe(Collections.singleton(playerId), messageListener);
    }

    @Override
    public void subscribe(Collection<Long> players, MessageListener messageListener) {
        byte[][] channels = toByteArrays(players);
        // Step 1. Add message listener
        jedisConnectionFactory.getConnection().subscribe(messageListener, channels);
    }

    @Override
    public void unsubscribe(final long player, final MessageListener messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<Long> players, MessageListener messageListener) {
        // jedisConnectionFactory.getConnection().subscribe(messageListener, channels);
    }

    private byte[][] toByteArrays(Collection<Long> players) {
        byte[][] channels = new byte[players.size()][];
        int i = 0;
        for (Long player : players)
            channels[i++] = redisSerializer.serialize(player);
        return channels;
    }

    private void notifyStateChange(byte[][] players, final PlayerState state) {
        for (byte[] player : players) {
            notifyStateChange(player, state);
        }
    }

    private void notifyStateChange(final byte[] playerId, final PlayerState state) {
        Long numUpdatedClients = jedisConnectionFactory.getConnection().publish(playerId, new byte[] { (byte) state.ordinal() });

        LOGGER.debug("Notified of change in {} state {} listeners", playerId, numUpdatedClients);
    }

}
