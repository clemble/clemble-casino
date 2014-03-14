package com.clemble.casino.server.spring.common;

import java.io.IOException;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.configuration.NotificationHost;
import com.clemble.casino.server.player.presence.RabbitSystemNotificationService;
import com.clemble.casino.server.player.presence.RabbitSystemNotificationServiceListener;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({ PlayerPresenceSpringConfiguration.class, RabbitSpringConfiguration.class })
public class SystemNotificationSpringConfiguration implements SpringConfiguration {

    @Bean
    public NotificationConfiguration systemNotificationConfiguration(
            @Value("${clemble.service.notification.host}") String host,
            @Value("${clemble.service.notification.rabbit.port}") int rabbitPort,
            @Value("${clemble.service.notification.stomp.port}") int stompPort,
            @Value("${clemble.service.notification.sockjs.port}") int sockjsPort
    ){
        NotificationHost rabbitHost = new NotificationHost(host, rabbitPort);
        NotificationHost stompHost = new NotificationHost(host, stompPort);
        NotificationHost sockjsHost = new NotificationHost(host, sockjsPort);
        return new NotificationConfiguration("guest", "guest", "", rabbitHost, stompHost, sockjsHost);
    }

    @Bean
    public SystemNotificationService systemNotificationService(
        @Value("${clemble.service.notification.host}") String host,
        Jackson2JsonMessageConverter jsonMessageConverter) throws IOException {
        return new RabbitSystemNotificationService(host, jsonMessageConverter);
    }

    @Bean(destroyMethod = "close")
    public SystemNotificationServiceListener presenceListenerService(ObjectMapper mapper, NotificationConfiguration systemNotificationConfiguration) {
        return new RabbitSystemNotificationServiceListener(systemNotificationConfiguration, mapper);
    }

}
