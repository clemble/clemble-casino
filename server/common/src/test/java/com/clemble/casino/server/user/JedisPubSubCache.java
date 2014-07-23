package com.clemble.casino.server.user;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.server.player.notification.JedisPubSubSubcriber;
import com.clemble.casino.server.player.notification.RedisSubscribersAware;
import com.google.common.collect.ImmutableList;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class JedisPubSubCache extends JedisPubSub implements RedisSubscribersAware {

    final private Logger LOG = LoggerFactory.getLogger(JedisPubSubCache.class);

    final private JedisPubSubSubcriber<JedisPubSubCache> subcriber;

    public JedisPubSubCache(JedisPool jedisPool) {
        subcriber = new JedisPubSubSubcriber<>(jedisPool, this);
    }

    @Override
    public void onMessage(String channel, String message) {
        LOG.debug("Channel {} with message {}", channel, message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        LOG.debug("Pattern {}, channel {}, message {}", pattern, channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        LOG.debug("Subscribed channel {}, message {}", channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        LOG.debug("UnSubscribed channel {}, message {}", channel, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        LOG.debug("UnPSubscribed channel {}, message {}", pattern, subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        LOG.debug("PSubscribed channel {}, message {}", pattern, subscribedChannels);
    }

    @Override
    public Collection<String> getChannels() {
        return ImmutableList.<String>of();
    }

    @Override
    public Collection<String> getPatterns() {
        return ImmutableList.<String>of("*");
    }

    @Override
    public String toString() {
        return "jedisTestCache";
    }

    public void close(){
        subcriber.close();
    }

}
