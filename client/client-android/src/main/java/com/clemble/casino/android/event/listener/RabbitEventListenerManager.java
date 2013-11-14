package com.clemble.casino.android.event.listener;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerController;
import com.clemble.casino.client.event.EventListenersManager;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.SessionEventSelector;
import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitEventListenerManager implements EventListenersManager, Closeable {

    final protected Collection<Entry<EventSelector, EventListener>> eventListeners = new ArrayList<>();
    final protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    final public NotificationConfiguration configurations;
    final public ObjectMapper objectMapper;

    protected Connection connection;
    protected volatile boolean closed = false;

    public RabbitEventListenerManager(NotificationConfiguration configurations, ObjectMapper objectMapper) throws IOException {
        this.configurations = configurations;
        this.objectMapper = objectMapper;

        this.executor.submit(new StartupTask());
    }

    @Override
    public EventListenerController subscribe(EventListener listener) {
        return subscribe((EventSelector) null, listener);
    }

    @Override
    public EventListenerController subscribe(GameSessionKey sessionKey, EventListener listener) {
        return subscribe(new SessionEventSelector(sessionKey), listener);
    }

    @Override
    public EventListenerController subscribe(EventSelector selector, EventListener listener) {
        final ImmutablePair<EventSelector, EventListener> listenerPair = new ImmutablePair<EventSelector, EventListener>(selector, listener);
        eventListeners.add(listenerPair);
        return new EventListenerController() {
            @Override
            public void close() {
                eventListeners.remove(listenerPair);
            }
        };
    }

    @Override
    public synchronized void close() throws IOException {
        if (closed)
            return;
        closed = true;

        if (connection != null)
            connection.close();
        executor.shutdown();
    }

    final public class StartupTask implements Runnable {

        @Override
        public void run() {
            if (closed)
                return;
            try {
                synchronized (RabbitEventListenerManager.this) {
                    // Step 1. Generalizing connection factory
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setUsername(configurations.getUser());
                    factory.setPassword(configurations.getPassword());
                    factory.setHost(configurations.getRabbitHost().getHost());
                    factory.setPort(configurations.getRabbitHost().getPort());
                    // Step 2. Creating connection
                    connection = factory.newConnection(executor);
                    // Step 3. Creating new Channel
                    Channel channel = connection.createChannel();
                    // Step 4. Creating new Queue that will be used for listening of player updates
                    Queue.DeclareOk queue = channel.queueDeclare();
                    channel.queueBind(queue.getQueue(), "amq/topic", configurations.getRoutingKey());
                    channel.basicConsume(queue.getQueue(), new AndroidRabbitDefaultConsumer(channel));
                }
            } catch (Throwable e) {
                e.printStackTrace();
                executor.schedule(new StartupTask(), 5, TimeUnit.SECONDS);
            }
        }

    }

    final public class AndroidRabbitDefaultConsumer extends DefaultConsumer {

        public AndroidRabbitDefaultConsumer(Channel channel) {
            super(channel);
        }

        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            Event event = objectMapper.readValue(body, Event.class);
            for (Entry<EventSelector, EventListener> listener : eventListeners) {
                EventSelector selector = listener.getKey();
                if (selector == null || selector.filter(event))
                    listener.getValue().onEvent(event);
            }
        }
    }

}
