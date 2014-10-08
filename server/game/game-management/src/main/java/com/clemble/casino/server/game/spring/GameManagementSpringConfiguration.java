package com.clemble.casino.server.game.spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.management.TournamentGameContext;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.TournamentGameConfiguration;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.game.action.*;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.game.aspect.TournamentGameAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.match.MatchFillAspectFactory;
import com.clemble.casino.server.game.aspect.record.GameRecordAspectFactory;
import com.clemble.casino.server.game.aspect.security.MatchGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.security.RoundGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.unit.GamePlayerUnitAspectFactory;
import com.clemble.casino.server.game.controller.GameActionController;
import com.clemble.casino.server.game.controller.GameRecordServiceController;
import com.clemble.casino.server.game.listener.ServerGameStartedEventListener;
import com.clemble.casino.server.game.repository.*;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.game.aspect.bet.BetRuleAspectFactory;
import com.clemble.casino.server.game.aspect.notification.PlayerNotificationRuleAspectFactory;
import com.clemble.casino.server.game.aspect.next.NextGameAspectFactory;
import com.clemble.casino.server.game.aspect.presence.GameEndPresenceAspectFactory;
import com.clemble.casino.server.game.aspect.time.GameTimeAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

@Configuration
@Import(value = {
    CommonSpringConfiguration.class,
    GameManagementSpringConfiguration.GameMongoSpringConfiguration.class,
    GameManagementSpringConfiguration.GameAspectSpringConfiguration.class,
    GameManagementSpringConfiguration.GameManagementControllerSpringConfiguration.class,
    PresenceServiceSpringConfiguration.class
})
public class GameManagementSpringConfiguration implements SpringConfiguration {

    public static class GameAspectSpringConfiguration implements SpringConfiguration {
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
        public GameEndPresenceAspectFactory gamePresenceAspectFactory(ServerPlayerPresenceService presenceService) {
            return new GameEndPresenceAspectFactory(presenceService);
        }

        @Bean
        public PlayerNotificationRuleAspectFactory gameNotificationManagementAspectFactory(PlayerNotificationService playerNotificationService) {
            return new PlayerNotificationRuleAspectFactory(playerNotificationService);
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
        public GameRecordAspectFactory gameRecordEventAspectFactory(GameRecordRepository recordRepository) {
            return new GameRecordAspectFactory(recordRepository);
        }

        /**
         * Needed to separate this way, since BeanPostProcessor is loaded prior to any other configuration, Spring tries to load whole configuration, but some
         * dependencies are naturally missing - like Repositories
         *
         * @author mavarazy
         *
         */
        @Bean
        public GameTimeAspectFactory gameTimeAspectFactory(@Qualifier("gameEventTaskExecutor") EventTaskExecutor gameEventTaskExecutor) {
            return new GameTimeAspectFactory(gameEventTaskExecutor);
        }

    }

    /**
     * Created by mavarazy on 8/20/14.
     */
    @Configuration
    @Import(MongoSpringConfiguration.class)
    public static class GameMongoSpringConfiguration implements SpringConfiguration {

        @Bean
        public GameRecordRepository gameRecordRepository(MongoRepositoryFactory mongoRepositoryFactory) {
            return mongoRepositoryFactory.getRepository(GameRecordRepository.class);
        }

    }


    @Bean
    public GameStateFactoryFacade gameStateFactoryFacade() {
        return new GameStateFactoryFacade();
    }

    @Bean
    public EventTaskAdapter gameEventTaskAdapter(GameManagerFactory managerFactory, SystemNotificationService notificationService){
        return new GameEventTaskAdapter(managerFactory, notificationService);
    }

    @Bean
    public EventTaskExecutor gameEventTaskExecutor(@Qualifier("gameEventTaskAdapter") EventTaskAdapter gameEventTaskAdapter) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL game:event:executor - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new EventTaskExecutor(gameEventTaskAdapter, executorService);
    }

    @Bean
    public GameManagerFactory gameProcessor(
        GameStateFactoryFacade stateFactory,
        ServerGameManagerFactory<RoundGameConfiguration, RoundGameState, RoundGameContext> roundGameManagerFactory,
        ServerGameManagerFactory<MatchGameConfiguration, MatchGameState, MatchGameContext> matchGameManagerFactory,
        ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameState, TournamentGameContext> tournamentGameManagerFactory,
        GameRecordRepository roundRecordRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactory(stateFactory, roundGameManagerFactory, matchGameManagerFactory, tournamentGameManagerFactory, roundRecordRepository, notificationService);
    }

    @Bean
    public ServerGameManagerFactory<RoundGameConfiguration, RoundGameState, RoundGameContext> roundGameManagerFactory() {
        return new ServerGameManagerFactory<>(RoundGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<MatchGameConfiguration, MatchGameState, MatchGameContext> matchGameManagerFactory() {
        return new ServerGameManagerFactory<>(MatchGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameState, TournamentGameContext> tournamentGameManagerFactory() {
        return new ServerGameManagerFactory<>(TournamentGameAspectFactory.class);
    }

    @Bean
    public ServerGameStartedEventListener serverGameStartedEventListener(
        GameManagerFactory managerFactory,
        SystemNotificationServiceListener notificationServiceListener,
        ServerPlayerPresenceService presenceService,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        ServerGameStartedEventListener eventListener = new ServerGameStartedEventListener(managerFactory, presenceService, notificationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Configuration
    public static class GameManagementControllerSpringConfiguration implements SpringConfiguration {

        @Bean
        public GameActionController picPacPoeEngineController(
            GameManagerFactory sessionProcessor,
            GameRecordRepository recordRepository) {
            return new GameActionController(recordRepository, sessionProcessor);
        }

        @Bean
        public GameRecordServiceController gameRecordController(GameRecordRepository recordRepository) {
            return new GameRecordServiceController(recordRepository);
        }

    }

}
