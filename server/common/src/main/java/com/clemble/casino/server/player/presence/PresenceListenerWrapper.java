package com.clemble.casino.server.player.presence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PresenceListenerWrapper implements MessageListener {

    final private Logger LOG = LoggerFactory.getLogger(PresenceListenerWrapper.class);

    final private ObjectMapper objectMapper;
    final private RedisSerializer<String> stringRedisSerializer;
    final private SystemEventListener<SystemEvent> playerStateListener;

    @SuppressWarnings("unchecked")
    public PresenceListenerWrapper(RedisSerializer<String> stringRedisSerializer, SystemEventListener<? extends SystemEvent> playerStateListener, ObjectMapper objectMapper) {
        this.stringRedisSerializer = checkNotNull(stringRedisSerializer);
        this.playerStateListener = (SystemEventListener<SystemEvent>) checkNotNull(playerStateListener);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LOG.debug("processing: {}", message);
        // Step 1. Reading channel and message
        String deserializedChannel = stringRedisSerializer.deserialize(message.getChannel());
        String deserializedMessage = stringRedisSerializer.deserialize(message.getBody());
        // Step 2. Notifying associated PlayerStateListener
        LOG.debug("channel: {}, message: {}", deserializedChannel, deserializedMessage);
        try {
            playerStateListener.onEvent(deserializedChannel, objectMapper.readValue(deserializedMessage, SystemEvent.class));
        } catch (IOException ioException) {
            LOG.error("Failed to deserialize {}", deserializedMessage);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerStateListener == null) ? 0 : playerStateListener.hashCode());
        result = prime * result + ((stringRedisSerializer == null) ? 0 : stringRedisSerializer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PresenceListenerWrapper other = (PresenceListenerWrapper) obj;
        if (playerStateListener == null) {
            if (other.playerStateListener != null)
                return false;
        } else if (!playerStateListener.equals(other.playerStateListener))
            return false;
        if (stringRedisSerializer == null) {
            if (other.stringRedisSerializer != null)
                return false;
        } else if (!stringRedisSerializer.equals(other.stringRedisSerializer))
            return false;
        return true;
    }

}
