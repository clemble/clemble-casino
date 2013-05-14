package com.gogomaya.server.game.connection;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameState;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitGameNotificationService<State extends GameState> implements GameNotificationService<State> {

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

    final private JsonMessageConverter jsonMessageConverter;

    @Inject
    public RabbitGameNotificationService(JsonMessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @Override
    public void notify(final GameConnection connection, final GogomayaEvent event) {
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

    @Override
    public void notify(GameConnection connection, Collection<? extends GogomayaEvent> events) {
        // Step 1. Notifying each event one after another
        for (GogomayaEvent event : events)
            notify(connection, event);
    }

}
