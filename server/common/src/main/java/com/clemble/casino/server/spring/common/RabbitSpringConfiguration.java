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

import com.clemble.casino.server.configuration.ServerRegistryServerService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.RabbitPlayerNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemble.casino.event.Event;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.player.PlayerPresence;

@Configuration
@Import({ ServerRegistrySpringConfiguration.class, JsonSpringConfiguration.class })
public class RabbitSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("objectMapper")
    public ObjectMapper objectMapper;

    @Autowired
    public ServerRegistryServerService serverRegistryService;

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
    public PlayerNotificationService<Event> playerNotificationService() {
        return new RabbitPlayerNotificationService<Event>(NotificationMapping.PLAYER_NOTIFICATION, jsonMessageConverter(), serverRegistryService);
    }

    @Bean
    public PlayerNotificationService<PlayerPresence> playerPresenceNotificationService() {
        return new RabbitPlayerNotificationService<PlayerPresence>(NotificationMapping.PLAYER_PRESENCE_NOTIFICATION, jsonMessageConverter(), serverRegistryService);
    }

}
