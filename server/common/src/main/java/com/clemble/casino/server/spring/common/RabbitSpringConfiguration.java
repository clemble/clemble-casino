package com.clemble.casino.server.spring.common;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.DNSBasedServerRegistry;
import com.clemble.casino.ServerRegistry;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.RabbitPlayerNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import({ JsonSpringConfiguration.class })
public class RabbitSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("objectMapper")
    public ObjectMapper objectMapper;

    public ServerRegistry playerNotificationRegistry(){
        return new DNSBasedServerRegistry(0, "127.0.0.1", "127.0.0.1", "127.0.0.1");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper);
        return jsonMessageConverter;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        ConnectionFactory connectionFactory = connectionFactory();
        // Step 1. Creating Queue
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(new Queue("SCDU", false, false, true));
        // Step 2. Creating RabbitTemplate for the Queue
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setQueue("SCDU");
        return rabbitTemplate;
    }

    @Bean
    public PlayerNotificationService playerNotificationService() {
        return new RabbitPlayerNotificationService(NotificationMapping.PLAYER_CHANNEL_POSTFIX, jsonMessageConverter(), playerNotificationRegistry());
    }

    @Bean
    public PlayerNotificationService playerPresenceNotificationService() {
        return new RabbitPlayerNotificationService(NotificationMapping.PRESENCE_CHANNEL_POSTFIX, jsonMessageConverter(), playerNotificationRegistry());
    }

}
