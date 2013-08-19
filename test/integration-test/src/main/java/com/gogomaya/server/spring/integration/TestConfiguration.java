package com.gogomaya.server.spring.integration;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.game.IntegrationGameSessionPlayerFactory;
import com.gogomaya.server.integration.game.WebGameSessionPlayerFactory;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.construction.IntegrationGameConstructionOperations;
import com.gogomaya.server.integration.game.construction.WebGameConstructionOperations;
import com.gogomaya.server.integration.game.tictactoe.TicTacToePlayerSessionFactory;
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
import com.gogomaya.server.spring.web.WebMvcSpiSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaRESTErrorHandler;
import com.gogomaya.server.web.game.options.GameConfigurationManagerController;
import com.gogomaya.server.web.game.session.GameActionController;
import com.gogomaya.server.web.game.session.GameConstructionController;
import com.gogomaya.server.web.payment.PaymentTransactionController;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.PlayerSessionController;
import com.gogomaya.server.web.player.account.PlayerAccountController;
import com.gogomaya.server.web.player.registration.PlayerLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;

@Configuration
@Import(value = { JsonSpringConfiguration.class, TestConfiguration.LocalTestConfiguration.class, TestConfiguration.LocalIntegrationTestConfiguration.class,
        TestConfiguration.RemoteIntegrationTestConfiguration.class })
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
    @Profile(value = SpringConfiguration.PROFILE_DEFAULT)
    @Import(value = { WebMvcSpiSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Autowired
        @Qualifier("registrationSignInContoller")
        public RegistrationSignInContoller registrationSignInContoller;

        @Autowired
        @Qualifier("registrationLoginController")
        public PlayerLoginController registrationLoginController;

        @Autowired
        @Qualifier("playerSessionController")
        public PlayerSessionController playerSessionController;

        @Autowired
        @Qualifier("playerAccountController")
        public PlayerAccountController playerAccountController;

        @Autowired
        @Qualifier("ticTacToeConfigurationManagerController")
        public GameConfigurationManagerController ticTacToeConfigurationManagerController;

        @Autowired
        @Qualifier("ticTacToeConstructionController")
        public GameConstructionController<TicTacToeState> ticTacToeConstructionController;

        @Autowired
        @Qualifier("ticTacToeEngineController")
        public GameActionController<TicTacToeState> ticTacToeEngineController;

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
            return new WebPlayerOperations(registrationSignInContoller, registrationLoginController, sessionOperations(), accountOperations(),
                    playerListenerOperations(), playerProfileOperations(), ticTacToeGameConstructionOperations());
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new WebAccountOperations(playerAccountController);
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
        public GameSessionPlayerFactory<TicTacToeState> ticTacToeSessionFactory() {
            return new WebGameSessionPlayerFactory<TicTacToeState>(ticTacToeEngineController, ticTacToeConstructionController);
        }

        @Bean
        @Singleton
        public GameConstructionOperations<TicTacToeState> ticTacToeGameConstructionOperations() {
            return new WebGameConstructionOperations<TicTacToeState>(Game.pic, ticTacToeConfigurationManagerController, ticTacToeConstructionController,
                    ticTacToeSessionPlayerFactory());
        }

        @Bean
        @Singleton
        public GameSessionPlayerFactory<TicTacToeState> ticTacToeSessionPlayerFactory() {
            return new TicTacToePlayerSessionFactory(ticTacToeSessionFactory());
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.PROFILE_INTEGRATION_CLOUD)
    public static class RemoteIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            // return "http://gogomaya.cfapps.io/";
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/gogomaya/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.PROFILE_INTEGRATION_LOCAL_TEST)
    public static class LocalIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.PROFILE_INTEGRATION_LOCAL_SERVER)
    public static class LocalServerIntegrationTestConfiguration extends IntegrationTestConfiguration {

    }

    @Import(ClientRestCommonSpringConfiguration.class)
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
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
                    accountOperations(), ticTacToeGameConstructionOperations());
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
        public GameConstructionOperations<TicTacToeState> ticTacToeGameConstructionOperations() {
            return new IntegrationGameConstructionOperations<TicTacToeState>(Game.pic, getBaseUrl(), restTemplate(), ticTacToeSessionPlayerFactory());
        }

        @Bean
        @Singleton
        @SuppressWarnings("unchecked")
        public GameSessionPlayerFactory<TicTacToeState> ticTacToeSessionPlayerFactory() {
            return new TicTacToePlayerSessionFactory((GameSessionPlayerFactory<TicTacToeState>) genericGameSessionFactory());
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new IntegrationPaymentTransactionOperations(restTemplate(), getBaseUrl());
        }

    }
}
