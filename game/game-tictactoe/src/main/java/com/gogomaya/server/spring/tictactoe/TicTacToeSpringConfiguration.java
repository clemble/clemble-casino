package com.gogomaya.server.spring.tictactoe;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.action.impl.GamePostProcessorListener;
import com.gogomaya.server.game.action.impl.VerificationGameProcessorListener;
import com.gogomaya.server.game.active.time.GameTimeManagementService;
import com.gogomaya.server.game.active.time.GameTimeProcessorListenerFactory;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.game.construct.GameConstructionRepository;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.gogomaya.server.tictactoe.action.impl.TicTacToeProcessor;
import com.gogomaya.server.tictactoe.action.impl.TicTacToeStateFactory;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.game")
@Import(value = { GameManagementSpringConfiguration.class })
public class TicTacToeSpringConfiguration {

    @Inject
    public GameSessionRepository<TicTacToeState> sessionRepository;

    @Inject
    public GameSpecificationRepository specificationRepository;

    @Inject
    public WalletTransactionManager walletTransactionManager;

    @Inject
    private TableServerRegistry tableRegistry;

    @Inject
    public GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    public VerificationGameProcessorListener<TicTacToeState> verificationGameProcessor;

    @Inject
    public GamePostProcessorListener<TicTacToeState> postProcessorListener;

    @Inject
    public PlayerStateManager playerStateQueue;

    @Inject
    public PlayerNotificationService playerNotificationService;

    @Inject
    public GameConstructionRepository constructionRepository;

    @Inject
    public PlayerLockService playerLockService;

    @Bean
    @Singleton
    public GameTableFactory<TicTacToeState> tableFactory() {
        return new GameTableFactory<TicTacToeState>(gameStateFactory(), tableRepository, tableRegistry);
    }

    @Bean
    @Singleton
    public GameConstructionService gameConstructionService() {
        return new GameConstructionService(walletTransactionManager, playerNotificationService, constructionRepository, initiatorService(), playerLockService);
    }

    @Bean
    @Singleton
    public GameInitiatorService initiatorService() {
        return new GameInitiatorService(sessionProcessor(), constructionRepository);
    }

    @Bean
    @Singleton
    public TicTacToeStateFactory gameStateFactory() {
        return new TicTacToeStateFactory(ticTacToeProcessorFactory());
    }

    @Bean
    @Singleton
    public TicTacToeConfigurationManager configurationManager() {
        return new TicTacToeConfigurationManager(specificationRepository);
    }

    @Bean()
    @Singleton
    public GameProcessorFactory<TicTacToeState> ticTacToeProcessorFactory() {
        return new GameProcessorFactory<TicTacToeState>(new TicTacToeProcessor(), verificationGameProcessor, postProcessorListener);
    }

    @Bean
    @Singleton
    public GameTimeProcessorListenerFactory<TicTacToeState> timeProcessorListenerFactory() {
        GameTimeProcessorListenerFactory<TicTacToeState> timeProcessorListenerFactory = new GameTimeProcessorListenerFactory<TicTacToeState>(
                timeManagementService());
        ticTacToeProcessorFactory().setTimeListenerFactory(timeProcessorListenerFactory);
        return timeProcessorListenerFactory;
    }

    @Bean
    @Singleton
    public GameCacheService<TicTacToeState> cacheService() {
        return new GameCacheService<TicTacToeState>(sessionRepository, ticTacToeProcessorFactory(), gameStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<TicTacToeState> sessionProcessor() {
        return new GameSessionProcessor<TicTacToeState>(tableFactory(), cacheService(), playerNotificationService);
    }

    @Bean
    @Singleton
    public GameTimeManagementService<TicTacToeState> timeManagementService() {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("Timeout Management - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new GameTimeManagementService<TicTacToeState>(sessionProcessor(), executorService);
    }

}
