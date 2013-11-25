package com.clemble.casino.integration.spring;

import java.util.ArrayList;
import java.util.List;

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

import com.clemble.casino.client.error.ClembleCasinoErrorHandler;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.integration.payment.PaymentTransactionOperations;
import com.clemble.casino.integration.payment.WebPaymentTransactionOperations;
import com.clemble.casino.integration.player.IntegrationPlayerOperations;
import com.clemble.casino.integration.player.IntegrationPlayerRegistrationService;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.spring.game.IntegrationGameWebSpringConfiguration;
import com.clemble.casino.integration.spring.web.management.IntegrationManagementWebSpringConfiguration;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.ServerRegistrySpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.clemble.casino.server.spring.web.player.PlayerWebSpringConfiguration;
import com.clemble.casino.server.web.payment.PaymentTransactionController;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { BaseTestSpringConfiguration.class, JsonSpringConfiguration.class, IntegrationTestSpringConfiguration.LocalTestConfiguration.class, IntegrationTestSpringConfiguration.IntegrationTestConfiguration.class })
public class IntegrationTestSpringConfiguration implements TestSpringConfiguration {

    @Configuration
    @Profile(DEFAULT)
    @Import({ PaymentWebSpringConfiguration.class, PlayerWebSpringConfiguration.class, IntegrationManagementWebSpringConfiguration.class, IntegrationGameWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Bean
        @Autowired
        public PaymentTransactionOperations paymentTransactionOperations(PaymentTransactionController paymentTransactionController) {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

    }

    @Configuration
    @Profile({ INTEGRATION_TEST, INTEGRATION_CLOUD, INTEGRATION_DEFAULT })
    @Import({ ClientRestCommonSpringConfiguration.class, ServerRegistrySpringConfiguration.class })
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Value("#{systemProperties['clemble.casino.management.url'] ?: 'http://localhost:8080/integration-management-web/'}")
        public String baseUrl;

        @Autowired
        public ServerRegistryConfiguration serverRegistryConfiguration;

        @Bean
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jackson2HttpMessageConverter.setObjectMapper(objectMapper);
            messageConverters.add(jackson2HttpMessageConverter);

            restTemplate.setMessageConverters(messageConverters);
            restTemplate.setErrorHandler(new ClembleCasinoErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        public PlayerRegistrationService playerRegistrationService() {
            return new IntegrationPlayerRegistrationService(baseUrl, restTemplate());
        }

        @Bean
        @Autowired
        public PlayerOperations playerOperations(PlayerRegistrationService registrationService) {
            return new IntegrationPlayerOperations(baseUrl, registrationService);
        }

    }
}
