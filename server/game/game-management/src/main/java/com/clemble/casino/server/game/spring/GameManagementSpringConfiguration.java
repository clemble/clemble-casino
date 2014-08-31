package com.clemble.casino.server.game.spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.clemble.casino.server.game.aspect.outcome.MatchDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.pot.MatchFillAspectFactory;
import com.clemble.casino.server.game.aspect.record.RoundGameRecordAspectFactory;
import com.clemble.casino.server.game.aspect.security.MatchGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.security.RoundGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.unit.GamePlayerUnitAspectFactory;
import com.clemble.casino.server.game.construction.ServerGameInitiationService;
import com.clemble.casino.server.game.construction.ServerGameReadyEventListener;
import com.clemble.casino.server.game.repository.*;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.action.GameStateFactoryFacade;
import com.clemble.casino.server.game.aspect.bet.BetRuleAspectFactory;
import com.clemble.casino.server.game.aspect.notification.PlayerNotificationRuleAspectFactory;
import com.clemble.casino.server.game.aspect.next.NextGameAspectFactory;
import com.clemble.casino.server.game.aspect.presence.GameEndPresenceAspectFactory;
import com.clemble.casino.server.game.aspect.time.GameTimeAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
@Import(value = {
    CommonSpringConfiguration.class,
    GameMongoSpringConfiguration.class,
    PresenceServiceSpringConfiguration.class
})
public class GameManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public BetRuleAspectFactory gameBetAspectFactory() {
        return new BetRuleAspectFactory();
    }

    @Bean
    public RoundGameSecurityAspectFactory gameMatchSecurityAspectFactory() {
        return new RoundGameSecurityAspectFactory();
    }

    @Bean
    public MatchGameSecurityAspectFactory gamePotSecurityAspectFactory() {
        return new MatchGameSecurityAspectFactory();
    }

    @Bean
    public RoundWonRuleAspectFactory wonRuleAspectFactory(SystemNotificationService paymentTransactionService) {
        return new RoundWonRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public RoundDrawRuleAspectFactory drawRuleAspectFactory(SystemNotificationService paymentTransactionService) {
        return new RoundDrawRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public MatchWonRuleAspectFactory potWonRuleAspectFactory(SystemNotificationService paymentTransactionService) {
        return new MatchWonRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public MatchDrawRuleAspectFactory potDrawRuleAspectFactory(SystemNotificationService paymentTransactionService) {
        return new MatchDrawRuleAspectFactory(paymentTransactionService);
    }

    @Bean
    public GameStateFactoryFacade gameStateFactoryFacade() {
        return new GameStateFactoryFacade();
    }

    @Bean
    public GameEndPresenceAspectFactory gamePresenceAspectFactory(ServerPlayerPresenceService presenceService) {
        return new GameEndPresenceAspectFactory(presenceService);
    }

    @Bean
    public PlayerNotificationRuleAspectFactory gameNotificationManagementAspectFactory(PlayerNotificationService playerNotificationService) {
        return new PlayerNotificationRuleAspectFactory(playerNotificationService);
    }
    @Bean
    public ServerGameInitiationService serverGameInitiationActivator(
        GameManagerFactory processor,
        ServerPlayerPresenceService presenceService,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new ServerGameInitiationService(processor, presenceService, notificationService);
    }

    @Bean
    public ServerGameReadyEventListener serverGameReadyEventListener(
        ServerGameInitiationService serverGameInitiationService,
        SystemNotificationServiceListener notificationServiceListener) {
        ServerGameReadyEventListener eventListener = new ServerGameReadyEventListener(serverGameInitiationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public GamePlayerUnitAspectFactory gamePlayerUnitAspectFactory() {
        return new GamePlayerUnitAspectFactory();
    }

    @Bean
    public MatchFillAspectFactory potFillAspectFactory() {
        return new MatchFillAspectFactory();
    }

    @Bean
    public NextGameAspectFactory nextGameAspectFactory(GameManagerFactory managerFactory) {
        return new NextGameAspectFactory(managerFactory);
    }

    @Bean
    public RoundGameRecordAspectFactory gameRecordEventAspectFactory(GameRecordRepository recordRepository) {
        return new RoundGameRecordAspectFactory(recordRepository);
    }

    /**
     * Needed to separate this way, since BeanPostProcessor is loaded prior to any other configuration, Spring tries to load whole configuration, but some
     * dependencies are naturally missing - like Repositories
     * 
     * @author mavarazy
     * 
     */
    @Bean
    public GameTimeAspectFactory gameTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
        return new GameTimeAspectFactory(eventTaskExecutor);
    }

    @Bean
    public GameEventTaskExecutor eventTaskExecutor(GameManagerFactory managerFactory) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL EventTaskExecutor - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new GameEventTaskExecutor(managerFactory, executorService);
    }

}
