package com.gogomaya.server.spring.game;

import javax.inject.Inject;
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
import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.connection.RabbitGameNotificationService;
import com.gogomaya.server.game.connection.SimpleGameServerConnectionManager;
import com.gogomaya.server.game.match.GameMatchingServiceImpl;
import com.gogomaya.server.game.outcome.TicTacToeOutcomeService;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.table.RedisGameTableQueue;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.game")
@Import(value = { CommonModuleSpringConfiguration.class, TicTacToeSpringConfiguration.GameManagementDefaultConfiguration.class,
        TicTacToeSpringConfiguration.GameManagementCloudConfiguration.class })
public class TicTacToeSpringConfiguration {

    @Inject
    public GameSessionRepository<TicTacToeState> sessionRepository;

    @Inject
    public GameSpecificationRepository specificationRepository;

    @Inject
    public RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    public GameServerConnectionManager serverConnectionManager;

    @Inject
    public Jackson2JsonMessageConverter jsonMessageConverter;

    @Inject
    public WalletTransactionManager walletTransactionManager;

    @Bean
    @Singleton
    public GameNotificationService<TicTacToeState> gameNotificationManager() {
        return new RabbitGameNotificationService<TicTacToeState>(jsonMessageConverter);
    }

    @Bean
    @Singleton
    public GameTableQueue<TicTacToeState> tableManager() {
        return new RedisGameTableQueue<TicTacToeState>(redisTemplate);
    }

    @Bean
    @Singleton
    public GameMatchingServiceImpl<TicTacToeState> stateManager() {
        return new GameMatchingServiceImpl<TicTacToeState>(tableManager(), tableRepository, gameNotificationManager(), tableFactory());
    }

    @Bean
    @Singleton
    public GameTableFactory<TicTacToeState> tableFactory() {
        return new GameTableFactory<TicTacToeState>(gameStateFactory(), serverConnectionManager, tableRepository, sessionRepository);
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
    public TicTacToeOutcomeService outcomeService() {
        return new TicTacToeOutcomeService(walletTransactionManager);
    }

    @Bean
    @Singleton
    public GameProcessorFactory<TicTacToeState> ticTacToeProcessorFactory() {
        return new GameProcessorFactory<TicTacToeState>(new TicTacToeProcessor());
    }

    @Bean
    @Singleton
    public GameCacheService<TicTacToeState> cacheService() {
        return new GameCacheService<TicTacToeState>(sessionRepository, tableRepository, ticTacToeProcessorFactory(), gameStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<TicTacToeState> sessionProcessor() {
        return new GameSessionProcessor<TicTacToeState>(outcomeService(), cacheService(), gameNotificationManager());
    }

    @Profile(value = { "default" })
    public static class GameManagementDefaultConfiguration {

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("localhost", "localhost");
        }

    }

    @Profile(value = "test")
    public static class GameManagementTestConfiguration {

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("localhost", "localhost");
        }

        @Bean
        @Singleton
        public WalletTransactionManager walletTransactionManager() {
            return new WalletTransactionManager() {
                @Override
                public void process(WalletTransaction walletTransaction) {
                }
            };
        }

    }

    @Profile(value = { "cloud" })
    public static class GameManagementCloudConfiguration {

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("ec2-50-16-93-157.compute-1.amazonaws.com", "gogomaya.cloudfoundry.com");
        }

    }

}
