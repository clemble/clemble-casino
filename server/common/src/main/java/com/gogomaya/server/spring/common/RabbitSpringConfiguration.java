package com.gogomaya.server.spring.common;

import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RabbitServiceInfo;
import org.cloudfoundry.runtime.service.messaging.RabbitServiceCreator;
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
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.ServerRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.notification.RabbitPlayerNotificationService;
import com.gogomaya.server.player.notification.SimplePlayerNotificationRegistry;

@Configuration
@Import({ RabbitSpringConfiguration.Cloud.class, RabbitSpringConfiguration.DefaultAndTest.class, JsonSpringConfiguration.class })
public class RabbitSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("connectionFactory")
    public ConnectionFactory connectionFactory;

    @Autowired
    @Qualifier("objectMapper")
    public ObjectMapper objectMapper;

    @Autowired
    @Qualifier("playerNotificationRegistry")
    public PlayerNotificationRegistry playerNotificationRegistry;

    @Bean
    @Singleton
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper);
        return jsonMessageConverter;
    }

    @Bean
    @Singleton
    public RabbitTemplate rabbitTemplate() {
        // Step 1. Creating Queue
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(new Queue("SCDU", false, false, true));
        // Step 2. Creating RabbitTemplate for the Queue
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setQueue("SCDU");
        return rabbitTemplate;
    }

    @Bean
    @Singleton
    public PlayerNotificationService playerNotificationService() {
        return new RabbitPlayerNotificationService(jsonMessageConverter(), playerNotificationRegistry);
    }

    @Configuration
    @Profile(value = CLOUD)
    public static class Cloud {

        @Autowired
        public CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            try {
                RabbitServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("gogomaya-rabbit", RabbitServiceInfo.class);
                RabbitServiceCreator serviceCreator = new RabbitServiceCreator();
                ConnectionFactory connectionFactory = serviceCreator.createService(serviceInfo);
                if (connectionFactory == null)
                    throw new NullPointerException("Rabbit Connection factory can't be created");
                return connectionFactory;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Configuration
    @Profile(value = { CLOUD })
    public static class Development {

        @Bean
        @Singleton
        public SimplePlayerNotificationRegistry playerNotificationRegistry() {
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "ec2-50-16-93-157.compute-1.amazonaws.com");
            return new SimplePlayerNotificationRegistry(serverRegistry);
        }

    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

        @Bean
        @Singleton
        public SimplePlayerNotificationRegistry playerNotificationRegistry() {
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "localhost");
            return new SimplePlayerNotificationRegistry(serverRegistry);
        }

    }

}
