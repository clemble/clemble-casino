package com.gogomaya.server.spring.common;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.messaging.CloudRabbitConnectionFactoryBean;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Configuration
@Import({ RabbitSpringConfiguration.Cloud.class, RabbitSpringConfiguration.DefaultAndTest.class, JsonSpringConfiguration.class })
public class RabbitSpringConfiguration {

    @Inject
    public ConnectionFactory connectionFactory;

    @Inject
    public ObjectMapper objectMapper;

    @Inject
    public PlayerNotificationRegistry notificationRegistry;

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
    public PlayerNotificationService gameNotificationManager() {
        return new RabbitPlayerNotificationService(jsonMessageConverter(), notificationRegistry);
    }

    @Configuration
    @Profile(value = "cloud")
    public static class Cloud {
        @Inject
        public CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            CloudRabbitConnectionFactoryBean cloudRabbitFactory = new CloudRabbitConnectionFactoryBean(cloudEnvironment);
            try {
                Collection<ServiceNameTuple<ConnectionFactory>> connectionFactories = cloudRabbitFactory.createInstances();
                connectionFactories = Collections2.filter(connectionFactories, new Predicate<ServiceNameTuple<ConnectionFactory>>() {
                    public boolean apply(ServiceNameTuple<ConnectionFactory> input) {
                        return input.name.equalsIgnoreCase("gogomaya-rabbit");
                    }
                });
                assert connectionFactories.size() == 1 : "Returned illegal ConnectionFactory";
                return connectionFactories.iterator().next().service;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Bean
        @Singleton
        public SimplePlayerNotificationRegistry playerNotificationRegistry() {
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "ec2-50-16-93-157.compute-1.amazonaws.com");
            return new SimplePlayerNotificationRegistry(serverRegistry);
        }

    }

    @Configuration
    @Profile(value = { "default", "test" })
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
