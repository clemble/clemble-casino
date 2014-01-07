package com.clemble.casino.server.spring.common;

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

    @Bean
    public ServerRegistry playerNotificationRegistry() {
        // TODO configurations
        return new DNSBasedServerRegistry(0, "127.0.0.1", "127.0.0.1", "127.0.0.1");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper);
        return jsonMessageConverter;
    }

    @Bean
    public PlayerNotificationService playerNotificationService(ServerRegistry playerNotificationRegistry, Jackson2JsonMessageConverter jsonMessageConverter) {
        return new RabbitPlayerNotificationService(NotificationMapping.PLAYER_CHANNEL_POSTFIX, jsonMessageConverter, playerNotificationRegistry);
    }

    @Bean
    public PlayerNotificationService playerPresenceNotificationService(ServerRegistry playerNotificationRegistry,
            Jackson2JsonMessageConverter jsonMessageConverter) {
        return new RabbitPlayerNotificationService(NotificationMapping.PRESENCE_CHANNEL_POSTFIX, jsonMessageConverter, playerNotificationRegistry);
    }

}
