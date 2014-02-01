package com.clemble.casino.server.spring.web.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.action.MatchGameManager;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.game.configuration.ServerGameConfigurationService;
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.game.construction.auto.ServerAutoGameConstructionService;
import com.clemble.casino.server.game.construction.availability.PendingPlayerCreationEventListener;
import com.clemble.casino.server.game.construction.availability.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.game.options.GameConfigurationController;
import com.clemble.casino.server.web.game.session.AutoGameConstructionController;
import com.clemble.casino.server.web.game.session.AvailabilityGameConstructionController;
import com.clemble.casino.server.web.game.session.GameActionController;
import com.clemble.casino.server.web.game.session.GameInitiationController;

@Configuration
@Import({ GameManagementSpringConfiguration.class, WebCommonSpringConfiguration.class })
abstract public class AbstractGameSpringConfiguration<State extends GameState> implements SpringConfiguration {

    abstract public Game getGame();

    @Bean
    public GameInitiationController gameInitiationController(ServerGameInitiationService initiationService) {
        return new GameInitiationController(initiationService);
    }

    @Bean
    public PendingPlayerCreationEventListener pendingPlayerCreationListener(PendingPlayerRepository playerRepository,
            SystemNotificationServiceListener notificationServiceListener) {
        PendingPlayerCreationEventListener initiationListener = new PendingPlayerCreationEventListener(playerRepository);
        notificationServiceListener.subscribe(initiationListener);
        return initiationListener;
    }

    @Bean
    public GameProcessorFactory<State> picPacPoeProcessorFactory() {
        return new GameProcessorFactory<State>();
    }

    @Bean
    public GameCacheService<State> picPacPoeCacheService(GameStateFactory<State> gameStateFactory, ServerGameConfigurationRepository configurationRepository,
            GameSessionRepository<State> gameSessionRepository) {
        return new GameCacheService<State>(gameSessionRepository, picPacPoeProcessorFactory(), gameStateFactory, configurationRepository);
    }

    @Bean
    public MatchGameManager<State> matchGameManager(GameStateFactory<State> stateFactory,
            GameSessionRepository<State> sessionRepository,
            GameCacheService<State> cacheService,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new MatchGameManager<State>(stateFactory, sessionRepository, cacheService, notificationService);
    }

    @Bean
    public GameConfigurationController gameConfigurationController(ServerGameConfigurationService configurationService) {
        return new GameConfigurationController(configurationService);
    }

    @Bean
    public GameActionController<State> picPacPoeEngineController(
            MatchGameManager<State> sessionProcessor,
            GameSessionRepository<State> gameSessionRepository,
            GameCacheService<State> cacheService) {
        return new GameActionController<State>(gameSessionRepository, cacheService, sessionProcessor);
    }

    @Bean
    public AutoGameConstructionController<State> autoGameConstructionController(ServerAutoGameConstructionService constructionService) {
        return new AutoGameConstructionController<>(constructionService);
    }

    @Bean
    public AvailabilityGameConstructionController availabilityGameConstructionController(ServerAvailabilityGameConstructionService constructionService,
            GameConstructionRepository constructionRepository) {
        return new AvailabilityGameConstructionController(constructionService, constructionRepository);
    }
}
