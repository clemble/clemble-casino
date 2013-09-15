package com.gogomaya.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.gogomaya.event.Event;
import com.gogomaya.server.configuration.ServerRegistryServerService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitPlayerNotificationService<T extends Event>  implements PlayerNotificationService<T> {

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

    final private String postfix;
    final private MessageConverter messageConverter;
    final private ServerRegistryServerService serverRegistryService;

    public RabbitPlayerNotificationService(final String postfix, final MessageConverter messageConverter, final ServerRegistryServerService serverRegistryService) {
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.serverRegistryService = checkNotNull(serverRegistryService);
    }

    @Override
    public boolean notify(final Collection<Long> playerIds, final T event) {
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        boolean fullSuccess = true;
        for (Long playerId : playerIds)
            fullSuccess = notify(playerId, message) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public boolean notify(final Collection<Long> playerIds, final Collection<? extends T> events) {
        // Step 1. Notifying each event one after another
        boolean fullSuccess = true;
        for (T event : events)
            fullSuccess = notify(playerIds, event) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public boolean notify(long playerId, T event) {
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        return notify(playerId, message);
    }

    private boolean notify(final long playerId, final Message message) {
        try {
            RabbitTemplate rabbitTemplate = RABBIT_CACHE.get(serverRegistryService.getPlayerNotificationRegistry().findNotificationServer(playerId));
            rabbitTemplate.send(String.valueOf(playerId) + postfix, message);
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

}
