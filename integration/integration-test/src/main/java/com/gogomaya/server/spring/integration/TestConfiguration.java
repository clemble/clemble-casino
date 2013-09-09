package com.gogomaya.server.spring.integration;

import static com.gogomaya.server.spring.common.SpringConfiguration.INTEGRATION_CLOUD;
import static com.gogomaya.server.spring.common.SpringConfiguration.INTEGRATION_DEFAULT;
import static com.gogomaya.server.spring.common.SpringConfiguration.INTEGRATION_TEST;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import com.gogomaya.server.spring.web.management.ManagementWebSpringConfiguration;
import com.gogomaya.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.gogomaya.server.spring.web.player.PlayerWebSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaRestErrorHandler;
import com.gogomaya.server.web.management.PlayerRegistrationController;
import com.gogomaya.server.web.management.PlayerSessionController;
import com.gogomaya.server.web.payment.PaymentTransactionController;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.account.PlayerAccountController;

@Configuration
@Import(value = { JsonSpringConfiguration.class,
        TestConfiguration.LocalTestConfiguration.class,
        TestConfiguration.IntegrationTestConfiguration.class})
public class TestConfiguration {

    @Autowired
    @Qualifier("playerOperations")
    public PlayerOperations playerOperations;

    @Bean
    @Singleton
    public GameScenarios gameScenarios() {
        return new GameScenarios(playerOperations);
    }

    @Configuration
    @Profile(value = SpringConfiguration.DEFAULT)
    @Import(value = { PaymentWebSpringConfiguration.class, PlayerWebSpringConfiguration.class, ManagementWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Autowired
        @Qualifier("playerRegistrationController")
        public PlayerRegistrationController playerRegistrationController;

        @Autowired
        @Qualifier("playerSessionController")
        public PlayerSessionController playerSessionController;

        @Autowired
        @Qualifier("playerAccountController")
        public PlayerAccountController playerAccountController;

        @Autowired
        public PlayerProfileController playerProfileController;

        @Autowired
        public PaymentTransactionController paymentTransactionController;

        @Bean
        @Singleton
        public PlayerListenerOperations playerListenerOperations() {
            return new SimplePlayerListenerOperations(objectMapper);
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new WebPlayerOperations(playerRegistrationController, sessionOperations(), accountOperations(),
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
    @Profile({INTEGRATION_TEST, INTEGRATION_CLOUD, INTEGRATION_DEFAULT})
    @Import(ClientRestCommonSpringConfiguration.class)
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Value("#{systemProperties['gogomaya.management.url'] ?: 'http://localhost:8080/picpacpoe/'}")
        public String baseUrl;

        public String getBaseUrl(){
            String base = baseUrl.substring(0, baseUrl.substring(0, baseUrl.length() - 1).lastIndexOf("/") + 1);
            return base;
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

            restTemplate.setErrorHandler(new GogomayaRestErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new IntegrationAccountOperations(restTemplate());
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new IntegrationPlayerOperations(baseUrl, restTemplate(), playerListenerOperations(), playerProfileOperations(), sessionOperations(),
                    accountOperations());
        }

        @Bean
        @Singleton
        public SessionOperations sessionOperations() {
            return new IntegrationSessionOperations(restTemplate(), baseUrl);
        }

        @Bean
        @Singleton
        public ProfileOperations playerProfileOperations() {
            return new IntegrationProfileOperations(restTemplate());
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
