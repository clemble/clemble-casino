package com.clemble.casino.server.spring.common;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper);
        return jsonMessageConverter;
    }

    @Bean
    public PlayerNotificationService playerNotificationService(
            @Value("${clemble.service.notification.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter) {
        return new RabbitPlayerNotificationService(NotificationMapping.PLAYER_CHANNEL_POSTFIX, jsonMessageConverter, new DNSBasedServerRegistry(host));
    }

    @Bean
    public PlayerNotificationService playerPresenceNotificationService(
            @Value("${clemble.service.notification.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter) {
        return new RabbitPlayerNotificationService(NotificationMapping.PRESENCE_CHANNEL_POSTFIX, jsonMessageConverter, new DNSBasedServerRegistry(host));
    }

}
