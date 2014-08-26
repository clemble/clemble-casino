package com.clemble.casino.server.spring.web.game;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.TournamentGameContext;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.game.configuration.TournamentGameConfiguration;
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
import com.clemble.casino.server.game.repository.GameConstructionRepository;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.game.repository.PendingPlayerRepository;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.presence.controller.game.options.GameConfigurationController;
import com.clemble.casino.server.presence.controller.game.session.AutoGameConstructionController;
import com.clemble.casino.server.presence.controller.game.session.AvailabilityGameConstructionController;
import com.clemble.casino.server.presence.controller.game.session.GameActionController;
import com.clemble.casino.server.presence.controller.game.session.GameInitiationController;
import com.clemble.casino.server.presence.controller.game.session.GameRecordController;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GameManagementSpringConfiguration.class})
abstract public class AbstractGameSpringConfiguration<State extends GameState> implements SpringConfiguration {

    @Bean
    public GameManagerFactory gameProcessor(
        GameStateFactoryFacade stateFactory,
        ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundGameManagerFactory,
        ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchGameManagerFactory,
        ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentGameManagerFactory,
        GameRecordRepository roundRecordRepository,
        ServerGameConfigurationRepository configurationRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactory(stateFactory, roundGameManagerFactory, matchGameManagerFactory, tournamentGameManagerFactory, roundRecordRepository, configurationRepository, notificationService);
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
    public ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundGameManagerFactory() {
        return new ServerGameManagerFactory<>(RoundGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchGameManagerFactory() {
        return new ServerGameManagerFactory<>(MatchGameAspectFactory.class);
    }

    @Bean
    public ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentGameManagerFactory() {
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
