package com.clemble.casino.server.player.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

public class JedisSystemNoficiationServiceListener extends JedisPubSub implements SystemNotificationServiceListener, RedisSubscribersAware {

    static final private Logger LOG = LoggerFactory.getLogger(JedisSystemNoficiationServiceListener.class);

    final private JedisPool jedisPool;
    final private ObjectMapper objectMapper;
    final private ReentrantLock lock = new ReentrantLock();
    final private ConcurrentHashMap<String, Collection<SystemEventListener<? extends SystemEvent>>> subscribers = new ConcurrentHashMap<>();
    final private JedisPubSubSubcriber<JedisSystemNoficiationServiceListener> subcriber;

    public JedisSystemNoficiationServiceListener(JedisPool jedis, ObjectMapper objectMapper) {
        // Step 1. Generic initialization
        this.jedisPool = checkNotNull(jedis);
        this.objectMapper = checkNotNull(objectMapper);
        // Step 2. Initializing Jedis subscriber, in which this JedisPubSub will be running
        this.subcriber = new JedisPubSubSubcriber<>(jedisPool, this);
    }

    @Override
    public Collection<String> getChannels() {
        return subscribers.keySet();
    }

    @Override
    public void subscribe(SystemEventListener<? extends SystemEvent> messageListener) {
        // Step 1. Initializing subscription
        if (!subscribers.containsKey(messageListener.getChannel())) {
            if (subscribers.putIfAbsent(messageListener.getChannel(), new LinkedBlockingQueue<SystemEventListener<? extends SystemEvent>>()) == null) {
                lock.lock();
                try {
                    if (isSubscribed())
                        subscribe(messageListener.getChannel());
                } finally {
                    lock.unlock();
                }
            }
        }
        // Step 2. Adding new message listener to the Queue
        subscribers.get(messageListener.getChannel()).add(messageListener);
    }

    @Override
    public void unsubscribe(SystemEventListener<? extends SystemEvent> messageListener) {
        // Step 1. Removing specific listener
        subscribers.get(messageListener.getChannel()).remove(messageListener);
        // Step 2. If there is nothing to subscribeToGameSession to for the player remove entry and unsubscribe
        if (subscribers.get(messageListener.getChannel()).isEmpty()) {
            subscribers.remove(messageListener.getChannel());
            // Step 2.1 Calling inherited unsubscribe method
            this.unsubscribe(messageListener.getChannel());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(String channel, String serializedEvent) {
        Collection<SystemEventListener<? extends SystemEvent>> channelListeners = subscribers.get(channel);
        SystemEvent event;
        try {
            event = objectMapper.readValue(serializedEvent, SystemEvent.class);
        } catch (IOException e) {
            LOG.error("Failed to read \"{}\"", serializedEvent);
            throw new RuntimeException(e); // TODO change
        }
        if (channelListeners != null && event != null) {
            for (SystemEventListener<? extends SystemEvent> channelListener : channelListeners) {
                ((SystemEventListener<SystemEvent>) channelListener).onEvent(event);
            }
        }
    }

    @Override
    public void onPMessage(String s, String s2, String s3) {
        onMessage(s, s2);
    }

    @Override
    public void onSubscribe(String s, int i) {
    }

    @Override
    public void onUnsubscribe(String s, int i) {
    }

    @Override
    public void onPUnsubscribe(String s, int i) {
    }

    @Override
    public void onPSubscribe(String s, int i) {
    }

    @Override
    public Collection<String> getPatterns() {
        return ImmutableList.<String>of();
    }

    public void close(){
        subcriber.close();
    }

    public String toString(){
        return "systemNotificationListener";
    }

}
