package com.clemble.casino.server.spring.web.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.server.event.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameSessionFactory;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.game.configuration.GameSpecificationConfigurationManager;
import com.clemble.casino.server.game.configuration.GameSpecificationRegistry;
import com.clemble.casino.server.game.construct.BasicServerGameConstructionService;
import com.clemble.casino.server.game.construct.BasicServerGameInitiationService;
import com.clemble.casino.server.game.construct.PendingGameInitiationListener;
import com.clemble.casino.server.game.construct.ServerGameConstructionService;
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.GameSpecificationRepository;
import com.clemble.casino.server.repository.game.PendingGameInitiationRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.game.options.GameSpecificationController;
import com.clemble.casino.server.web.game.session.GameActionController;
import com.clemble.casino.server.web.game.session.GameConstructionController;
import com.clemble.casino.server.web.game.session.GameInitiationController;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

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
    @Qualifier("playerAccountService")
    public ServerPlayerAccountService playerAccountService;

    @Autowired
    public ServerPlayerPresenceService playerStateManager;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    @Autowired
    public GameIdGenerator gameIdGenerator;

    abstract public Game getGame();

    @Bean
    public GameSessionFactory<State> gameSessionFactory(GameStateFactory<State> gameStateFactory) {
        return new GameSessionFactory<State>(gameStateFactory, gameSessionRepository);
    }

    @Bean
    public GameConstructionController<State> constructionController(ServerGameConstructionService constructionServerService,
            ServerGameInitiationService initiationService) {
        return new GameConstructionController<State>(gameConstructionRepository, constructionServerService, gameSpecificationRegistry);
    }

    @Bean
    public GameInitiationController initiationController(ServerGameInitiationService initiationService) {
        return new GameInitiationController(initiationService);
    }

    @Bean
    public ServerGameConstructionService picPacPoeConstructionService(ServerGameInitiationService initiatorService) {
        return new BasicServerGameConstructionService(gameIdGenerator, playerAccountService, playerNotificationService, gameConstructionRepository,
                initiatorService, playerLockService, playerStateManager);
    }

    @Bean
    public ServerGameInitiationService serverGameInitiationService(GameSessionProcessor<?> sessionProcessor, ServerPlayerPresenceService presenceServerService,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService, PendingPlayerRepository pendingPlayerRepository,
            PendingGameInitiationRepository initiationRepository) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("Game initiation - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new BasicServerGameInitiationService(sessionProcessor, presenceServerService, notificationService, pendingPlayerRepository,
                initiationRepository, executorService);
    }

    @Bean
    public PendingGameInitiationListener pendingGameInitiationListener(PendingGameInitiationRepository initiationRepository,
            PendingPlayerRepository playerRepository, SystemNotificationServiceListener notificationServiceListener, ServerPlayerPresenceService presenceService, ServerGameInitiationService initiationService) {
        PendingGameInitiationListener initiationListener = new PendingGameInitiationListener(playerRepository, presenceService, initiationService);
        notificationServiceListener.subscribe(SystemPlayerPresenceChangedEvent.CHANNEL, initiationListener);
        return initiationListener;
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
        return new GameCacheService<State>(gameSessionRepository, picPacPoeProcessorFactory(), gameStateFactory);
    }

    @Bean
    public GameSessionProcessor<State> picPacPoeSessionProcessor(GameSessionFactory<State> gameSessionFactory, GameCacheService<State> gameCacheService) {
        return new GameSessionProcessor<State>(gameSessionFactory, gameCacheService, playerNotificationService);
    }

    @Bean
    public GameSpecificationController picPacPoeConfigurationManagerController() {
        return new GameSpecificationController(gameSpecificationRegistry);
    }

    @Bean
    public GameActionController<State> picPacPoeEngineController(GameSessionProcessor<State> sessionProcessor) {
        return new GameActionController<State>(gameSessionRepository, sessionProcessor);
    }
}
