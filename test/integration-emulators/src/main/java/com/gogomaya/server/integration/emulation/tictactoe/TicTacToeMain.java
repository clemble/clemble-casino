package com.gogomaya.server.integration.emulation.tictactoe;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.emulator.GameplayEmulator;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;

public class TicTacToeMain {

    @SuppressWarnings({ "resource", "unchecked" })
    public static void main(String[] arguments) {
        // Step 1. Reading application context configuration
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("localServerIntegration");
        applicationContext.register(TestConfiguration.class);
        applicationContext.register(MainConfiguration.class);
        applicationContext.refresh();
        applicationContext.start();

        // Step 2. Starting game emulator
        GameplayEmulator<TicTacToeState> emulator = applicationContext.getBean(GameplayEmulator.class);
        emulator.emulate();

        // Step 3. To guarantee proper resource release, registering shutdown hook
        applicationContext.registerShutdownHook();
    }

    @Configuration
    public static class MainConfiguration {

        @Inject
        public GameConstructionOperations<TicTacToeState> gameOperations;
        
        @Inject
        public PlayerOperations playerOperations;

        @Bean
        @Singleton
        public TicTacToeActor actor() {
            return new TicTacToeActor();
        }

        @Bean
        @Singleton
        public GameplayEmulator<TicTacToeState> emulator() {
            return new GameplayEmulator<>(playerOperations, gameOperations, actor());
        }

    }

}
