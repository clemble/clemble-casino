package com.gogomaya.server.spring.game;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.ServerRegistry;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.impl.GamePostProcessorListener;
import com.gogomaya.server.game.action.impl.VerificationGameProcessorListener;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, GameManagementSpringConfiguration.DefaultAndTest.class,
        GameManagementSpringConfiguration.Cloud.class, GameManagementSpringConfiguration.Test.class })
public class GameManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("walletTransactionManager")
    public WalletTransactionManager walletTransactionManager;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Bean
    @Singleton
    public <State extends GameState> VerificationGameProcessorListener<State> verificationGameProcessorListener() {
        return new VerificationGameProcessorListener<State>();
    }

    @Bean
    @Singleton
    public <State extends GameState> GamePostProcessorListener<State> gamePostProcessorListener() {
        return new GamePostProcessorListener<State>(playerStateManager, walletTransactionManager);
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

                @Override
                public boolean canAfford(long playerId, Money ammount) {
                    return true;
                }

                @Override
                public boolean canAfford(Collection<Long> playerId, Money ammount) {
                    return true;
                }
            };
        }

    }

    @Profile(value = { "cloud" })
    public static class Cloud {

        @Autowired
        @Qualifier("tableQueueTemplate")
        public RedisTemplate<byte[], Long> redisTemplate;

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(10_000L, "gogomaya.cloudfoundry.com");
            return new TableServerRegistry(serverRegistry);
        }

    }

}
