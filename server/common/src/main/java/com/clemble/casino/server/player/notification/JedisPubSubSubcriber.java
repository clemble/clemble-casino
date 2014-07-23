
package com.clemble.casino.server.player.notification;
import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class JedisPubSubSubcriber<T extends JedisPubSub & RedisSubscribersAware> implements Runnable {

    final private Logger LOG = LoggerFactory.getLogger(JedisPubSubSubcriber.class);

    final private ScheduledExecutorService scheduledExecutorService;

    final private JedisPool jedisPool;
    final private T jedisPubSub;

    public JedisPubSubSubcriber(JedisPool jedisPool, T jedisPubSub) {
        this.jedisPool = checkNotNull(jedisPool);
        this.jedisPubSub = checkNotNull(jedisPubSub);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("CL " + jedisPubSub.toString()).build());

        this.scheduledExecutorService.execute(this);
    }

    @Override
    public void run() {
        LOG.info("Starting listener");
        Jedis jedis = jedisPool.getResource();
        try {
            // TODO Enable support for both channels and patterns on startup
            if (!jedisPubSub.getChannels().isEmpty()) {
                LOG.info("Listening for registered channels {}", jedisPubSub.getChannels());
                jedis.subscribe(jedisPubSub, jedisPubSub.getChannels().toArray(new String[0]));
            } else if(!jedisPubSub.getPatterns().isEmpty()) {
                LOG.info("Listening for registered patterns");
                jedis.psubscribe(jedisPubSub, jedisPubSub.getPatterns().toArray(new String[0]));
            } else {
                LOG.info("Listening for registered subscribers NULL");
                jedis.subscribe(jedisPubSub, "null");
            }
            LOG.info("Failed to start because of {}", jedis);
        } catch(Throwable throwable) {
            LOG.error("Failed to start because of", throwable);
            scheduledExecutorService.schedule(this, 30, TimeUnit.SECONDS);
        } finally {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    public void close(){
        scheduledExecutorService.shutdownNow();
    }

}
