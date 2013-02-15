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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Configuration
@Import({RabbitModuleSpringConfigurations.RabbitModuleCloudFoundryConfigurations.class, RabbitModuleSpringConfigurations.RabbitModuleDefaultConfigurations.class, RabbitModuleSpringConfigurations.RabbitModuleTestConfigurations.class})
public class RabbitModuleSpringConfigurations {
    
    @Inject
    ConnectionFactory connectionFactory;

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

    @Configuration
    @Profile(value = "cloud")
    static class RabbitModuleCloudFoundryConfigurations {
        @Inject
        CloudEnvironment cloudEnvironment;

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

    }

    @Configuration
    @Profile(value = "default")
    static class RabbitModuleDefaultConfigurations {

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

    }

    @Configuration
    @Profile(value = "test")
    static class RabbitModuleTestConfigurations {

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

    }

}
