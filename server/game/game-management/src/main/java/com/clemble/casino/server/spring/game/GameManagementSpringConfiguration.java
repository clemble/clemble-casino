package com.clemble.casino.server.spring.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.clemble.casino.server.game.aspect.bet.BetRuleAspectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.game.id.UUIDGameIdGenerator;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.game.aspect.management.GameNotificationManagementAspectFactory;
import com.clemble.casino.server.game.aspect.management.GameSequenceManagementAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.DrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.WonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.presence.GamePresenceAspectFactory;
import com.clemble.casino.server.game.aspect.price.GamePriceAspectFactory;
import com.clemble.casino.server.game.aspect.security.GameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.time.GameTimeAspectFactory;
import com.clemble.casino.server.game.configuration.GameSpecificationRegistry;
import com.clemble.casino.server.game.construct.GameConstructionServerService;
import com.clemble.casino.server.game.construct.GameInitiatorService;
import com.clemble.casino.server.game.construct.SimpleGameConstructionServerService;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
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
    @Autowired
    public WonRuleAspectFactory wonRuleAspectFactory(PaymentTransactionServerService paymentTransactionService) {
        return new WonRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    @Autowired
    public DrawRuleAspectFactory drawRuleAspectFactory(PaymentTransactionServerService paymentTransactionService) {
        return new DrawRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    @Autowired
    public GamePresenceAspectFactory gamePresenceAspectFactory(PlayerPresenceServerService presenceService){
        return new GamePresenceAspectFactory(presenceService);
    }

    @Bean
    @Autowired
    public GameManagementAspecteFactory gameManagementAspectFactory(GameIdGenerator idGenerator, GameInitiatorService initiatorService, GameConstructionRepository constructionRepository) {
        return new GameSequenceManagementAspectFactory(idGenerator, initiatorService, constructionRepository);
    }
    
    @Bean
    @Autowired
    public GameNotificationManagementAspectFactory gameNotificationManagementAspectFactory(PlayerNotificationService playerNotificationService){
        return new GameNotificationManagementAspectFactory(playerNotificationService);
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
        public GameTimeAspectFactory gameTimeAspectFactory() {
            return new GameTimeAspectFactory(eventTaskExecutor());
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
        public GameConstructionServerService gameConstructionService(GameIdGenerator gameIdGenerator, PlayerAccountServerService playerAccountService,
                PlayerNotificationService playerNotificationService, GameConstructionRepository constructionRepository,
                GameInitiatorService initiatorService, PlayerLockService playerLockService, PlayerPresenceServerService playerStateManager) {
            return new SimpleGameConstructionServerService(gameIdGenerator, playerAccountService, playerNotificationService, constructionRepository,
                    initiatorService, playerLockService, playerStateManager);
        }

        @Bean
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

}
