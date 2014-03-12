package com.clemble.casino.server.spring.web.game;

import com.clemble.casino.game.*;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.action.GameStateFactoryFacade;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.game.aspect.TournamentGameAspectFactory;
import com.clemble.casino.server.game.configuration.ServerGameConfigurationService;
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.game.construction.auto.ServerAutoGameConstructionService;
import com.clemble.casino.server.game.construction.availability.PendingPlayerCreationEventListener;
import com.clemble.casino.server.game.construction.availability.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.game.*;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.game.options.GameConfigurationController;
import com.clemble.casino.server.web.game.session.AutoGameConstructionController;
import com.clemble.casino.server.web.game.session.AvailabilityGameConstructionController;
import com.clemble.casino.server.web.game.session.GameActionController;
import com.clemble.casino.server.web.game.session.GameInitiationController;
import com.clemble.casino.server.web.game.session.GameRecordController;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GameManagementSpringConfiguration.class, WebCommonSpringConfiguration.class})
abstract public class AbstractGameSpringConfiguration<State extends GameState> implements SpringConfiguration {

    @Bean
    public GameManagerFactory gameProcessor(
        GameStateFactoryFacade stateFactory,
        @Qualifier("roundProcessorFactory") ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> matchProcessorFactory,
        @Qualifier("matchProcessorFactory") ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> potProcessorFactory,
        @Qualifier("tournamentProcessorFactory") ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentProcessorFactory,
        GameRecordRepository roundRecordRepository,
        ServerGameConfigurationRepository configurationRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactory(stateFactory, matchProcessorFactory, potProcessorFactory, tournamentProcessorFactory, roundRecordRepository, configurationRepository, notificationService);
    }

    @Bean
    public GameInitiationController gameInitiationController(ServerGameInitiationService initiationService) {
        return new GameInitiationController(initiationService);
    }

    @Bean
    public PendingPlayerCreationEventListener pendingPlayerCreationListener(PendingPlayerRepository playerRepository,
                                                                            SystemNotificationServiceListener notificationServiceListener) {
        PendingPlayerCreationEventListener initiationListener = new PendingPlayerCreationEventListener(playerRepository);
        notificationServiceListener.subscribe(initiationListener);
        return initiationListener;
    }

    @Bean
    public ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundProcessorFactory() {
        return new ServerGameManagerFactory<>(RoundGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchProcessorFactory() {
        return new ServerGameManagerFactory<>(MatchGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentProcessorFactory() {
        return new ServerGameManagerFactory<>(TournamentGameAspectFactory.class);
    }

    @Bean
    public GameConfigurationController gameConfigurationController(ServerGameConfigurationService configurationService) {
        return new GameConfigurationController(configurationService);
    }

    @Bean
    public GameActionController<State> picPacPoeEngineController(
        GameManagerFactory sessionProcessor,
        GameRecordRepository roundGameRecordRepository) {
        return new GameActionController<>(roundGameRecordRepository, sessionProcessor);
    }

    @Bean
    public AutoGameConstructionController<State> autoGameConstructionController(
        ServerAutoGameConstructionService constructionService) {
        return new AutoGameConstructionController<>(constructionService);
    }

    @Bean
    public AvailabilityGameConstructionController availabilityGameConstructionController(
        ServerAvailabilityGameConstructionService constructionService,
        GameConstructionRepository constructionRepository) {
        return new AvailabilityGameConstructionController(constructionService, constructionRepository);
    }

    @Bean
    public GameRecordController gameRecordController(GameRecordRepository roundGameRecordRepository) {
        return new GameRecordController(roundGameRecordRepository);
    }

}
