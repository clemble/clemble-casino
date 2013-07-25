package com.gogomaya.server.spring.tictactoe;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.action.impl.GamePostProcessorListener;
import com.gogomaya.server.game.action.impl.VerificationGameProcessorListener;
import com.gogomaya.server.game.aspect.time.GameTimeManagementService;
import com.gogomaya.server.game.aspect.time.GameTimeProcessorListenerFactory;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.construct.SimpleGameConstructionService;
import com.gogomaya.server.game.construct.SimpleGameInitiatorService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameSpecificationRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.gogomaya.server.tictactoe.action.impl.TicTacToeProcessor;
import com.gogomaya.server.tictactoe.action.impl.TicTacToeStateFactory;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
@Import(value = { GameManagementSpringConfiguration.class })
public class TicTacToeSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<TicTacToeState> gameSessionRepository;

    @Autowired
    @Qualifier("gameSpecificationRepository")
    public GameSpecificationRepository gameSpecificationRepository;

    @Autowired
    @Qualifier("playerWalletService")
    public PlayerAccountService playerWalletService;

    @Autowired
    @Qualifier("tableServerRegistry")
    private TableServerRegistry tableServerRegistry;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<TicTacToeState> gameTableRepository;

    @Autowired
    @Qualifier("verificationGameProcessorListener")
    public VerificationGameProcessorListener<TicTacToeState> verificationGameProcessorListener;

    @Autowired
    @Qualifier("gamePostProcessorListener")
    public GamePostProcessorListener<TicTacToeState> gamePostProcessorListener;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Autowired
    @Qualifier("playerNotificationService")
    public PlayerNotificationService playerNotificationService;

    @Autowired
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository gameConstructionRepository;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Bean
    @Singleton
    public GameTableFactory<TicTacToeState> ticTacToeTableFactory() {
        return new GameTableFactory<TicTacToeState>(ticTacToeStateFactory(), gameTableRepository, tableServerRegistry);
    }

    @Bean
    @Singleton
    public GameConstructionService ticTacToeConstructionService() {
        return new SimpleGameConstructionService(playerWalletService, playerNotificationService, gameConstructionRepository, ticTacToeInitiatorService(),
                playerLockService, playerStateManager);
    }

    @Bean
    @Singleton
    public GameInitiatorService ticTacToeInitiatorService() {
        return new SimpleGameInitiatorService(ticTacToeSessionProcessor(), playerStateManager);
    }

    @Bean
    @Singleton
    public TicTacToeStateFactory ticTacToeStateFactory() {
        return new TicTacToeStateFactory(ticTacToeProcessorFactory());
    }

    @Bean
    @Singleton
    public TicTacToeConfigurationManager ticTacToeConfigurationManager() {
        return new TicTacToeConfigurationManager(gameSpecificationRepository);
    }

    @Bean
    @Singleton()
    public GameProcessorFactory<TicTacToeState> ticTacToeProcessorFactory() {
        return new GameProcessorFactory<TicTacToeState>(new TicTacToeProcessor(), verificationGameProcessorListener, gamePostProcessorListener);
    }

    @Bean
    @Singleton
    public GameCacheService<TicTacToeState> ticTacToeCacheService() {
        return new GameCacheService<TicTacToeState>(gameSessionRepository, ticTacToeProcessorFactory(), ticTacToeStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<TicTacToeState> ticTacToeSessionProcessor() {
        return new GameSessionProcessor<TicTacToeState>(ticTacToeTableFactory(), ticTacToeCacheService(), playerNotificationService);
    }

    @Bean
    @Singleton
    public GameTimeProcessorListenerFactory<TicTacToeState> ticTacToeTimeProcessorListenerFactory() {
        GameTimeProcessorListenerFactory<TicTacToeState> timeProcessorListenerFactory = new GameTimeProcessorListenerFactory<TicTacToeState>(
                ticTacToeTimeManagementService());
        ticTacToeProcessorFactory().setTimeListenerFactory(timeProcessorListenerFactory);
        return timeProcessorListenerFactory;
    }

    @Bean
    @Singleton
    public GameTimeManagementService<TicTacToeState> ticTacToeTimeManagementService() {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("Timeout Management - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new GameTimeManagementService<TicTacToeState>(ticTacToeSessionProcessor(), executorService);
    }

}
