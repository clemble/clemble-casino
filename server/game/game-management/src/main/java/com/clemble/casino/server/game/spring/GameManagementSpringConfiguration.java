package com.clemble.casino.server.game.spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.clemble.casino.base.ActionLatchService;
import com.clemble.casino.base.JavaActionLatchService;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.server.game.GameSessionKeyGenerator;
import com.clemble.casino.server.game.aspect.outcome.MatchDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.pot.MatchFillAspectFactory;
import com.clemble.casino.server.game.aspect.record.RoundGameRecordAspectFactory;
import com.clemble.casino.server.game.aspect.security.MatchGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.security.RoundGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.unit.GamePlayerUnitAspectFactory;
import com.clemble.casino.server.game.repository.*;
import com.clemble.casino.server.id.RedisKeyFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
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
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.game.construction.auto.ServerAutoGameConstructionService;
import com.clemble.casino.server.game.construction.availability.PendingGameInitiationEventListener;
import com.clemble.casino.server.game.construction.availability.PendingPlayerCreationEventListener;
import com.clemble.casino.server.game.construction.availability.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import redis.clients.jedis.JedisPool;

@Configuration
@Import(value = { CommonSpringConfiguration.class,
    PresenceServiceSpringConfiguration.class,
    GameNeo4jSpringConfiguration.class,
    GameMongoSpringConfiguration.class,
    PaymentClientSpringConfiguration.class,
    RedisSpringConfiguration.class
})
public class GameManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public GameSessionKeyGenerator gameSessionKeyGenerator(JedisPool jedisPool) {
        return new GameSessionKeyGenerator(new RedisKeyFactory("GAME_COUNTER", "G", jedisPool));
    }

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
    public ServerAutoGameConstructionService serverAutoGameConstructionService(final @Qualifier("gameSessionKeyGenerator") GameSessionKeyGenerator sessionKeyGenerator,
            final ServerGameInitiationService initiatorService, final GameConstructionRepository constructionRepository,
            final PlayerLockService playerLockService, final ServerPlayerPresenceService playerStateManager) {
        return new ServerAutoGameConstructionService(sessionKeyGenerator, initiatorService, constructionRepository, playerLockService, playerStateManager);
    }

    @Bean
    public ActionLatchService constructionActionLatchService() {
        return new JavaActionLatchService();
    }

    @Bean
    public ServerAvailabilityGameConstructionService serverAvailabilityGameConstructionService(
        @Qualifier("constructionActionLatchService") ActionLatchService constructionActionLatchService,
        @Qualifier("gameSessionKeyGenerator") GameSessionKeyGenerator sessionKeyGenerator,
        @Qualifier("playerAccountClient") PlayerAccountServiceContract accountServerService,
        GameConstructionRepository constructionRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService,
        PendingGameInitiationEventListener pendingInitiationService) {
        return new ServerAvailabilityGameConstructionService(constructionActionLatchService, sessionKeyGenerator, accountServerService, constructionRepository, notificationService, pendingInitiationService);
    }

    @Bean
    public PendingPlayerCreationEventListener pendingPlayerCreationEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        PendingPlayerRepository playerRepository) {
        PendingPlayerCreationEventListener playerEventListener = new PendingPlayerCreationEventListener(playerRepository);
        notificationServiceListener.subscribe(playerEventListener);
        return playerEventListener;
    }

    @Bean
    public PendingGameInitiationEventListener pendingGameInitiationEventListener(PendingPlayerRepository playerRepository,
            PendingGameInitiationRepository initiationRepository, ServerPlayerPresenceService presenceService, ServerGameInitiationService initiationService,
            SystemNotificationServiceListener notificationServiceListener) {
        PendingGameInitiationEventListener eventListener = new PendingGameInitiationEventListener(playerRepository, initiationRepository, presenceService,
                initiationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public ServerGameInitiationService serverGameInitiationActivator(GameManagerFactory processor, ServerPlayerPresenceService presenceService,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new ServerGameInitiationService(processor, presenceService, notificationService);
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
