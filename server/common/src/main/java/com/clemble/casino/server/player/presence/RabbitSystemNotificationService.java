package com.clemble.casino.server.player.presence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RabbitSystemNotificationService implements SystemNotificationService {

    final private Logger LOG = LoggerFactory.getLogger(RabbitSystemNotificationService.class);

    final private RabbitTemplate rabbitTemplate;

    public RabbitSystemNotificationService(String server, MessageConverter messageConverter) {
        // Step 1. Configuring executor service
        ThreadFactory notificationThreadFactory = new ThreadFactoryBuilder().setNameFormat("CL systemNotification %d").build();
        ExecutorService executorService = Executors.newFixedThreadPool(1, notificationThreadFactory);
        // Step 2. Creating caching connection factory
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(server);
        connectionFactory.setExecutor(executorService);
        // Step 3. Creating rabbit template
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate.setExchange(SystemEventListener.EXCHANGE);
        // Step 4. Ensuring exchange exists
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareExchange(new TopicExchange(SystemEventListener.EXCHANGE, true, false, null));
    }

    @Override
    public void notify(SystemEvent event) {
        LOG.debug("Notifying {} of {}", event.getChannel(), event);
        rabbitTemplate.convertAndSend(event.getChannel(), event);
    }
}
