package com.clemble.casino.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;

import com.clemble.casino.server.configuration.ServerRegistryServerService;
import com.clemble.casino.event.Event;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class RabbitPlayerNotificationManager<T extends Event> implements PlayerNotificationListenerManger<T> {

    final private LoadingCache<String, RabbitListenerControl> RABBIT_CONTROL_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<String, RabbitListenerControl>() {

        @Override
        public RabbitListenerControl load(String server) throws Exception {
            ConnectionFactory connectionFactory = new CachingConnectionFactory(server);
            return new RabbitListenerControl(connectionFactory);
        }

    });

    final private ConcurrentHashMap<String, Collection<PlayerNotificationListener<T>>> listeners = new ConcurrentHashMap<>();

    final private String postfix;
    final private MessageConverter messageConverter;
    final private ServerRegistryServerService serverRegistryService;

    public RabbitPlayerNotificationManager(final String postfix, final MessageConverter messageConverter, final ServerRegistryServerService serverRegistryService) {
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.serverRegistryService = checkNotNull(serverRegistryService);
    }

    @Override
    public void subscribe(String player, PlayerNotificationListener<T> messageListener) {
        if (messageListener == null)
            return;
        // Step 1. Checking that there is something
        listeners.putIfAbsent(player, new CopyOnWriteArraySet<PlayerNotificationListener<T>>());
        listeners.get(player).add(messageListener);
        // Step 2. Figuring out involved notification server
        String notificationServer = serverRegistryService.getPlayerNotificationRegistry().findNotificationServer(player);
        // Step 3. Add new binding
        RabbitListenerControl rabbitListenerControl = RABBIT_CONTROL_CACHE.getUnchecked(notificationServer);
        rabbitListenerControl.subscribe(player);
    }

    @Override
    public void subscribe(Collection<String> players, PlayerNotificationListener<T> messageListener) {
        for (String player : players) {
            subscribe(player, messageListener);
        }
    }

    @Override
    public void unsubscribe(String player, PlayerNotificationListener<T> messageListener) {
        if (messageListener == null)
            return;
        // Step 1. Checking that there is something
        Collection<PlayerNotificationListener<T>> notifications = listeners.get(player);
        if (notifications != null)
            notifications.remove(messageListener);
        if (notifications.isEmpty()) {
            // Step 2. Figuring out involved notification server
            String notificationServer = serverRegistryService.getPlayerNotificationRegistry().findNotificationServer(player);
            // Step 3. Add new binding
            RabbitListenerControl rabbitListenerControl = RABBIT_CONTROL_CACHE.getUnchecked(notificationServer);
            rabbitListenerControl.unsubscribe(player);
        }
    }

    @Override
    public void unsubscribe(Collection<String> players, PlayerNotificationListener<T> messageListener) {
        for (String player : players)
            unsubscribe(player, messageListener);
    }

    public class RabbitListenerControl {

        final private RabbitAdmin admin;
        final private Queue queue;
        final private SimpleMessageListenerContainer messageListenerContainer;

        public RabbitListenerControl(ConnectionFactory connectionFactory) {
            this.admin = new RabbitAdmin(connectionFactory);
            this.queue = admin.declareQueue();
            this.messageListenerContainer = new SimpleMessageListenerContainer();
            messageListenerContainer.setConnectionFactory(connectionFactory);
            messageListenerContainer.setQueues(queue);
            messageListenerContainer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    @SuppressWarnings("unchecked")
                    T event = (T) messageConverter.fromMessage(message);
                    String routingKey = message.getMessageProperties().getReceivedRoutingKey();
                    String player = routingKey.substring(0, routingKey.length() - postfix.length());
                    for(PlayerNotificationListener<T> playerStateListener: listeners.get(player)) {
                        playerStateListener.onUpdate(player, event);
                    }
                }
            });
        }

        public void subscribe(String player) {
            admin.declareBinding(toBinding(player));
        }

        public void unsubscribe(String player) {
            admin.removeBinding(toBinding(player));
        }

        private Binding toBinding(String player) {
            return BindingBuilder.bind(queue).to(new TopicExchange("amq.topic")).with(player + postfix);
        }
    }

}
