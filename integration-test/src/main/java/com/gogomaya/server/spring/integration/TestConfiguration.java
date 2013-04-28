package com.gogomaya.server.spring.integration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.IntegrationGameOperations;
import com.gogomaya.server.integration.game.WebGameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperationsImpl;
import com.gogomaya.server.integration.player.IntegrationPlayerOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.WebPlayerOperations;
import com.gogomaya.server.integration.tictactoe.IntegrationTicTacToeOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToeOperations;
import com.gogomaya.server.integration.tictactoe.WebTicTacToeOperations;
import com.gogomaya.server.spring.web.WebGenericConfiguration;
import com.gogomaya.server.spring.web.WebMvcSpiConfiguration;
import com.gogomaya.server.web.active.session.GameEngineController;
import com.gogomaya.server.web.active.session.GameTableMatchController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;

@Configuration
@Import(value = {TestConfiguration.LocalTestConfiguration.class,
        TestConfiguration.LocalIntegrationTestConfiguration.class,
        TestConfiguration.RemoteIntegrationTestConfiguration.class})
public class TestConfiguration {

    @Configuration
    @Profile("default")
    @Import(value = {WebGenericConfiguration.class, WebMvcSpiConfiguration.class})
    public static class LocalTestConfiguration {

        @Inject
        RegistrationSignInContoller signInContoller;

        @Inject
        RegistrationLoginController loginController;

        @Inject
        GameConfigurationManagerController configuartionManagerController;

        @Inject
        GameTableMatchController matchController;
        
        @Inject
        GameEngineController engineController;
        
        @Bean
        @Singleton
        @SuppressWarnings("rawtypes")
        public GameListenerOperations<?> tableListenerOperations() {
            return new GameListenerOperationsImpl();
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new WebPlayerOperations(signInContoller, loginController);
        }

        @Bean
        @Singleton
        public GameOperations gameOperations() {
            return new WebGameOperations(configuartionManagerController, matchController);
        }

        @Bean
        @Singleton
        @SuppressWarnings("unchecked")
        public TicTacToeOperations ticTacToeOperations() {
            return new WebTicTacToeOperations(playerOperations(), gameOperations(), (GameListenerOperations<GameTable<TicTacToeState>>) tableListenerOperations(), engineController);
        }
    }

    @Configuration
    @Profile("remoteIntegration")
    public static class RemoteIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://gogomaya.cloudfoundry.com/";
        }

    }

    @Configuration
    @Profile("localIntegration")
    public static class LocalIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:8080/gogomaya-web";
        }

    }

    public static class IntegrationTestConfiguration {

        public String getBaseUrl() {
            return "http://localhost:8080/gogomaya-web";
        }

        @Bean
        @Singleton
        @SuppressWarnings("rawtypes")
        public GameListenerOperations<?> tableListenerOperations() {
            return new GameListenerOperationsImpl();
        }

        @Bean
        @Singleton
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new IntegrationPlayerOperations(getBaseUrl(), restTemplate());
        }

        @Bean
        @Singleton
        public GameOperations gameOperations() {
            return new IntegrationGameOperations(getBaseUrl(), restTemplate());
        }

        @Bean
        @Singleton
        @SuppressWarnings("unchecked")
        public IntegrationTicTacToeOperations ticTacToeOperations() {
            return new IntegrationTicTacToeOperations(getBaseUrl(), restTemplate(), playerOperations(), gameOperations(),
                    (GameListenerOperations<GameTable<TicTacToeState>>) tableListenerOperations());
        }
    }
}
