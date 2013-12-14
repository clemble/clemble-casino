package com.clemble.casino.server.player.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringRedisPlayerPresenceListenerService implements SystemNotificationServiceListener {

    final private Logger LOGGER = LoggerFactory.getLogger(StringRedisPlayerPresenceListenerService.class);

    final private ObjectMapper objectMapper;
    final private StringRedisTemplate redisTemplate;
    final private RedisSerializer<String> stringRedisSerializer;
    final private RedisMessageListenerContainer listenerContainer;

    public StringRedisPlayerPresenceListenerService(ObjectMapper objectMapper, StringRedisTemplate redisTemplate,
            RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.stringRedisSerializer = checkNotNull(redisTemplate).getStringSerializer();
        this.listenerContainer = checkNotNull(listenerContainer);
    }

    @Override
    public void subscribe(final String player, final SystemEventListener<? extends SystemEvent> messageListener) {
        subscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void subscribe(Collection<String> players, SystemEventListener<? extends SystemEvent> messageListener) {
        LOGGER.debug("Subscribing {} for changes from {}", players, messageListener);
        // Step 1. Add message listener
        listenerContainer
                .addMessageListener(new PresenceListenerWrapper(redisTemplate.getStringSerializer(), messageListener, objectMapper), toTopics(players));
        // Step 2. Checking if listener container is alive, and starting it if needed
        if (!listenerContainer.isActive() || !listenerContainer.isRunning())
            listenerContainer.start();
    }

    @Override
    public void unsubscribe(final String player, final SystemEventListener<? extends SystemEvent> messageListener) {
        unsubscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void unsubscribe(Collection<String> players, SystemEventListener<? extends SystemEvent> playerStateListener) {
        LOGGER.debug("Unsubscribing {} for changes from {}", players, playerStateListener);
        listenerContainer.removeMessageListener(new PresenceListenerWrapper(stringRedisSerializer, playerStateListener, objectMapper), toTopics(players));
    }

    private Collection<Topic> toTopics(Collection<String> players) {
        Collection<Topic> playerTopics = new ArrayList<Topic>(players.size());
        for (String player : players)
            playerTopics.add(new ChannelTopic(player));
        return playerTopics;
    }

    @Override
    public void subscribe(EventSelector eventSelector, SystemEventListener<? extends SystemEvent> messageListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsubscribe(EventSelector eventSelector, SystemEventListener<? extends SystemEvent> playerStateListener) {
        throw new UnsupportedOperationException();
    }

}
