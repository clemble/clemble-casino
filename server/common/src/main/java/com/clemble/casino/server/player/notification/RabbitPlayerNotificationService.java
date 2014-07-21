package com.clemble.casino.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RabbitPlayerNotificationService implements PlayerNotificationService {

    final private LoadingCache<String, RabbitTemplate> RABBIT_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<String, RabbitTemplate>() {

        @Override
        public RabbitTemplate load(String server) throws Exception {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(server);
            connectionFactory.setExecutor(executorService);
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(messageConverter);
            rabbitTemplate.setExchange("amq.topic");
            return rabbitTemplate;
        }

    });

    final private static Logger LOG = LoggerFactory.getLogger(RabbitPlayerNotificationService.class);

    final private String postfix;
    final private MessageConverter messageConverter;
    final private String host;

    final private ExecutorService executorService;

    public RabbitPlayerNotificationService(
        final String postfix,
        final MessageConverter messageConverter,
        final String host) {
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.host = host;

        ThreadFactory notificationThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("CL playerNotification with '" + postfix + "' %d")
            .build();
        this.executorService = Executors.newFixedThreadPool(2, notificationThreadFactory);
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
            RabbitTemplate rabbitTemplate = RABBIT_CACHE.get(host);
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

    @Override
    public <T extends Event> boolean notifyAll(Collection<? extends PlayerAware> players, T event) {
        return notify(PlayerAwareUtils.toPlayerList(players), event);
    }

    @Override
    public <T extends Event> boolean notifyAll(Collection<? extends PlayerAware> players, Collection<? extends T> events) {
        return notify(PlayerAwareUtils.toPlayerList(players), events);
    }

}
