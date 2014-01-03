package com.clemble.casino.server.spring.game;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameSessionFactory;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.GameSessionRepository;

@Ignore
@Configuration
@Import(GameManagementSpringConfiguration.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleGameSpringConfiguration {

    @Bean
    @Autowired
    public GameSessionProcessor<?> gameProcessor(GameSessionFactory sessionFactory, GameCacheService cacheService,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameSessionProcessor(sessionFactory, cacheService, notificationService);
    }

    @Bean
    @Autowired
    public GameCacheService<?> gameCacheService(GameSessionRepository<?> sessionRepository, GameProcessorFactory processorFactory, GameStateFactory stateFactory) {
        return new GameCacheService<>(sessionRepository, processorFactory, stateFactory);
    }

    @Bean
    public GameProcessorFactory<?> processorFactory() {
        return new GameProcessorFactory<>();
    }

    
    @Bean
    @Autowired
    public GameSessionFactory gameSessionFactory(GameStateFactory stateFactory, GameSessionRepository sessionRepository) {
        return new GameSessionFactory(stateFactory, sessionRepository);
    }

    @Bean
    public GameStateFactory ticTacToeStateFactory() {
        return new GameStateFactory() {

            @Override
            public GameState constructState(GameInitiation initiation, GameContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public GameState constructState(GameSession gameSession) {
                throw new UnsupportedOperationException();
            }
        };
    }

}
