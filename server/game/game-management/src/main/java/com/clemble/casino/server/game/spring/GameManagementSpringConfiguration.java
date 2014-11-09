package com.clemble.casino.server.game.spring;


import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.TournamentGameConfiguration;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.game.action.*;
import com.clemble.casino.server.game.aspect.*;
import com.clemble.casino.server.game.aspect.next.MatchNextGameAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.MatchWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundDrawRuleAspectFactory;
import com.clemble.casino.server.game.aspect.outcome.RoundWonRuleAspectFactory;
import com.clemble.casino.server.game.aspect.match.MatchFillAspectFactory;
import com.clemble.casino.server.game.aspect.record.GameRecordAspectFactory;
import com.clemble.casino.server.game.aspect.security.MatchGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.security.RoundGameSecurityAspectFactory;
import com.clemble.casino.server.game.aspect.time.RoundGameTimeAspectFactory;
import com.clemble.casino.server.game.aspect.unit.GamePlayerUnitAspectFactory;
import com.clemble.casino.server.game.controller.GameActionServiceController;
import com.clemble.casino.server.game.controller.GameRecordServiceController;
import com.clemble.casino.server.game.listener.ServerGameStartedEventListener;
import com.clemble.casino.server.game.listener.SystemGameTimeoutEventListener;
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
        public MatchNextGameAspectFactory matchNextGameAspectFactory(GameManagerFactoryFacade factoryFacade) {
            return new MatchNextGameAspectFactory(factoryFacade);
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
        public NextGameAspectFactory nextGameAspectFactory(GameManagerFactoryFacade managerFactory) {
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
        public RoundGameTimeAspectFactory gameTimeAspectFactory(SystemNotificationService systemNotificationService) {
            return new RoundGameTimeAspectFactory(systemNotificationService);
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
    public RoundStateFactoryFacade gameStateFactoryFacade() {
        return new RoundStateFactoryFacade();
    }

    @Bean
    public GameManagerFactoryFacade gameProcessor(
        RoundStateFactoryFacade stateFactory,
        ClembleManagerFactory<RoundGameConfiguration> roundGameManagerFactory,
        ClembleManagerFactory<MatchGameConfiguration> matchGameManagerFactory,
        ClembleManagerFactory<TournamentGameConfiguration> tournamentGameManagerFactory,
        GameRecordRepository roundRecordRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactoryFacade(stateFactory, roundGameManagerFactory, matchGameManagerFactory, tournamentGameManagerFactory, roundRecordRepository, notificationService);
    }

    @Bean
    public ClembleManagerFactory<RoundGameConfiguration> roundGameManagerFactory() {
        return new ClembleManagerFactory<>(RoundGameAspectFactory.class, GenericGameAspectFactory.class);
    }

    @Bean
    public ClembleManagerFactory<MatchGameConfiguration> matchGameManagerFactory() {
        return new ClembleManagerFactory<>(MatchGameAspectFactory.class, GenericGameAspectFactory.class);
    }

    @Bean
    public ClembleManagerFactory<TournamentGameConfiguration> tournamentGameManagerFactory() {
        return new ClembleManagerFactory<>(TournamentGameAspectFactory.class, GenericGameAspectFactory.class);
    }

    @Bean
    public ServerGameStartedEventListener serverGameStartedEventListener(
        GameManagerFactoryFacade managerFactory,
        SystemNotificationServiceListener notificationServiceListener,
        ServerPlayerPresenceService presenceService,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        ServerGameStartedEventListener eventListener = new ServerGameStartedEventListener(managerFactory, presenceService, notificationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemGameTimeoutEventListener gameTimeoutEventListener(
        GameManagerFactoryFacade managerFactory,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemGameTimeoutEventListener eventListener = new SystemGameTimeoutEventListener(managerFactory);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }


    @Configuration
    public static class GameManagementControllerSpringConfiguration implements SpringConfiguration {

        @Bean
        public GameActionServiceController picPacPoeEngineController(
            GameManagerFactoryFacade sessionProcessor,
            GameRecordRepository recordRepository) {
            return new GameActionServiceController(recordRepository, sessionProcessor);
        }

        @Bean
        public GameRecordServiceController gameRecordController(GameRecordRepository recordRepository) {
            return new GameRecordServiceController(recordRepository);
        }

    }

}
