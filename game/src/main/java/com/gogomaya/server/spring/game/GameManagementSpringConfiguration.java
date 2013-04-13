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
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.connection.RabbitGameNotificationManager;
import com.gogomaya.server.game.connection.SimpleGameServerConnectionManager;
import com.gogomaya.server.game.match.TicTacToeSpecificationRepository;
import com.gogomaya.server.game.match.TicTacToeStateManager;
import com.gogomaya.server.game.session.TicTacToeSessionRepository;
import com.gogomaya.server.game.table.TicTacToeTableManager;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.game")
@Import(value = { CommonModuleSpringConfiguration.class, GameManagementSpringConfiguration.GameManagementTestConfiguration.class,
        GameManagementSpringConfiguration.GameManagementCloudConfiguration.class })
public class GameManagementSpringConfiguration {

    @Inject
    public TicTacToeSessionRepository sessionRepository;

    @Inject
    public TicTacToeSpecificationRepository specificationRepository;

    @Inject
    public RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public TicTacToeTableRepository tableRepository;

    @Inject
    public GameServerConnectionManager serverConnectionManager;

    @Inject
    public JsonMessageConverter jsonMessageConverter;

    @Bean
    @Singleton
    public GameNotificationManager gameNotificationManager() {
        return new RabbitGameNotificationManager(jsonMessageConverter);
    }

    @Bean
    @Singleton
    public TicTacToeTableManager tableManager() {
        return new TicTacToeTableManager(redisTemplate, tableRepository, serverConnectionManager);
    }

    @Bean
    @Singleton
    public TicTacToeStateManager stateManager() {
        return new TicTacToeStateManager(tableManager(), tableRepository, sessionRepository, gameNotificationManager(), gameStateFactory());
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

    @Profile(value = { "default", "test" })
    public static class GameManagementTestConfiguration {

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("localhost", "localhost");
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
