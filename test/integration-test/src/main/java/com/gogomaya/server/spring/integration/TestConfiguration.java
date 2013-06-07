package com.gogomaya.server.spring.integration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

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
import com.gogomaya.server.integration.util.GogomayaHTTPErrorHandler;
import com.gogomaya.server.spring.web.WebGenericConfiguration;
import com.gogomaya.server.spring.web.WebMvcSpiConfiguration;
import com.gogomaya.server.web.active.session.GameEngineController;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.session.PlayerSessionController;
import com.gogomaya.server.web.player.wallet.WalletController;

@Configuration
@Import(value = { TestConfiguration.LocalTestConfiguration.class, TestConfiguration.LocalIntegrationTestConfiguration.class,
        TestConfiguration.RemoteIntegrationTestConfiguration.class })
public class TestConfiguration {

    @Configuration
    @Profile("default")
    @Import(value = { WebMvcSpiConfiguration.class })
    public static class LocalTestConfiguration {

        @Inject
        RegistrationSignInContoller signInContoller;

        @Inject
        RegistrationLoginController loginController;

        @Inject
        PlayerSessionController sessionController;

        @Inject
        WalletController walletController;

        @Inject
        GameConfigurationManagerController configuartionManagerController;

        @Inject
        GameConstructionController<TicTacToeState> matchController;

        @Inject
        GameEngineController<TicTacToeState> engineController;

        @Bean
        @Singleton
        public GameListenerOperations<TicTacToeState> tableListenerOperations() {
            return new GameListenerOperationsImpl<TicTacToeState>();
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new WebPlayerOperations(signInContoller, loginController, walletController, sessionController);
        }

        @Bean
        @Singleton
        public GameOperations<TicTacToeState> gameOperations() {
            return new WebGameOperations<TicTacToeState>(configuartionManagerController, matchController, ticTacToeOperations(), playerOperations());
        }

        @Bean
        @Singleton
        public TicTacToeOperations ticTacToeOperations() {
            return new WebTicTacToeOperations(tableListenerOperations(), engineController);
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
            return "http://localhost:9999";
        }

    }

    @Configuration
    @Profile("localServerIntegration")
    public static class LocalServerIntegrationTestConfiguration extends IntegrationTestConfiguration {

    }

    public static class IntegrationTestConfiguration {

        public String getBaseUrl() {
            return "http://localhost:8080/gogomaya-web";
        }

        @Bean
        @Singleton
        public GameListenerOperations<TicTacToeState> tableListenerOperations() {
            return new GameListenerOperationsImpl<TicTacToeState>();
        }

        @Bean
        @Singleton
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new GogomayaHTTPErrorHandler());
            return restTemplate;
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new IntegrationPlayerOperations(getBaseUrl(), restTemplate());
        }

        @Bean
        @Singleton
        public GameOperations<TicTacToeState> gameOperations() {
            return new IntegrationGameOperations<TicTacToeState>(getBaseUrl(), restTemplate(), ticTacToeOperations(), playerOperations());
        }

        @Bean
        @Singleton
        public IntegrationTicTacToeOperations ticTacToeOperations() {
            return new IntegrationTicTacToeOperations(getBaseUrl(), restTemplate(), tableListenerOperations());
        }
    }
}
