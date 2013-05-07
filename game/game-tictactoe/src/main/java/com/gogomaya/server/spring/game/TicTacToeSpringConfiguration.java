package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.connection.RabbitGameNotificationService;
import com.gogomaya.server.game.connection.SimpleGameServerConnectionManager;
import com.gogomaya.server.game.match.GameMatchingServiceImpl;
import com.gogomaya.server.game.outcome.TicTacToeOutcomeService;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableManagerImpl;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
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
    public GameSessionRepository sessionRepository;

    @Inject
    public GameSpecificationRepository specificationRepository;

    @Inject
    public RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    public GameServerConnectionManager serverConnectionManager;

    @Inject
    public JsonMessageConverter jsonMessageConverter;

    @Inject
    public WalletTransactionManager walletTransactionManager;

    @Bean
    @Singleton
    public GameNotificationService gameNotificationManager() {
        return new RabbitGameNotificationService(jsonMessageConverter);
    }

    @Bean
    @Singleton
    public GameTableManager<TicTacToeState> tableManager() {
        return new GameTableManagerImpl<TicTacToeState>(redisTemplate, tableRepository, serverConnectionManager);
    }

    @Bean
    @Singleton
    public GameMatchingServiceImpl<TicTacToeState> stateManager() {
        return new GameMatchingServiceImpl<TicTacToeState>(tableManager(), tableRepository, sessionRepository, gameNotificationManager(), gameStateFactory());
    }

    @Bean
    @Singleton
    public TicTacToeStateFactory gameStateFactory() {
        return new TicTacToeStateFactory();
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
