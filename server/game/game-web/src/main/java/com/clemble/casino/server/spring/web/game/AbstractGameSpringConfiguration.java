package com.clemble.casino.server.spring.web.game;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameSessionFactory;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.aspect.bet.GameBetAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.GameOutcomeAspectFactory;
import com.clemble.casino.server.game.aspect.price.GamePriceAspectFactory;
import com.clemble.casino.server.game.aspect.security.GameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.time.GameTimeAspectFactory;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.game.configuration.GameSpecificationConfigurationManager;
import com.clemble.casino.server.game.configuration.GameSpecificationRegistry;
import com.clemble.casino.server.game.construct.GameConstructionServerService;
import com.clemble.casino.server.game.construct.GameInitiatorService;
import com.clemble.casino.server.game.construct.SimpleGameConstructionServerService;
import com.clemble.casino.server.game.construct.SimpleGameInitiatorService;
import com.clemble.casino.server.game.notification.TableServerRegistry;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.GameSpecificationRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.game.options.GameConfigurationManagerController;
import com.clemble.casino.server.web.game.session.GameActionController;
import com.clemble.casino.server.web.game.session.GameConstructionController;

@Configuration
@Import({ GameManagementSpringConfiguration.class, WebCommonSpringConfiguration.class })
abstract public class AbstractGameSpringConfiguration<State extends GameState> implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<State> gameSessionRepository;

    @Autowired
    @Qualifier("gameSpecificationRepository")
    public GameSpecificationRepository gameSpecificationRepository;

    @Autowired
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository gameConstructionRepository;

    @Autowired
    @Qualifier("playerNotificationService")
    public PlayerNotificationService<Event> playerNotificationService;

    @Autowired
    @Qualifier("gamePriceAspectFactory")
    public GamePriceAspectFactory gamePriceAspectFactory;

    @Autowired
    @Qualifier("gameBetAspectFactory")
    public GameBetAspectFactory gameBetAspectFactory;

    @Autowired
    @Qualifier("gameSecurityAspectFactory")
    public GameSecurityAspectFactory gameSecurityAspectFactory;

    @Autowired
    @Qualifier("gameOutcomeAspectFactory")
    public GameOutcomeAspectFactory gameOutcomeAspectFactory;

    @Autowired
    @Qualifier("gameTimeAspectFactory")
    public GameTimeAspectFactory gameTimeAspectFactory;

    @Autowired
    @Qualifier("playerAccountService")
    public PlayerAccountServerService playerAccountService;

    @Autowired
    @Qualifier("tableServerRegistry")
    private TableServerRegistry tableServerRegistry;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerPresenceServerService playerStateManager;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    abstract public Game getGame();

    @Bean
    @Singleton
    public GameSessionFactory<State> gameSessionFactory() {
        return new GameSessionFactory<State>(ticTacToeStateFactory(), gameSessionRepository);
    }

    @Bean
    @Singleton
    public GameConstructionServerService picPacPoeConstructionService() {
        return new SimpleGameConstructionServerService(playerAccountService, playerNotificationService, gameConstructionRepository,
                picPacPoeInitiatorService(), playerLockService, playerStateManager);
    }

    @Bean
    @Singleton
    public GameInitiatorService picPacPoeInitiatorService() {
        return new SimpleGameInitiatorService(picPacPoeSessionProcessor(), playerStateManager);
    }

    @Bean
    @Singleton
    abstract public GameStateFactory<State> ticTacToeStateFactory();

    @Bean
    @Singleton
    @DependsOn("gameSpecificationRepository")
    public GameSpecificationConfigurationManager picPacPoeConfigurationManager() {
        return new GameSpecificationConfigurationManager(getGame(), gameSpecificationRepository);
    }

    @Bean
    @Singleton()
    public GameProcessorFactory<State> picPacPoeProcessorFactory() {
        return new GameProcessorFactory<State>(gameSecurityAspectFactory, gameBetAspectFactory, gamePriceAspectFactory, gameTimeAspectFactory,
                gameOutcomeAspectFactory);
    }

    @Bean
    @Singleton
    public GameCacheService<State> picPacPoeCacheService() {
        return new GameCacheService<State>(gameConstructionRepository, gameSessionRepository, picPacPoeProcessorFactory(), ticTacToeStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<State> picPacPoeSessionProcessor() {
        return new GameSessionProcessor<State>(getGame(), tableServerRegistry, gameSessionFactory(), picPacPoeCacheService(), playerNotificationService);
    }

    @Bean
    @Singleton
    public GameConstructionController<State> picPacPoeConstructionController() {
        return new GameConstructionController<State>(getGame(), gameConstructionRepository, picPacPoeConstructionService(), gameSpecificationRegistry, tableServerRegistry);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController picPacPoeConfigurationManagerController() {
        return new GameConfigurationManagerController(gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameActionController<State> picPacPoeEngineController() {
        return new GameActionController<State>(getGame(), gameSessionRepository, picPacPoeSessionProcessor());
    }
}
