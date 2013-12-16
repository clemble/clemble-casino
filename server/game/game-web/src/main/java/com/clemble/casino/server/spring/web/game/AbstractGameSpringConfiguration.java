package com.clemble.casino.server.spring.web.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameSessionFactory;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.aspect.bet.GameBetAspectFactory;
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
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.GameSpecificationRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.game.options.GameSpecificationController;
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
    public PlayerNotificationService playerNotificationService;

    @Autowired
    @Qualifier("gamePriceAspectFactory")
    public GamePriceAspectFactory gamePriceAspectFactory;

    @Autowired
    @Qualifier("gameBetAspectFactory")
    public GameBetAspectFactory betAspectFactory;

    @Autowired
    @Qualifier("gameSecurityAspectFactory")
    public GameSecurityAspectFactory securityAspectFactory;

    @Autowired
    @Qualifier("gameTimeAspectFactory")
    public GameTimeAspectFactory timeAspectFactory;

    @Autowired
    @Qualifier("playerAccountService")
    public PlayerAccountServerService playerAccountService;

    @Autowired
    public PlayerPresenceServerService playerStateManager;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    @Autowired
    public GameIdGenerator gameIdGenerator;

    abstract public Game getGame();

    @Bean
    @Autowired
    public GameSessionFactory<State> gameSessionFactory(GameStateFactory<State> gameStateFactory) {
        return new GameSessionFactory<State>(gameStateFactory, gameSessionRepository);
    }

    @Bean
    public GameConstructionController<State> constructionController(GameConstructionServerService constructionServerService) {
        return new GameConstructionController<State>(gameConstructionRepository, constructionServerService, gameSpecificationRegistry);
    }

    @Bean
    public GameConstructionServerService picPacPoeConstructionService(GameInitiatorService initiatorService) {
        return new SimpleGameConstructionServerService(gameIdGenerator, playerAccountService, playerNotificationService, gameConstructionRepository,
                initiatorService, playerLockService, playerStateManager);
    }

    @Bean
    @Autowired
    public GameInitiatorService picPacPoeInitiatorService(GameSessionProcessor sessionProcessor, PlayerPresenceServerService presenceServerService, SystemNotificationServiceListener presenceListenerService) {
        return new SimpleGameInitiatorService(sessionProcessor, presenceServerService, presenceListenerService);
    }

    @Bean
    @DependsOn("gameSpecificationRepository")
    public GameSpecificationConfigurationManager picPacPoeConfigurationManager() {
        return new GameSpecificationConfigurationManager(getGame(), gameSpecificationRepository);
    }

    @Bean
    public GameProcessorFactory<State> picPacPoeProcessorFactory() {
        return new GameProcessorFactory<State>();
    }

    @Bean
    public GameCacheService<State> picPacPoeCacheService(GameStateFactory<State> gameStateFactory) {
        return new GameCacheService<State>(gameConstructionRepository, gameSessionRepository, picPacPoeProcessorFactory(), gameStateFactory);
    }

    @Bean
    @Autowired
    public GameSessionProcessor picPacPoeSessionProcessor(GameSessionFactory<?> gameSessionFactory, GameCacheService<?> gameCacheService) {
        return new GameSessionProcessor(gameSessionFactory, gameCacheService, playerNotificationService);
    }

    @Bean
    public GameSpecificationController picPacPoeConfigurationManagerController() {
        return new GameSpecificationController(gameSpecificationRegistry);
    }

    @Bean
    @Autowired
    public GameActionController<State> picPacPoeEngineController(GameSessionProcessor sessionProcessor) {
        return new GameActionController<State>(gameSessionRepository, sessionProcessor);
    }
}
