package com.clemble.casino.server.spring.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.player.notification.RabbitSystemNotificationService;
import com.clemble.casino.server.player.notification.RabbitSystemNotificationServiceListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import({ RabbitSpringConfiguration.class })
public class SystemNotificationSpringConfiguration implements SpringConfiguration {

    final private static Logger LOG = LoggerFactory.getLogger(SpringConfiguration.class);

    @Bean
    public SystemNotificationService systemNotificationService(
        @Value("${clemble.service.notification.user}") String user,
        @Value("${clemble.service.notification.password}") String password,
        @Value("${clemble.service.notification.host}") String host,
        Jackson2JsonMessageConverter jsonMessageConverter) throws IOException {
        LOG.debug("Connecting player NotificationService with {0}", user);
        return new RabbitSystemNotificationService(host, user, password, jsonMessageConverter);
    }

    @Bean(destroyMethod = "close")
    public SystemNotificationServiceListener presenceListenerService(ObjectMapper mapper,
        @Value("${clemble.service.notification.user}") String user,
        @Value("${clemble.service.notification.password}") String password,
        @Value("${clemble.service.notification.host}") String host) {
        LOG.debug("Connecting player NotificationService with {0}", user);
        return new RabbitSystemNotificationServiceListener(host, user, password, mapper);
    }

}
