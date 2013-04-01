package com.gogomaya.server.game.connection;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import com.gogomaya.server.game.table.GameTable;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameNotificationManager {

    final private JsonMessageConverter jsonMessageConverter;

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

    @Inject
    public GameNotificationManager(JsonMessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    public void notify(final GameTable table) {
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

}
