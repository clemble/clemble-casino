package com.gogomaya.server.game.connection;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.event.GameEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitGameNotificationService implements GameNotificationService {

    final private LoadingCache<String, RabbitTemplate> RABBIT_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<String, RabbitTemplate>() {

        @Override
        public RabbitTemplate load(String server) throws Exception {
            ConnectionFactory connectionFactory = new CachingConnectionFactory(server);
            RabbitTemplate rabbitTemplagte = new RabbitTemplate(connectionFactory);
            rabbitTemplagte.setMessageConverter(jsonMessageConverter);
            rabbitTemplagte.setExchange("amq.topic");
            return rabbitTemplagte;
        }

    });

    final private LoadingCache<Long, GameConnection> GAME_CONNECTION = CacheBuilder.newBuilder().build(new CacheLoader<Long, GameConnection>() {

        @Override
        public GameConnection load(Long key) throws Exception {
            return null;
        }

    });

    final private JsonMessageConverter jsonMessageConverter;

    @Inject
    public RabbitGameNotificationService(JsonMessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @Override
    public void notify(final GameTable<?> table) {
        // Step 1. Fetching new rabbit
        RabbitTemplate rabbitTemplate = null;
        try {
            rabbitTemplate = RABBIT_CACHE.get(table.getServerResource().getNotificationURL());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // Step 2. Sending session update
        rabbitTemplate.convertAndSend(String.valueOf(table.getTableId()), table);
    }

    @Override
    public void notify(final GameConnection connection, final GameEvent<?> event) {
        // Step 1. Fetching new rabbit
        RabbitTemplate rabbitTemplate = null;
        try {
            rabbitTemplate = RABBIT_CACHE.get(connection.getServerConnection().getNotificationURL());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // Step 2. Sending session update
        rabbitTemplate.convertAndSend(String.valueOf(connection.getRoutingKey()), event);
    }

}
