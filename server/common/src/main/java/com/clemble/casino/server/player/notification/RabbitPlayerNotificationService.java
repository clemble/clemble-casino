package com.clemble.casino.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerAware;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitPlayerNotificationService implements PlayerNotificationService {

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

    final private static Logger LOG = LoggerFactory.getLogger(RabbitPlayerNotificationService.class);

    final private String postfix;
    final private MessageConverter messageConverter;
    final private ServerRegistry serverRegistry;

    public RabbitPlayerNotificationService(final String postfix, final MessageConverter messageConverter, final ServerRegistry serverRegistry) {
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.serverRegistry = checkNotNull(serverRegistry);
    }

    @Override
    public <T extends Event> boolean notify(final Collection<String> players, final T event) {
        LOG.trace("Sending {} to {}", event, players);
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        boolean fullSuccess = true;
        for (String playerId : players)
            fullSuccess = notify(playerId, message) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public <T extends Event> boolean notify(final String player, final Collection<T> events) {
        boolean fullSuccess = true;
        for (T event : events)
            fullSuccess = notify(player, event) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public <T extends Event> boolean notify(final Collection<String> players, final Collection<? extends T> events) {
        // Step 1. Notifying each event one after another
        boolean fullSuccess = true;
        for (T event : events)
            fullSuccess = notify(players, event) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public <T extends Event> boolean notify(String player, T event) {
        LOG.trace("Sending {} to {}", event, player);
        // Step 1. Creating message to send
        Message message = messageConverter.toMessage(event, null);
        // Step 2. Notifying specific player
        return notify(player, message);
    }

    private boolean notify(final String player, final Message message) {
        try {
            RabbitTemplate rabbitTemplate = RABBIT_CACHE.get(serverRegistry.findById(player));
            rabbitTemplate.send(String.valueOf(player) + postfix, message);
        } catch (Throwable e) {
            LOG.trace("Failed notification of {} with {}", player, message);
            return false;
        }
        return true;
    }

    @Override
    public <T extends PlayerAware & Event> boolean notify(Collection<T> events) {
        // Step 1. Sanity check
        if (events == null || events.size() == 0)
            return true;
        // Step 2. Checking values in Event
        boolean notifiedAll = true;
        for (T event : events) {
            notifiedAll = notify(event) & notifiedAll;
        }
        return notifiedAll;
    }

    @Override
    public <T extends PlayerAware & Event> boolean notify(T event) {
        return event != null ? notify(event.getPlayer(), event) : true;
    }

}
