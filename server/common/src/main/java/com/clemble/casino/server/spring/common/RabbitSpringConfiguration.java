package com.clemble.casino.server.spring.common;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.clemble.casino.event.NotificationMapping.*;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.RabbitServerNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public ServerNotificationService playerNotificationService(
            @Value("${clemble.service.notification.player.user}") String user,
            @Value("${clemble.service.notification.player.password}") String password,
            @Value("${clemble.service.notification.player.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter,
            SystemNotificationService systemNotificationService) {
        LOG.debug("Connecting player NotificationService with {0}", user);
        return new RabbitServerNotificationService(PLAYER_CHANNEL_POSTFIX, jsonMessageConverter, host, user, password, systemNotificationService);
    }

    @Bean
    public ServerNotificationService playerPresenceNotificationService(
            @Value("${clemble.service.notification.player.user}") String user,
            @Value("${clemble.service.notification.player.password}") String password,
            @Value("${clemble.service.notification.player.host}") String host,
            Jackson2JsonMessageConverter jsonMessageConverter,
            SystemNotificationService systemNotificationService) {
        LOG.debug("Connecting player presence NotificationService with {0}", user);
        return new RabbitServerNotificationService(PRESENCE_CHANNEL_POSTFIX, jsonMessageConverter, host, user, password, systemNotificationService);
    }

}
