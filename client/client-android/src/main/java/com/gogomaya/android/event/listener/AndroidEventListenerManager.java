package com.gogomaya.android.event.listener;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.client.ImmutablePair;
import com.gogomaya.configuration.NotificationServerConfiguration;
import com.gogomaya.event.Event;
import com.gogomaya.event.listener.EventListener;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.event.listener.EventSelector;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class AndroidEventListenerManager implements EventListenersManager, Closeable {

    final protected Collection<Entry<EventSelector, EventListener>> eventListeners = new ArrayList<>();
    final protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    final public NotificationServerConfiguration configurations;
    final public ObjectMapper objectMapper;

    protected Connection connection;
    protected volatile boolean closed = false;

    public AndroidEventListenerManager(NotificationServerConfiguration configurations, ObjectMapper objectMapper) throws IOException {
        this.configurations = configurations;
        this.objectMapper = objectMapper;

        this.executor.submit(new StartupTask());
    }

    public void subscribe(EventListener listener) {
        subscribe(null, listener);
    }

    public void subscribe(EventSelector selector, EventListener listener) {
        eventListeners.add(new ImmutablePair<EventSelector, EventListener>(selector, listener));
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
                synchronized (AndroidEventListenerManager.this) {
                    // Step 1. Generalizing connection factory
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setUsername(configurations.getUser());
                    factory.setPassword(configurations.getPassword());
                    factory.setHost(configurations.getHost());
                    factory.setPort(configurations.getPort());
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
