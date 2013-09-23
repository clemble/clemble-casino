package com.gogomaya.server.spring.web.game;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.gogomaya.event.Event;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameState;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.aspect.bet.GameBetAspectFactory;
import com.gogomaya.server.game.aspect.outcome.GameOutcomeAspectFactory;
import com.gogomaya.server.game.aspect.price.GamePriceAspectFactory;
import com.gogomaya.server.game.aspect.security.GameSecurityAspectFactory;
import com.gogomaya.server.game.aspect.time.GameTimeAspectFactory;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.game.configuration.GameSpecificationConfigurationManager;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.game.construct.GameConstructionServerService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.construct.SimpleGameConstructionServerService;
import com.gogomaya.server.game.construct.SimpleGameInitiatorService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameSpecificationRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.web.game.options.GameConfigurationManagerController;
import com.gogomaya.server.web.game.session.GameActionController;
import com.gogomaya.server.web.game.session.GameConstructionController;

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
    @Qualifier("gameTableRepository")
    public GameTableRepository<State> gameTableRepository;

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
    public GameTableFactory<State> picPacPoeTableFactory() {
        return new GameTableFactory<State>(ticTacToeStateFactory(), gameTableRepository, tableServerRegistry);
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
        return new GameSessionProcessor<State>(getGame(), picPacPoeTableFactory(), picPacPoeCacheService(), playerNotificationService);
    }

    @Bean
    @Singleton
    public GameConstructionController<State> picPacPoeConstructionController() {
        return new GameConstructionController<State>(gameConstructionRepository, picPacPoeConstructionService(), gameSpecificationRegistry, tableServerRegistry);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController picPacPoeConfigurationManagerController() {
        return new GameConfigurationManagerController(gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameActionController<State> picPacPoeEngineController() {
        return new GameActionController<State>(gameSessionRepository, picPacPoeSessionProcessor());
    }
}
