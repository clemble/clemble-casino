package com.gogomaya.server.spring.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.event.Event;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.server.LongServerRegistry;
import com.gogomaya.server.game.action.GameEventTaskExecutor;
import com.gogomaya.server.game.aspect.bet.GameBetAspectFactory;
import com.gogomaya.server.game.aspect.outcome.GameOutcomeAspectFactory;
import com.gogomaya.server.game.aspect.price.GamePriceAspectFactory;
import com.gogomaya.server.game.aspect.security.GameSecurityAspectFactory;
import com.gogomaya.server.game.aspect.time.GameTimeAspectFactory;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.game.construct.GameConstructionServerService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.construct.SimpleGameConstructionServerService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class,
        GameManagementSpringConfiguration.GameTimeAspectConfiguration.class, GameManagementSpringConfiguration.DefaultAndTest.class, GameManagementSpringConfiguration.Cloud.class, GameManagementSpringConfiguration.Test.class })
public class GameManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("paymentTransactionService")
    public PaymentTransactionServerService paymentTransactionService;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerPresenceServerService playerStateManager;

    @Bean
    @Singleton
    public GamePriceAspectFactory gamePriceAspectFactory() {
        return new GamePriceAspectFactory();
    }

    @Bean
    @Singleton
    public GameBetAspectFactory gameBetAspectFactory() {
        return new GameBetAspectFactory();
    }

    @Bean
    @Singleton
    public GameSecurityAspectFactory gameSecurityAspectFactory() {
        return new GameSecurityAspectFactory();
    }

    @Bean
    @Singleton
    public GameOutcomeAspectFactory gameOutcomeAspectFactory() {
        return new GameOutcomeAspectFactory(paymentTransactionService, playerStateManager);
    }

    @Bean
    @Singleton
    public GameSpecificationRegistry gameSpecificationRegistry() {
        return new GameSpecificationRegistry();
    }

    /**
     * Needed to separate this way, since BeanPostProcessor is loaded prior to any other configuration,
     * Spring tries to load whole configuration, but some dependencies are naturally missing - like Repositories
     * 
     * @author mavarazy
     *
     */
    @Configuration
    public static class GameTimeAspectConfiguration {

        @Bean
        @Singleton
        public GameTimeAspectFactory gameTimeAspectFactory() {
            return new GameTimeAspectFactory(eventTaskExecutor());
        }

        @Bean
        @Singleton
        public GameEventTaskExecutor eventTaskExecutor() {
            ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("[GETE] task-executor - %d");
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
            return new GameEventTaskExecutor(executorService);
        }
    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            LongServerRegistry serverRegistry = new LongServerRegistry();
            serverRegistry.register(10_000L, "localhost");
            return new TableServerRegistry(serverRegistry);
        }

    }

    @Configuration
    @Profile(value = { UNIT_TEST })
    public static class Test {

        @Autowired
        public PlayerLockService playerLockService;

        @Autowired
        public PlayerPresenceServerService playerStateManager;

        @Autowired
        public PlayerNotificationService<Event> playerNotificationService;

        @Autowired
        public GameConstructionRepository constructionRepository;

        @Autowired
        public PlayerAccountServerService playerAccountService;

        @Bean
        @Singleton
        public GameConstructionServerService gameConstructionService() {
            return new SimpleGameConstructionServerService(playerAccountService, playerNotificationService, constructionRepository, initiatorService(),
                    playerLockService, playerStateManager);
        }

        @Bean
        @Singleton
        public GameInitiatorService initiatorService() {
            return new GameInitiatorService() {
                @Override
                public void initiate(GameConstruction construction) {
                }

                @Override
                public boolean initiate(GameInitiation initiation) {
                    return true;
                }
            };
        }

    }

    @Configuration
    @Profile(value = { CLOUD })
    public static class Cloud {

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            LongServerRegistry serverRegistry = new LongServerRegistry();
            serverRegistry.register(10_000L, "gogomaya.cloudfoundry.com");
            return new TableServerRegistry(serverRegistry);
        }

    }

}
