package com.clemble.casino.server.spring.common;

import java.io.IOException;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

@Configuration
@Import({ PlayerPresenceSpringConfiguration.class, RabbitSpringConfiguration.class })
public class SystemNotificationSpringConfiguration implements SpringConfiguration {

    @Bean
    public NotificationConfiguration systemNotificationConfiguration(){
        NotificationHost rabbitHost = new NotificationHost("127.0.0.1", 5672);
        NotificationHost stompHost = new NotificationHost("127.0.0.1", 61613);
        NotificationHost sockjsHost = new NotificationHost("127.0.0.1", 15674);
        return new NotificationConfiguration("guest", "guest", "", rabbitHost, stompHost, sockjsHost);
    }

    @Bean
    public SystemNotificationService systemNotificationService(Jackson2JsonMessageConverter jsonMessageConverter) throws IOException {
        return new RabbitSystemNotificationService("127.0.0.1", jsonMessageConverter);
    }

    @Bean(destroyMethod = "close")
    public SystemNotificationServiceListener presenceListenerService(ObjectMapper mapper, NotificationConfiguration systemNotificationConfiguration) {
        return new RabbitSystemNotificationServiceListener(systemNotificationConfiguration, mapper);
    }
    // In case using Redis
    // return new JedisSystemNoficiationServiceListener(jedisPool, mapper);

}
