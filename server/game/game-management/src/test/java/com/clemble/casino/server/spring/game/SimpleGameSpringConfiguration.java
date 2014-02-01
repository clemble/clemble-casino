package com.clemble.casino.server.spring.game;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.action.MatchGameManager;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

@Ignore
@Configuration
@Import(GameManagementSpringConfiguration.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleGameSpringConfiguration {

    @Bean
    public MatchGameManager<?> gameProcessor(GameStateFactory<?> stateFactory, GameSessionRepository<?> sessionRepository, GameCacheService<?> cacheService,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new MatchGameManager(stateFactory, sessionRepository, cacheService, notificationService);
    }

    @Bean
    public GameCacheService<?> gameCacheService(GameSessionRepository<?> sessionRepository, GameProcessorFactory processorFactory,
            GameStateFactory stateFactory, ServerGameConfigurationRepository configurationRepository) {
        return new GameCacheService<>(sessionRepository, processorFactory, stateFactory, configurationRepository);
    }

    @Bean
    public GameProcessorFactory<?> processorFactory() {
        return new GameProcessorFactory<>();
    }

    @Bean
    public GameStateFactory fakeStateFactory() {
        return new GameStateFactory() {

            @Override
            public Game getGame() {
                return Game.num;
            }

            @Override
            public GameState constructState(GameInitiation initiation, MatchGameContext context) {
                throw new UnsupportedOperationException();
            }

        };
    }

}
