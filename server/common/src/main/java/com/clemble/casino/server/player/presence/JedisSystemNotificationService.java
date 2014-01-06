package com.clemble.casino.server.player.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JedisSystemNotificationService implements SystemNotificationService {

    final private static Logger LOG = LoggerFactory.getLogger(JedisSystemNotificationService.class);
    
    final private JedisPool jedisPool;
    final private ObjectMapper objectMapper;
    
    public JedisSystemNotificationService(JedisPool jedisPool, ObjectMapper objectMapper) {
        this.jedisPool = checkNotNull(jedisPool);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void notify(SystemEvent event) {
        // Step 1. Fetching jedis connection
        Jedis jedis = jedisPool.getResource();
        try {
            // Step 2. Notifying players
            Long numUpdatedClients = jedis.publish(event.getChannel(), objectMapper.writeValueAsString(event));
            LOG.debug("published {} system events", numUpdatedClients);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to convert to JSON", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}
