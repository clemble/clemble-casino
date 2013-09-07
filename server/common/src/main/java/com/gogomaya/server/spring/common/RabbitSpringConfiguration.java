package com.gogomaya.server.spring.common;

import javax.inject.Singleton;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.ServerRegistry;
import com.gogomaya.server.configuration.RestServerRegistryService;
import com.gogomaya.server.configuration.ServerRegistryService;
import com.gogomaya.server.player.notification.PaymentEndpointRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.notification.RabbitPlayerNotificationService;
import com.gogomaya.server.player.notification.SimplePlayerNotificationRegistry;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ RabbitSpringConfiguration.Default.class, RabbitSpringConfiguration.Cloud.class, RabbitSpringConfiguration.IntegrationTest.class,
        RabbitSpringConfiguration.Test.class, JsonSpringConfiguration.class })
public class RabbitSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("connectionFactory")
    public ConnectionFactory connectionFactory;

    @Autowired
    @Qualifier("objectMapper")
    public ObjectMapper objectMapper;

    @Autowired
    @Qualifier("serverRegistryService")
    public ServerRegistryService serverRegistryService;

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
        return new RabbitPlayerNotificationService(jsonMessageConverter(), serverRegistryService);
    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { CLOUD })
    public static class Cloud {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        public ServerRegistryService serverRegistryService() {
            return new RestServerRegistryService("http://ec2-50-16-93-157.compute-1.amazonaws.com/management-web/", restTemplate);
        }

    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { INTEGRATION_TEST })
    public static class IntegrationTest {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

        @Bean
        public ServerRegistryService serverRegistryService() {
            return new RestServerRegistryService("http://localhost:9999/management-web/", restTemplate);
        }

    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { DEFAULT })
    public static class Default {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

        @Autowired(required = false)
        @Qualifier("serverRegistryService")
        public ServerRegistryService serverRegistryService;

        @Bean
        public ServerRegistryService serverRegistryService() {
            return serverRegistryService != null ? null : new RestServerRegistryService("http://localhost:8080/management-web/", restTemplate);
        }

    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { UNIT_TEST })
    public static class Test {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

        @Autowired(required = false)
        @Qualifier("serverRegistryService")
        public ServerRegistryService serverRegistryService;

        @Bean
        public ServerRegistryService serverRegistryService() {
            return serverRegistryService != null ? null : new ServerRegistryService() {

                @Override
                public PlayerNotificationRegistry getPlayerNotificationRegistry() {
                    ServerRegistry serverRegistry = new ServerRegistry();
                    serverRegistry.register(1_000_000L, "http://localhost/");
                    return new SimplePlayerNotificationRegistry(serverRegistry);
                }

                @Override
                public PaymentEndpointRegistry getPaymentEndpointRegistry() {
                    return new PaymentEndpointRegistry("http://localhost:8080/payment-web/");
                }
            };
        }

    }

}
