package com.gogomaya.server.spring.integration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.game.IntegrationGameSessionPlayerFactory;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.payment.IntegrationPaymentTransactionOperations;
import com.gogomaya.server.integration.payment.PaymentTransactionOperations;
import com.gogomaya.server.integration.payment.WebPaymentTransactionOperations;
import com.gogomaya.server.integration.player.IntegrationPlayerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.WebPlayerOperations;
import com.gogomaya.server.integration.player.account.AccountOperations;
import com.gogomaya.server.integration.player.account.IntegrationAccountOperations;
import com.gogomaya.server.integration.player.account.WebAccountOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.listener.SimplePlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.IntegrationProfileOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.profile.WebProfileOperations;
import com.gogomaya.server.integration.player.session.IntegrationSessionOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.integration.player.session.WebSessionOperations;
import com.gogomaya.server.spring.common.JsonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;
import com.gogomaya.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.gogomaya.server.spring.web.player.PlayerWebSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaRESTErrorHandler;
import com.gogomaya.server.web.payment.PaymentTransactionController;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.PlayerSessionController;
import com.gogomaya.server.web.player.account.PlayerAccountController;
import com.gogomaya.server.web.player.registration.PlayerRegistrationController;

@Configuration
@Import(value = { JsonSpringConfiguration.class,
        TestConfiguration.LocalTestConfiguration.class,
        TestConfiguration.LocalIntegrationTestConfiguration.class,
        TestConfiguration.RemoteIntegrationTestConfiguration.class })
public class TestConfiguration {

    @Inject
    @Named("playerOperations")
    public PlayerOperations playerOperations;

    @Bean
    @Singleton
    public GameScenarios gameScenarios() {
        return new GameScenarios(playerOperations);
    }

    @Configuration
    @Profile(value = SpringConfiguration.DEFAULT)
    @Import(value = { PaymentWebSpringConfiguration.class, PlayerWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Inject
        @Named("objectMapper")
        public ObjectMapper objectMapper;

        @Inject
        @Named("registrationLoginController")
        public PlayerRegistrationController registrationLoginController;

        @Inject
        @Named("playerSessionController")
        public PlayerSessionController playerSessionController;

        @Inject
        @Named("playerAccountController")
        public PlayerAccountController playerAccountController;

        @Inject
        public PlayerProfileController playerProfileController;

        @Inject
        public PaymentTransactionController paymentTransactionController;

        @Bean
        @Singleton
        public PlayerListenerOperations playerListenerOperations() {
            return new SimplePlayerListenerOperations(objectMapper);
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new WebPlayerOperations(registrationLoginController, sessionOperations(), accountOperations(),
                    playerListenerOperations(), playerProfileOperations());
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new WebAccountOperations(paymentTransactionController, playerAccountController);
        }

        @Bean
        @Singleton
        public SessionOperations sessionOperations() {
            return new WebSessionOperations(playerSessionController);
        }

        @Bean
        @Singleton
        public ProfileOperations playerProfileOperations() {
            return new WebProfileOperations(playerProfileController);
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_CLOUD)
    public static class RemoteIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/gogomaya/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_TEST)
    public static class LocalIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_DEFAULT)
    public static class LocalServerIntegrationTestConfiguration extends IntegrationTestConfiguration {

    }

    @Import(ClientRestCommonSpringConfiguration.class)
    public static class IntegrationTestConfiguration {

        @Inject
        @Named("objectMapper")
        public ObjectMapper objectMapper;

        public String getBaseUrl() {
            return "http://localhost:8080/";
        }

        @Bean
        @Singleton
        public PlayerListenerOperations playerListenerOperations() {
            return new SimplePlayerListenerOperations(objectMapper);
        }

        @Bean
        @Singleton
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                    ((MappingJackson2HttpMessageConverter) messageConverter).setObjectMapper(objectMapper);
                }
            }

            restTemplate.setErrorHandler(new GogomayaRESTErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new IntegrationAccountOperations(restTemplate(), getBaseUrl());
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new IntegrationPlayerOperations(getBaseUrl(), restTemplate(), playerListenerOperations(), playerProfileOperations(), sessionOperations(),
                    accountOperations());
        }

        @Bean
        @Singleton
        public SessionOperations sessionOperations() {
            return new IntegrationSessionOperations(restTemplate(), getBaseUrl());
        }

        @Bean
        @Singleton
        public ProfileOperations playerProfileOperations() {
            return new IntegrationProfileOperations(restTemplate(), getBaseUrl());
        }

        @Bean
        @Singleton
        public GameSessionPlayerFactory<?> genericGameSessionFactory() {
            return new IntegrationGameSessionPlayerFactory<>(restTemplate(), getBaseUrl());
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new IntegrationPaymentTransactionOperations(restTemplate(), getBaseUrl());
        }

    }
}
