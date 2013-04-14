package com.gogomaya.server.spring.integration;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.RestGameOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.game.listener.GameListenerOperationsImpl;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.RestPlayerOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToeOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;

@Configuration
public class IntegrationTestConfiguration {

    String baseUrl = "http://localhost:8080/gogomaya-web";

    // String baseUrl = "http://gogomaya.cloudfoundry.com/";

    @Bean
    @Singleton
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Singleton
    public PlayerOperations playerOperations() {
        return new RestPlayerOperations(baseUrl, restTemplate());
    }

    @Bean
    @Singleton
    public GameOperations gameOperations() {
        return new RestGameOperations(baseUrl, restTemplate());
    }

    @Bean
    @Singleton
    @SuppressWarnings("rawtypes")
    public GameListenerOperations<?> tableListenerOperations() {
        return new GameListenerOperationsImpl();
    }

    @Bean
    @Singleton
    @SuppressWarnings("unchecked")
    public TicTacToeOperations ticTacToeOperations() {
        return new TicTacToeOperations(baseUrl, restTemplate(), playerOperations(), gameOperations(),
                (GameListenerOperations<TicTacToeTable>) tableListenerOperations());
    }
}
