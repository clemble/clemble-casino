package com.gogomaya.server.integration.emulation.tictactoe;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.emulator.GameplayEmulator;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;

public class TicTacToeMain {

    public static void main(String[] arguments) {
        // Step 1. Reading application context configuration
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("localServerIntegration");
        applicationContext.register(TestConfiguration.class);
        applicationContext.register(MainConfiguration.class);
        applicationContext.refresh();
        applicationContext.start();
        // Step 2. Starting game emulator
        GameplayEmulator<TicTacToeState> emulator = applicationContext.getBean(GameplayEmulator.class);
        emulator.emulate();
    }

    @Configuration
    public static class MainConfiguration {

        @Inject
        public GameOperations<TicTacToeState> gameOperations;

        @Bean
        @Singleton
        public TicTacToeActor actor() {
            return new TicTacToeActor();
        }

        @Bean
        @Singleton
        public GameplayEmulator<TicTacToeState> emulator() {
            return new GameplayEmulator<>(gameOperations, actor());
        }

    }

}
