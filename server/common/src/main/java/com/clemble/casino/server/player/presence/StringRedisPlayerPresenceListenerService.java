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

import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationListener;

public class StringRedisPlayerPresenceListenerService implements PlayerPresenceListenerService {

    final private Logger LOGGER = LoggerFactory.getLogger(StringRedisPlayerPresenceListenerService.class);

    final private StringRedisTemplate redisTemplate;
    final private RedisSerializer<String> stringRedisSerializer;
    final private RedisMessageListenerContainer listenerContainer;

    public StringRedisPlayerPresenceListenerService(StringRedisTemplate redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.stringRedisSerializer = checkNotNull(redisTemplate).getStringSerializer();
        this.listenerContainer = checkNotNull(listenerContainer);
    }

    @Override
    public void subscribe(final String player, final PlayerNotificationListener<Presence> messageListener) {
        subscribe(Collections.singleton(player), messageListener);
    }

    @Override
    public void subscribe(Collection<String> players, PlayerNotificationListener<Presence> messageListener) {
        LOGGER.debug("Subscribing {} for changes from {}", players, messageListener);
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
        LOGGER.debug("Unsubscribing {} for changes from {}", players, playerStateListener);
        listenerContainer.removeMessageListener(new PresenceListenerWrapper(stringRedisSerializer, playerStateListener), toTopics(players));
    }

    private Collection<Topic> toTopics(Collection<String> players) {
        Collection<Topic> playerTopics = new ArrayList<Topic>(players.size());
        for (String player : players)
            playerTopics.add(new ChannelTopic(player));
        return playerTopics;
    }

}
