package com.clemble.casino.client.event;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.event.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitEventListenerTemplate extends AbstractEventListenerTemplate {

    public RabbitEventListenerTemplate(NotificationConfiguration configurations, ObjectMapper objectMapper) {
        this.executor.submit(new StartupTask(configurations, objectMapper));
    }

    final public class StartupTask implements Runnable {

        final public NotificationConfiguration configurations;
        final public ObjectMapper objectMapper;

        public StartupTask(NotificationConfiguration configuration, ObjectMapper objectMapper) {
            this.configurations = checkNotNull(configuration);
            this.objectMapper = checkNotNull(objectMapper);
        }

        @Override
        public void run() {
            try {
                synchronized (RabbitEventListenerTemplate.this) {
                    // Step 1. Generalizing connection factory
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setUsername(configurations.getUser());
                    factory.setPassword(configurations.getPassword());
                    factory.setHost(configurations.getRabbitHost().getHost());
                    factory.setPort(configurations.getRabbitHost().getPort());
                    // Step 2. Creating connection
                    final Connection rabbitConnection = factory.newConnection(executor);
                    // Step 3. Creating new Channel
                    Channel channel = rabbitConnection.createChannel();
                    // Step 4. Creating new Queue that will be used for listening of player updates
                    Queue.DeclareOk queue = channel.queueDeclare();
                    channel.queueBind(queue.getQueue(), "amq/topic", configurations.getRoutingKey());
                    channel.basicConsume(queue.getQueue(), new AndroidRabbitDefaultConsumer(channel, objectMapper));
                    // Step 5. Updating Closeable in the parent
                    connectionCleaner.set(new Closeable() {
                        
                        @Override
                        public void close() throws IOException {
                            rabbitConnection.close();
                        }
                    });
                }
            } catch (Throwable e) {
                // TODO add error processing
                executor.schedule(new StartupTask(configurations, objectMapper), 30, TimeUnit.SECONDS);
            }
        }

    }

    final public class AndroidRabbitDefaultConsumer extends DefaultConsumer {

        final private ObjectMapper objectMapper;

        public AndroidRabbitDefaultConsumer(Channel channel, ObjectMapper objectMapper) {
            super(channel);
            this.objectMapper = objectMapper;
        }

        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            update(objectMapper.readValue(body, Event.class));
        }
    }

}
