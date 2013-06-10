package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.action.GameCacheService;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.action.impl.TicTacToeProcessor;
import com.gogomaya.server.game.action.impl.TicTacToeStateFactory;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.active.RedisActivePlayerQueue;
import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.game.match.GameConstructionServiceImpl;
import com.gogomaya.server.game.notification.GameNotificationService;
import com.gogomaya.server.game.notification.RabbitGameNotificationService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.tictactoe.TicTacToeState;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.game")
@Import(value = { GameManagementSpringConfiguration.class, TicTacToeSpringConfiguration.Test.class })
public class TicTacToeSpringConfiguration {

    @Inject
    public PendingSessionQueue pendingSessionQueue;

    @Inject
    public GameSessionRepository<TicTacToeState> sessionRepository;

    @Inject
    public GameSpecificationRepository specificationRepository;

    @Inject
    public GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    public Jackson2JsonMessageConverter jsonMessageConverter;

    @Inject
    public PlayerNotificationRegistry notificationRegistry;

    @Inject
    public WalletTransactionManager walletTransactionManager;

    @Inject
    @Named("playerQueueTemplate")
    public RedisTemplate<Long, Long> playerQueueTemplate;

    @Inject
    private TableServerRegistry tableRegistry;

    @Bean
    @Singleton
    public ActivePlayerQueue activePlayerQueue() {
        return new RedisActivePlayerQueue(playerQueueTemplate);
    }

    @Bean
    @Singleton
    public GameNotificationService<TicTacToeState> gameNotificationManager() {
        return new RabbitGameNotificationService<TicTacToeState>(jsonMessageConverter, notificationRegistry);
    }

    @Bean
    @Singleton
    public GameConstructionServiceImpl<TicTacToeState> stateManager() {
        return new GameConstructionServiceImpl<TicTacToeState>(pendingSessionQueue, tableRepository, gameNotificationManager(), tableFactory(),
                walletTransactionManager, activePlayerQueue(), tableRegistry);
    }

    @Bean
    @Singleton
    public GameTableFactory<TicTacToeState> tableFactory() {
        return new GameTableFactory<TicTacToeState>(gameStateFactory(), tableRepository, sessionRepository, tableRegistry);
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

    @Bean
    @Singleton
    public GameProcessorFactory<TicTacToeState> ticTacToeProcessorFactory() {
        return new GameProcessorFactory<TicTacToeState>(activePlayerQueue(), walletTransactionManager, new TicTacToeProcessor(), pendingSessionQueue);
    }

    @Bean
    @Singleton
    public GameCacheService<TicTacToeState> cacheService() {
        return new GameCacheService<TicTacToeState>(sessionRepository, ticTacToeProcessorFactory(), gameStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<TicTacToeState> sessionProcessor() {
        return new GameSessionProcessor<TicTacToeState>(cacheService(), gameNotificationManager());
    }

    @Configuration
    @Profile(value = { "test" })
    public static class Test {

        @Bean
        @Singleton
        public WalletTransactionManager walletTransactionManager() {
            return new WalletTransactionManager() {
                @Override
                public void process(WalletTransaction walletTransaction) {
                }

                @Override
                public boolean canAfford(WalletOperation walletOperation) {
                    return true;
                }
            };
        }

    }

}
