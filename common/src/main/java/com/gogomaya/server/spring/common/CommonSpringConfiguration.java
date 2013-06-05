package com.gogomaya.server.spring.common;

import javax.inject.Singleton;
import javax.validation.Validation;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.notification.ServerRegistry;
import com.gogomaya.server.player.notification.SimplePlayerNotificationRegistry;

@Configuration
@Import({ CommonSpringConfiguration.Cloud.class,
        CommonSpringConfiguration.DefaultAndTest.class,
        RabbitSpringConfiguration.class,
        JPASpringConfiguration.class,
        RedisSpringConfiguration.class })
public class CommonSpringConfiguration {

    @Bean
    @Singleton
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @Bean
    @Singleton
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper());
        return jsonMessageConverter;
    }

    @Bean
    @Singleton
    public GogomayaValidationService validationService() {
        return new GogomayaValidationService(Validation.buildDefaultValidatorFactory());
    }

    @Configuration
    @Profile(value = { "default", "test" })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public SimplePlayerNotificationRegistry playerNotificationRegistry() {
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "localhost");
            return new SimplePlayerNotificationRegistry(serverRegistry);
        }

    }

    @Configuration
    @Profile(value = "cloud")
    public static class Cloud {

        @Bean
        @Singleton
        public SimplePlayerNotificationRegistry playerNotificationRegistry() {
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "ec2-50-16-93-157.compute-1.amazonaws.com");
            return new SimplePlayerNotificationRegistry(serverRegistry);
        }

        @Bean
        @Singleton
        public CloudEnvironment cloudEnvironment() {
            return new CloudEnvironment();
        }

    }

}
