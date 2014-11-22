package com.clemble.casino.server.spring.common;

import com.clemble.casino.player.service.PlayerConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.clemble.casino.event.NotificationMapping.*;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.RabbitPlayerNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;

@Configuration
@Import({ JsonSpringConfiguration.class, ConnectionClientSpringConfiguration.class })
public class RabbitSpringConfiguration implements SpringConfiguration {

    final private static Logger LOG = LoggerFactory.getLogger(SpringConfiguration.class);

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper);
        return jsonMessageConverter;
    }

    @Bean
    public PlayerNotificationService playerNotificationService(
            @Value("${clemble.service.notification.user}") String user,
            @Value("${clemble.service.notification.password}") String password,
            @Value("${clemble.service.notification.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter,
            @Qualifier("playerConnectionClient") PlayerConnectionService connectionService) {
        LOG.debug("Connecting player NotificationService with {0}", user);
        return new RabbitPlayerNotificationService(PLAYER_CHANNEL_POSTFIX, jsonMessageConverter, host, user, password, connectionService);
    }

    @Bean
    public PlayerNotificationService playerPresenceNotificationService(
            @Value("${clemble.service.notification.user}") String user,
            @Value("${clemble.service.notification.password}") String password,
            @Value("${clemble.service.notification.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter,
            @Qualifier("playerConnectionClient") PlayerConnectionService connectionService) {
        LOG.debug("Connecting player presence NotificationService with {0}", user);
        return new RabbitPlayerNotificationService(PRESENCE_CHANNEL_POSTFIX, jsonMessageConverter, host, user, password, connectionService);
    }

}
