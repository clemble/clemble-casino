package com.clemble.casino.server.player.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class JedisPlayerPresenceCleaner extends JedisPubSub implements RedisSubscribersAware, PlayerPresenceCleaner {

    final private static Collection<String> CHANNELS = ImmutableList.<String>of("__keyevent@0__:expired");
    final private static Logger LOG = LoggerFactory.getLogger(JedisPlayerPresenceCleaner.class);

    final private ServerPlayerPresenceService presenceService;
    final private JedisPubSubSubcriber<JedisPlayerPresenceCleaner> subcriber;

    public JedisPlayerPresenceCleaner(JedisPool jedisPool, ServerPlayerPresenceService presenceService) {
        this.presenceService = checkNotNull(presenceService);
        this.subcriber = new JedisPubSubSubcriber<>(jedisPool, this);
    }

    @Override
    public void onMessage(String channel, String message) {
        LOG.debug("Switching {} to offline", message);
        presenceService.markOffline(message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        LOG.error("Ignoring pattern {}, channel {}, message {}", pattern, channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        LOG.debug("Subscribed channel {}, channels num {}", channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        LOG.trace("Unsubscribed channel {}, channels num {}", channel, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        LOG.trace("Subscribed pattern {}, channels num {}", pattern, subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        LOG.trace("Unsubscribe pattern {}, channels num {}", pattern, subscribedChannels);
    }

    @Override
    public Collection<String> getChannels() {
        return CHANNELS;
    }

    @Override
    public Collection<String> getPatterns() {
        return ImmutableList.<String>of();
    }

    public void close(){
        subcriber.close();
    }

    public String toString(){
        return "playerPresenceCleaner";
    }
}
