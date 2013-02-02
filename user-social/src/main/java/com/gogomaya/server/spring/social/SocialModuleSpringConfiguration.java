package com.gogomaya.server.spring.social;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.messaging.CloudRabbitConnectionFactoryBean;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.social.SocialConnectionDataUpdateListener;
import com.gogomaya.server.social.SocialGamerProfileCreator;
import com.gogomaya.server.social.adapter.FacebookSocialAdapter;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration.CloudFoundryConfigurations;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration.DefaultConfigurations;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration.TestConfigurations;
import com.gogomaya.server.spring.user.UserModuleSpringConfiguration;
import com.gogomaya.server.user.GamerProfileRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Configuration
@Import(value = { UserModuleSpringConfiguration.class, CloudFoundryConfigurations.class, DefaultConfigurations.class, TestConfigurations.class })
public class SocialModuleSpringConfiguration {

    @Inject
    DataSource dataSource;

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    GamerProfileRepository gamerProfileRepository;

    @Bean
    @Singleton
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setMessageListener(new SocialConnectionDataUpdateListener(usersConnectionRepository(), connectionFactoryLocator()));
        simpleMessageListenerContainer.setQueueNames("SCDU");
        return simpleMessageListenerContainer;
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
    public SocialConnectionDataAdapter socialConnectionDataAdapter() {
        return new SocialConnectionDataAdapter(connectionFactoryLocator(), usersConnectionRepository(), rabbitTemplate());
    }

    @Bean
    @Singleton
    public ConnectionFactoryRegistry connectionFactoryLocator() {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory());
        return connectionFactoryRegistry;
    }

    @Bean
    @Singleton
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("", "");
        return facebookConnectionFactory;
    }

    @Bean
    @Singleton
    public FacebookSocialAdapter facebookSocialAdapter() {
        return new FacebookSocialAdapter();
    }

    @Bean
    @Singleton
    public ConnectionSignUp connectionSignUp() {
        return new SocialGamerProfileCreator(gamerProfileRepository);
    }

    @Bean
    @Singleton
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp());
        return repository;
    }

    @Configuration
    @Profile(value = "cloud")
    static class CloudFoundryConfigurations {
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
    static class DefaultConfigurations {

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

    }

    @Configuration
    @Profile(value = "test")
    static class TestConfigurations {

        @Bean
        @Singleton
        public ConnectionFactory connectionFactory() {
            return new CachingConnectionFactory();
        }

    }

}
