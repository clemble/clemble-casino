package com.gogomaya.integration.spring.game;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.gogomaya.game.Game;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
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
import com.gogomaya.server.integration.NumberState;
import com.gogomaya.server.integration.NumberStateFactory;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.state.PlayerStateManager;
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
public class IntegrationGameWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<NumberState> gameSessionRepository;

    @Autowired
    @Qualifier("gameSpecificationRepository")
    public GameSpecificationRepository gameSpecificationRepository;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<NumberState> gameTableRepository;

    @Autowired
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository gameConstructionRepository;

    @Autowired
    @Qualifier("playerNotificationService")
    public PlayerNotificationService playerNotificationService;

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
    public PlayerStateManager playerStateManager;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    @Bean
    @Singleton
    public GameTableFactory<NumberState> picPacPoeTableFactory() {
        return new GameTableFactory<NumberState>(ticTacToeStateFactory(), gameTableRepository, tableServerRegistry);
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
    public NumberStateFactory ticTacToeStateFactory() {
        return new NumberStateFactory(gameConstructionRepository, picPacPoeProcessorFactory());
    }

    @Bean
    @Singleton
    @DependsOn("gameSpecificationRepository")
    public GameSpecificationConfigurationManager picPacPoeConfigurationManager() {
        return new GameSpecificationConfigurationManager(Game.num, gameSpecificationRepository);
    }

    @Bean
    @Singleton()
    public GameProcessorFactory<NumberState> picPacPoeProcessorFactory() {
        return new GameProcessorFactory<NumberState>(gameSecurityAspectFactory, gameBetAspectFactory, gamePriceAspectFactory, gameTimeAspectFactory,
                gameOutcomeAspectFactory);
    }

    @Bean
    @Singleton
    public GameCacheService<NumberState> picPacPoeCacheService() {
        return new GameCacheService<NumberState>(gameConstructionRepository, gameSessionRepository, picPacPoeProcessorFactory(), ticTacToeStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<NumberState> picPacPoeSessionProcessor() {
        return new GameSessionProcessor<NumberState>(Game.num, picPacPoeTableFactory(), picPacPoeCacheService(), playerNotificationService);
    }

    @Bean
    @Singleton
    public GameConstructionController<NumberState> picPacPoeConstructionController() {
        return new GameConstructionController<NumberState>(gameConstructionRepository, picPacPoeConstructionService(), gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController picPacPoeConfigurationManagerController() {
        return new GameConfigurationManagerController(gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameActionController<NumberState> picPacPoeEngineController() {
        return new GameActionController<NumberState>(gameSessionRepository, picPacPoeSessionProcessor());
    }

}
