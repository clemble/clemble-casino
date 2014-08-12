package com.clemble.casino.server.player.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RabbitSystemNotificationServiceListener implements SystemNotificationServiceListener {

    final private Logger LOG = LoggerFactory.getLogger(getClass());

    final private String notificationConfiguration;
    final private ObjectMapper objectMapper;

    final private Map<SystemEventListener<?>, RabbitStartupTask<?>> listenerToTask = new HashMap<SystemEventListener<?>, RabbitSystemNotificationServiceListener.RabbitStartupTask<?>>();

    public RabbitSystemNotificationServiceListener(String notificationConfiguration, ObjectMapper objectMapper) {
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

        final private String configurations;
        final private ObjectMapper objectMapper;
        final private ScheduledExecutorService executor;
        final private SystemEventListener<T> eventListener;

        final AtomicBoolean keepClosed = new AtomicBoolean(false);
        final private AtomicReference<Connection> rabbitConnection = new AtomicReference<>(null);

        public RabbitStartupTask(String configuration, ObjectMapper objectMapper, SystemEventListener<T> eventListener) {
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
                    factory.setUsername("guest");
                    factory.setPassword("guest");
                    factory.setHost(configurations);
                    factory.setPort(5672);
                    LOG.debug("Created connection factory");
                    // Step 2. Creating connection
                    rabbitConnection.set(factory.newConnection(executor));
                    final AtomicBoolean keepClosed = new AtomicBoolean(false);
                    // Step 3. Creating new Channel
                    Channel channel = rabbitConnection.get().createChannel();
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
                if(rabbitConnection.get() != null) {
                    Connection connection = rabbitConnection.get();
                    if(connection.isOpen())
                        connection.close();
                } else {
                    LOG.error("Failed to start {} {}", eventListener.getQueueName(), eventListener.getChannel());
                }
            } catch (ShutdownSignalException exception) {
                LOG.error("Failure to close listener ShutdownSignalException", exception);
                LOG.error("Exception source {}", exception.getReference());
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
                try {
                    eventListener.onEvent(event);
                } catch (Throwable throwable) {
                    LOG.error("Failed to process message", throwable);
                }
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
