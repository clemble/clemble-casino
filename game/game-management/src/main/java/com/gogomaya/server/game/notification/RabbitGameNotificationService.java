package com.gogomaya.server.game.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitGameNotificationService<State extends GameState> implements GameNotificationService<State> {

    final private LoadingCache<String, RabbitTemplate> RABBIT_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<String, RabbitTemplate>() {

        @Override
        public RabbitTemplate load(String server) throws Exception {
            ConnectionFactory connectionFactory = new CachingConnectionFactory(server);
            RabbitTemplate rabbitTemplagte = new RabbitTemplate(connectionFactory);
            rabbitTemplagte.setMessageConverter(messageConverter);
            rabbitTemplagte.setExchange("amq.topic");
            return rabbitTemplagte;
        }

    });

    final private MessageConverter messageConverter;

    final private PlayerNotificationRegistry notificationRegistry;

    @Inject
    public RabbitGameNotificationService(final MessageConverter messageConverter, final PlayerNotificationRegistry notificationRegistry) {
        this.messageConverter = checkNotNull(messageConverter);
        this.notificationRegistry = checkNotNull(notificationRegistry);
    }

    @Override
    public void notify(final Collection<Long> playerIds, final Event event) {
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        for (Long playerId : playerIds)
            notify(playerId, message);
    }

    @Override
    public void notify(final Collection<Long> playerIds, final Collection<? extends Event> events) {
        // Step 1. Notifying each event one after another
        for (Event event : events)
            notify(playerIds, event);
    }

    @Override
    public void notify(long playerId, Event event) {
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        notify(playerId, message);
    }

    private void notify(final long playerId, final Message message) {
        try {
            RabbitTemplate rabbitTemplate = RABBIT_CACHE.get(notificationRegistry.findNotificationServer(playerId));
            rabbitTemplate.send(String.valueOf(playerId), message);
        } catch (ExecutionException e) {
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        }
    }

}
