package com.clemble.casino.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.player.service.PlayerConnectionService;
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
            connectionFactory.setUsername(user);
            connectionFactory.setPassword(password);
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
    final private String user;
    final private String password;

    final private PlayerConnectionService connectionService;
    final private ExecutorService executorService;

    public RabbitPlayerNotificationService(
        final String postfix,
        final MessageConverter messageConverter,
        final String host,
        final String user,
        final String password,
        final PlayerConnectionService connectionService) {
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.host = host;
        this.user = user;
        this.password = password;
        this.connectionService = connectionService;

        ThreadFactory notificationThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("CL playerNotification with '" + postfix + "' %d")
            .build();
        this.executorService = Executors.newFixedThreadPool(2, notificationThreadFactory);
    }

    @Override
    public <T extends Event> boolean send(final Collection<String> players, final T event) {
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
    public <T extends Event> boolean send(final String player, final Collection<T> events) {
        boolean fullSuccess = true;
        for (T event : events)
            fullSuccess = send(player, event) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public <T extends PlayerAware & Event> boolean sendToAll(T event) {
        String player = event.getPlayer();
        Collection<String> connections = new ArrayList<>(connectionService.getConnections(player));
        connections.add(player);
        return send(connections, event);
    }

    public <T extends PlayerAware & Event> boolean send(PrivacyRule privacyRule, T event) {
        switch (privacyRule) {
            case friends:
            case world:
                return sendToAll(event);
            case me:
            default:
                return send(event);
        }
    }

    @Override
    public <T extends Event> boolean send(final Collection<String> players, final Collection<? extends T> events) {
        // Step 1. Notifying each event one after another
        boolean fullSuccess = true;
        for (T event : events)
            fullSuccess = send(players, event) & fullSuccess;
        return fullSuccess;
    }

    @Override
    public <T extends Event> boolean send(String player, T event) {
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
    public <T extends PlayerAware & Event> boolean send(Collection<T> events) {
        // Step 1. Sanity check
        if (events == null || events.size() == 0)
            return true;
        // Step 2. Checking values in Event
        boolean notifiedAll = true;
        for (T event : events) {
            notifiedAll = send(event) & notifiedAll;
        }
        return notifiedAll;
    }

    @Override
    public <T extends PlayerAware & Event> boolean send(T event) {
        return event != null ? send(event.getPlayer(), event) : true;
    }

    @Override
    public <T extends Event> boolean sendAll(Collection<? extends PlayerAware> players, T event) {
        return send(PlayerAwareUtils.toPlayerList(players), event);
    }

    @Override
    public <T extends Event> boolean sendAll(Collection<? extends PlayerAware> players, Collection<? extends T> events) {
        return send(PlayerAwareUtils.toPlayerList(players), events);
    }

}
