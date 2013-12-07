package com.clemble.casino.server.player.presence;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 05/12/13
 * Time: 13:21
 * To change this template use File | Settings | File Templates.
 */
public class JedisPlayerPresenceListenerService extends JedisPubSub implements PlayerPresenceListenerService, Runnable {

    private Logger LOG = LoggerFactory.getLogger(JedisPlayerPresenceListenerService.class);

    static final private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("Presence Listener").build());

    final private JedisPool jedisPool;
    final private ReentrantLock lock = new ReentrantLock();
    final private ConcurrentHashMap<String, Collection<PlayerNotificationListener<Presence>>> subscribers = new ConcurrentHashMap<>();

    public JedisPlayerPresenceListenerService(JedisPool jedis) {
        this.jedisPool = jedis;
        this.scheduledExecutorService.execute(this);
    }

    @Override
    public void subscribe(String playerId, PlayerNotificationListener<Presence> messageListener) {
        // Step 1. Initializing subscription
        if(!subscribers.containsKey(playerId)) {
            if(subscribers.putIfAbsent(playerId, new LinkedBlockingQueue<PlayerNotificationListener<Presence>>()) == null){
                lock.lock();
                try {
                    subscribe(playerId);
                } finally {
                    lock.unlock();
                }
            }
        }
        // Step 2. Adding new message listener to the Queue
        subscribers.get(playerId).add(messageListener);
    }

    @Override
    public void subscribe(Collection<String> players, PlayerNotificationListener<Presence> messageListener) {
        for(String player: players) {
            subscribe(player, messageListener);
        }
    }

    @Override
    public void unsubscribe(String player, PlayerNotificationListener<Presence> messageListener) {
        // Step 1. Removing specific listener
        subscribers.get(player).remove(messageListener);
        // Step 2. If there is nothing to subscribe to for the player remove entry and unsubscribe
        if(subscribers.get(player).isEmpty()){
            subscribers.remove(player);
            // Step 2.1 Calling inherited unsubscribe method
            this.unsubscribe(player);
        }
    }

    @Override
    public void unsubscribe(Collection<String> players, PlayerNotificationListener<Presence> playerStateListener) {
        for(String player: players)
            unsubscribe(player, playerStateListener);
    }

    @Override
    public void onMessage(String player, String presence) {
        Collection<PlayerNotificationListener<Presence>> playerPresenceListeners = subscribers.get(player);
        if (playerPresenceListeners != null) {
            for(PlayerNotificationListener notificationListener: playerPresenceListeners) {
                notificationListener.onUpdate(player, Presence.valueOf(presence));
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

//    @Override
//    public void subscribe(String... channels) {
//        synchronized (subscribers) {
//            super.subscribe(channels);
//        }
//    }

    @Override
    public void run(){
        LOG.info("Starting listener");
        Jedis jedis = jedisPool.getResource();
        try {
            String[] subscriptions = subscribers.keySet().toArray(new String[0]);
            if(subscriptions.length > 0) {
                LOG.info("Listening for registered subscribers {}", subscriptions);
                jedis.subscribe(this, subscriptions);
            } else {
                LOG.info("Listening for registered subscribers NULL");
                jedis.subscribe(this, "null");
            }
            LOG.info("Failed to start because of {}", jedis);
        } catch(Throwable throwable) {
            LOG.error("Failed to start because of", throwable);
            this.scheduledExecutorService.schedule(this, 30, TimeUnit.SECONDS);
        } finally {
            jedisPool.returnBrokenResource(jedis);
        }
    }

}
