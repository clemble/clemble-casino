package com.clemble.casino.server.player.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RabbitSystemNotificationServiceListener implements SystemNotificationServiceListener {

    final private Logger LOG = LoggerFactory.getLogger(getClass());

    final private NotificationConfiguration notificationConfiguration;
    final private ObjectMapper objectMapper;

    final private Map<SystemEventListener<?>, RabbitStartupTask<?>> listenerToTask = new HashMap<SystemEventListener<?>, RabbitSystemNotificationServiceListener.RabbitStartupTask<?>>();

    public RabbitSystemNotificationServiceListener(NotificationConfiguration notificationConfiguration, ObjectMapper objectMapper) {
        this.notificationConfiguration = checkNotNull(notificationConfiguration);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void subscribe(SystemEventListener<? extends SystemEvent> eventListener) {
        // Step 1. Creating RabbitStartupTask
        RabbitStartupTask<?> startupTask = listenerToTask.put(eventListener, new RabbitStartupTask<>(notificationConfiguration, objectMapper, eventListener));
        if(startupTask != null)
            System.exit(1);
    }

    @Override
    public void unsubscribe(SystemEventListener<? extends SystemEvent> messageListener) {
        // Step 1. Closing registered rabbit startup task
        RabbitStartupTask<?> startupTask = listenerToTask.remove(messageListener);
        if(startupTask != null)
            startupTask.close();
    }

    @Override
    public void close(){
        for(RabbitStartupTask<?> startupTask: listenerToTask.values())
            startupTask.close();
    }

    public class RabbitStartupTask<T extends SystemEvent> implements Runnable, Closeable {

        final private Logger LOG = LoggerFactory.getLogger(RabbitStartupTask.class);

        final private NotificationConfiguration configurations;
        final private ObjectMapper objectMapper;
        final private ScheduledExecutorService executor;
        final private SystemEventListener<T> eventListener;

        final AtomicBoolean keepClosed = new AtomicBoolean(false);

        private Connection rabbitConnection;

        public RabbitStartupTask(NotificationConfiguration configuration, ObjectMapper objectMapper, SystemEventListener<T> eventListener) {
            this.configurations = checkNotNull(configuration);
            this.objectMapper = checkNotNull(objectMapper);
            this.eventListener = checkNotNull(eventListener);

            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("CL " + eventListener.getQueueName() + " %d").build();
            this.executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
            this.executor.execute(this);
        }

        @Override
        public void run() {
            try {
                synchronized (configurations) {
                    // Step 1. Generalizing connection factory
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setUsername(configurations.getUser());
                    factory.setPassword(configurations.getPassword());
                    factory.setHost(configurations.getRabbitHost().getHost());
                    factory.setPort(configurations.getRabbitHost().getPort());
                    LOG.debug("Created connection factory");
                    // Step 2. Creating connection
                    rabbitConnection = factory.newConnection(executor);
                    final AtomicBoolean keepClosed = new AtomicBoolean(false);
                    // Step 3. Creating new Channel
                    Channel channel = rabbitConnection.createChannel();
                    LOG.debug("Channel for RabbitConnection created");
                    channel.addShutdownListener(new ShutdownListener() {
                        @Override
                        public void shutdownCompleted(ShutdownSignalException cause) {
                            if (!keepClosed.get()) {
                                LOG.error("KeepClosed flag not specified, restarting", cause);
                                executor.schedule(RabbitStartupTask.this, 30, TimeUnit.SECONDS);
                            }
                        }
                    });
                    // Step 4. Creating new Queue that will be used for listening of player updates
                    AMQP.Queue.DeclareOk declare = channel.queueDeclare(eventListener.getQueueName(), true, false, false, null);
                    LOG.debug("Queue declared {}", declare);
                    String basicConsume = channel.basicConsume(eventListener.getQueueName(), true, new SystemEventListenerAdapter<T>(eventListener, objectMapper));
                    LOG.debug("Basic consume {}", basicConsume);
                    AMQP.Queue.BindOk bind = channel.queueBind(eventListener.getQueueName(), SystemEventListener.EXCHANGE, eventListener.getChannel());
                    LOG.debug("Bind {} with channel {}", bind, eventListener.getChannel());
                }
            } catch (Throwable e) {
                LOG.error("Failed to start Rabbit listener", e);
                executor.schedule(RabbitStartupTask.this, 30, TimeUnit.SECONDS);
            }
        }

        public void close() {
            try {
                keepClosed.set(true);
                rabbitConnection.close();
            } catch (ShutdownSignalException exception) {
                LOG.error("Failure to close listener ShutdownSignalException", exception);
            } catch (IOException ioException) {
                LOG.error("Failure to close IOException", ioException);
            }
        }

    }

    final private static class SystemEventListenerAdapter<T extends SystemEvent> implements Consumer {

        final private Logger LOG;
        
        final private ObjectMapper objectMapper;
        final private SystemEventListener<T> eventListener;

        public SystemEventListenerAdapter(SystemEventListener<T> eventListener, ObjectMapper objectMapper) {
            this.eventListener = checkNotNull(eventListener);
            this.objectMapper = checkNotNull(objectMapper);

            LOG = LoggerFactory.getLogger(eventListener.getClass());
        }

        @Override
        public void handleConsumeOk(String consumerTag) {
            LOG.debug("consume OK {}", consumerTag);
        }

        @Override
        public void handleCancelOk(String consumerTag) {
            LOG.debug("cancel OK {}", consumerTag);
        }

        @Override
        public void handleCancel(String consumerTag) throws IOException {
            LOG.debug("cancel {}", consumerTag);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
            LOG.debug("Processing {} from {} with routing key {}", consumerTag, envelope.getExchange(), envelope.getRoutingKey());
            // Step 1. Safely reading event
            T event = null;
            try {
                event = (T) objectMapper.readValue(body, SystemEvent.class);
            } catch(IOException ioe) {
                LOG.error("Failed to parse message {}", new String(body));
                LOG.error("FIX_ASAP Ignoring message", ioe);
            }
            // Step 2. If we were able to read event notify, otherwise ignore, some error happened
            if(event != null) {
                LOG.debug("Extracted event {}", event);
                eventListener.onEvent(event);
            } else {
                LOG.error("Failed to parse message {}", new String(body));
            }
            LOG.debug("Processing finished {}", consumerTag);
        }

        @Override
        public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
            LOG.debug("shutdown {} / {}", consumerTag, sig);
        }

        @Override
        public void handleRecoverOk(String consumerTag) {
            LOG.debug("recover OK {}", consumerTag);
        }

    }
}
