package com.clemble.casino.server.spring.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.game.id.UUIDGameIdGenerator;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.bet.BetRuleAspectFactory;
import com.clemble.casino.server.game.aspect.management.NextGameConstructionAspectFactory;
import com.clemble.casino.server.game.aspect.management.PlayerNotificationRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.DrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.WonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.presence.GameEndPresenceAspectFactory;
import com.clemble.casino.server.game.aspect.price.GamePriceAspectFactory;
import com.clemble.casino.server.game.aspect.security.GameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.time.GameTimeAspectFactory;
import com.clemble.casino.server.game.configuration.GameSpecificationRegistry;
import com.clemble.casino.server.game.construct.BasicServerGameConstructionService;
import com.clemble.casino.server.game.construct.ServerGameConstructionService;
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentCommonSpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
@Import(value = { CommonSpringConfiguration.class, GameJPASpringConfiguration.class, PaymentCommonSpringConfiguration.class,
        PlayerCommonSpringConfiguration.class, GameManagementSpringConfiguration.GameTimeAspectConfiguration.class,
        GameManagementSpringConfiguration.Test.class })
public class GameManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public GameIdGenerator gameIdGenerator() {
        return new UUIDGameIdGenerator();
    }

    @Bean
    public GamePriceAspectFactory gamePriceAspectFactory() {
        return new GamePriceAspectFactory();
    }

    @Bean
    public BetRuleAspectFactory gameBetAspectFactory() {
        return new BetRuleAspectFactory();
    }

    @Bean
    public GameSecurityAspectFactory gameSecurityAspectFactory() {
        return new GameSecurityAspectFactory();
    }

    @Bean
    public WonRuleAspectFactory wonRuleAspectFactory(ServerPaymentTransactionService paymentTransactionService) {
        return new WonRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public DrawRuleAspectFactory drawRuleAspectFactory(ServerPaymentTransactionService paymentTransactionService) {
        return new DrawRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public GameEndPresenceAspectFactory gamePresenceAspectFactory(ServerPlayerPresenceService presenceService) {
        return new GameEndPresenceAspectFactory(presenceService);
    }

    // @Bean
    public GameAspectFactory<GameEndedEvent<?>> nextGameConstructionAspect(GameIdGenerator idGenerator, ServerGameInitiationService initiatorService,
            GameConstructionRepository constructionRepository) {
        return new NextGameConstructionAspectFactory(idGenerator, initiatorService, constructionRepository);
    }

    @Bean
    public PlayerNotificationRuleAspectFactory gameNotificationManagementAspectFactory(PlayerNotificationService playerNotificationService) {
        return new PlayerNotificationRuleAspectFactory(playerNotificationService);
    }

    @Bean
    public GameSpecificationRegistry gameSpecificationRegistry() {
        return new GameSpecificationRegistry();
    }

    /**
     * Needed to separate this way, since BeanPostProcessor is loaded prior to any other configuration, Spring tries to load whole configuration, but some
     * dependencies are naturally missing - like Repositories
     * 
     * @author mavarazy
     * 
     */
    @Configuration
    public static class GameTimeAspectConfiguration {

        @Bean
        @Autowired
        public GameTimeAspectFactory gameTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
            return new GameTimeAspectFactory(eventTaskExecutor);
        }

        @Bean
        public GameEventTaskExecutor eventTaskExecutor() {
            ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("[GETE] task-executor - %d");
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
            return new GameEventTaskExecutor(executorService);
        }
    }

    @Configuration
    @Profile(value = { UNIT_TEST })
    public static class Test {

        @Bean
        @Autowired
        public ServerGameConstructionService gameConstructionService(GameIdGenerator gameIdGenerator, ServerPlayerAccountService playerAccountService,
                PlayerNotificationService playerNotificationService, GameConstructionRepository constructionRepository,
                ServerGameInitiationService initiatorService, PlayerLockService playerLockService, ServerPlayerPresenceService playerStateManager) {
            return new BasicServerGameConstructionService(gameIdGenerator, playerAccountService, playerNotificationService, constructionRepository,
                    initiatorService, playerLockService, playerStateManager);
        }

        @Bean
        public ServerGameInitiationService initiatorService() {
            return new ServerGameInitiationService() {
                @Override
                public void register(GameInitiation construction) {
                }

                @Override
                public void start(GameInitiation initiation) {
                }

                @Override
                public GameInitiation ready(GameSessionKey sessionKey, String player) {
                    return null;
                }
            };
        }

    }

}
