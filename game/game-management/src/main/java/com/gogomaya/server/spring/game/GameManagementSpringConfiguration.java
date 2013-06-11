package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.impl.GamePostProcessorListener;
import com.gogomaya.server.game.action.impl.VerificationGameProcessorListener;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.active.RedisActivePlayerQueue;
import com.gogomaya.server.game.active.time.GameTimeStateFactory;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.table.JavaPendingSessionQueue;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.game.table.RedisGameSessionQueue;
import com.gogomaya.server.notification.ServerRegistry;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@Import(value = { CommonSpringConfiguration.class, GameManagementSpringConfiguration.DefaultAndTest.class, GameManagementSpringConfiguration.Cloud.class,
        GameManagementSpringConfiguration.Test.class })
public class GameManagementSpringConfiguration {
    @Inject
    WalletTransactionManager walletTransactionManager;

    @Inject
    @Named("playerQueueTemplate")
    public RedisTemplate<Long, Long> playerQueueTemplate;

    @Inject
    public PendingSessionQueue pendingSessionQueue;

    @Bean
    @Singleton
    public ActivePlayerQueue activePlayerQueue() {
        return new RedisActivePlayerQueue(playerQueueTemplate);
    }

    @Bean
    public <State extends GameState> VerificationGameProcessorListener<State> verificationGameProcessor() {
        return new VerificationGameProcessorListener<State>();
    }

    @Bean
    public <State extends GameState> GamePostProcessorListener<State> gamePostProcessor() {
        return new GamePostProcessorListener<State>(activePlayerQueue(), walletTransactionManager, pendingSessionQueue);
    }

    @Bean
    public GameTimeStateFactory timeStateFactory() {
        return new GameTimeStateFactory();
    }

    @Profile(value = { "default", "test" })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(10000L, "localhost");
            return new TableServerRegistry(serverRegistry);
        }

        @Bean
        @Singleton
        public PendingSessionQueue gameTableQueue() {
            return new JavaPendingSessionQueue();
        }

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

    @Profile(value = { "cloud" })
    public static class Cloud {

        @Inject
        @Named("tableQueueTemplate")
        public RedisTemplate<byte[], Long> redisTemplate;

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(10000L, "gogomaya.cloudfoundry.com");
            return new TableServerRegistry(serverRegistry);
        }

        @Bean
        @Singleton
        public PendingSessionQueue gameTableQueue() {
            return new RedisGameSessionQueue(redisTemplate);
        }

    }

}
