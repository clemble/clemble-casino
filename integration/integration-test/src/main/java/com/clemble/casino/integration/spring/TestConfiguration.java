package com.clemble.casino.integration.spring;

import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_CLOUD;
import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_DEFAULT;
import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_TEST;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.annotation.PostConstruct;
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
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.error.ClembleCasinoErrorHandler;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.game.SimpleGameSessionPlayerFactory;
import com.clemble.casino.integration.game.construction.SimpleGameScenarios;
import com.clemble.casino.integration.payment.IntegrationPaymentTransactionOperations;
import com.clemble.casino.integration.payment.PaymentTransactionOperations;
import com.clemble.casino.integration.payment.WebPaymentTransactionOperations;
import com.clemble.casino.integration.player.IntegrationPlayerRegistrationService;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.player.SimplePlayerOperations;
import com.clemble.casino.integration.player.account.PaymentServiceFactory;
import com.clemble.casino.integration.player.listener.EventListenerOperationsFactory;
import com.clemble.casino.integration.player.profile.PlayerProfileServiceFactory;
import com.clemble.casino.integration.player.session.IntegrationSessionOperations;
import com.clemble.casino.integration.spring.game.IntegrationGameWebSpringConfiguration;
import com.clemble.casino.integration.spring.web.management.ManagementWebSpringConfiguration;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.ServerRegistrySpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.clemble.casino.server.spring.web.player.PlayerWebSpringConfiguration;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { JsonSpringConfiguration.class, TestConfiguration.LocalTestConfiguration.class, TestConfiguration.IntegrationTestConfiguration.class })
public class TestConfiguration {

    @Autowired
    @Qualifier("playerOperations")
    public PlayerOperations playerOperations;

    @Autowired
    public GameSessionPlayerFactory sessionPlayerFactory;

    @PostConstruct
    public void initialize() {
        ObjectGenerator.register(RSAKeySecret.class, new AbstractValueGenerator<RSAKeySecret>() {

            @Override
            public RSAKeySecret generate() {
                try {
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(1024);
                    KeyPair keyPair = generator.generateKeyPair();
                    return new RSAKeySecret(keyPair.getPrivate(), keyPair.getPublic());
                } catch (NoSuchAlgorithmException algorithmException) {
                    return null;
                }
            }
        });
    }

    @Bean
    @Singleton
    public SimpleGameScenarios gameScenarios() {
        return new SimpleGameScenarios(playerOperations, sessionPlayerFactory);
    }

    @Bean
    @Autowired
    public GameSessionPlayerFactory sessionPlayerFactory() {
        return new SimpleGameSessionPlayerFactory();
    }

    @Bean
    @Autowired
    public PlayerOperations playerOperations(ObjectMapper objectMapper,
            EventListenerOperationsFactory listenerOperations,
            PlayerRegistrationService registrationService,
            PlayerProfileServiceFactory profileOperations,
            PlayerSessionService sessionOperations,
            PaymentServiceFactory accountOperations) {
        return new SimplePlayerOperations(objectMapper, listenerOperations, registrationService, profileOperations, sessionOperations, accountOperations);
    }

    @Configuration
    @Profile(value = SpringConfiguration.DEFAULT)
    @Import(value = { PaymentWebSpringConfiguration.class, PlayerWebSpringConfiguration.class, ManagementWebSpringConfiguration.class,
            IntegrationGameWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Autowired
        @Qualifier("playerRegistrationController")
        public PlayerRegistrationService playerRegistrationController;

        @Autowired
        @Qualifier("playerSessionController")
        public PlayerSessionService playerSessionController;

        @Autowired
        @Qualifier("playerAccountController")
        public PlayerAccountService playerAccountController;

        @Autowired
        public PlayerProfileService playerProfileController;

        @Autowired
        public PaymentTransactionService paymentTransactionController;

        @Bean
        public EventListenerOperationsFactory playerListenerOperations() {
            if (new Random().nextBoolean()) {
                return new EventListenerOperationsFactory.RabbitEventListenerServiceFactory();
            } else {
                return new EventListenerOperationsFactory.StompEventListenerServiceFactory();
            }
        }

        @Bean
        public PaymentServiceFactory accountOperations() {
            return new PaymentServiceFactory.SingletonPaymentService(paymentTransactionController, playerAccountController);
        }

        @Bean
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

        @Bean
        public PlayerProfileServiceFactory playerProfileOperations() {
            return new PlayerProfileServiceFactory.SingletonPlayerProfileServiceFactory(playerProfileController);
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

        public String getBaseUrl() {
            String base = baseUrl.substring(0, baseUrl.substring(0, baseUrl.length() - 1).lastIndexOf("/") + 1);
            return base;
        }

        @Bean
        public EventListenerOperationsFactory playerListenerOperations() {
            if (new Random().nextBoolean()) {
                return new EventListenerOperationsFactory.RabbitEventListenerServiceFactory();
            } else {
                return new EventListenerOperationsFactory.StompEventListenerServiceFactory();
            }
        }

        @Bean
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                    ((MappingJackson2HttpMessageConverter) messageConverter).setObjectMapper(objectMapper);
                }
            }

            restTemplate.setErrorHandler(new ClembleCasinoErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        public PaymentServiceFactory accountOperations() {
            return new PaymentServiceFactory.IntegrationPaymentServiceFactory(restTemplate());
        }

        @Bean
        public PlayerRegistrationService playerRegistrationService() {
            return new IntegrationPlayerRegistrationService(baseUrl, restTemplate());
        }

        @Bean
        public PlayerSessionService sessionOperations() {
            return new IntegrationSessionOperations(restTemplate(), baseUrl);
        }

        @Bean
        public PlayerProfileServiceFactory playerProfileOperations() {
            return new PlayerProfileServiceFactory.IntegrationPlayerProfileServiceFactory(restTemplate());
        }

        @Bean
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new IntegrationPaymentTransactionOperations(restTemplate(), serverRegistryConfiguration.getPaymentRegistry());
        }

    }
}
