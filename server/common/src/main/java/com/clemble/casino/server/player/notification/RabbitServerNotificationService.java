package com.clemble.casino.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

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
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RabbitServerNotificationService extends AbstractServerNotificationService {

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

    final private static Logger LOG = LoggerFactory.getLogger(RabbitServerNotificationService.class);

    final private String postfix;
    final private MessageConverter messageConverter;
    final private String host;
    final private String user;
    final private String password;

    final private ExecutorService executorService;

    public RabbitServerNotificationService(
        final String postfix,
        final MessageConverter messageConverter,
        final String host,
        final String user,
        final String password,
        final SystemNotificationService systemNotificationService) {
        super(systemNotificationService);
        this.postfix = checkNotNull(postfix);
        this.messageConverter = checkNotNull(messageConverter);
        this.host = host;
        this.user = user;
        this.password = password;

        ThreadFactory notificationThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("CL playerNotification with '" + postfix + "' %d")
            .build();
        this.executorService = Executors.newFixedThreadPool(2, notificationThreadFactory);
    }

    protected <T extends Event> boolean doSend(final String player, final T event) {
        Message message = messageConverter.toMessage(event, null);
        try {
            RabbitTemplate rabbitTemplate = RABBIT_CACHE.get(host);
            rabbitTemplate.send(String.valueOf(player) + postfix, message);
        } catch (Throwable e) {
            LOG.trace("Failed notification of {} with {}", player, message);
            return false;
        }
        return true;
    }

}
