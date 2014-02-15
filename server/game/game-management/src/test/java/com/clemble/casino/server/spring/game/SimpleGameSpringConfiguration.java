package com.clemble.casino.server.spring.game;

import com.clemble.casino.game.*;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.action.GameStateFactoryFacade;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.game.aspect.ServerGameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Ignore
@Configuration
@Import(GameManagementSpringConfiguration.class)
@SuppressWarnings({"rawtypes"})
public class SimpleGameSpringConfiguration {

    @Bean
    public GameManagerFactory gameProcessor(PotGameRecordRepository potRepository,
                                            GameStateFactoryFacade stateFactory,
                                            ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> processorFactory,
                                            ServerGameAspectFactory<PotGameConfiguration, PotGameContext, PotGameRecord> potProcessorFactory,
                                            ServerGameAspectFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentProcessorFactory,
                                            MatchGameRecordRepository sessionRepository,
                                            ServerGameConfigurationRepository configurationRepository,
                                            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactory(potRepository, stateFactory, processorFactory, potProcessorFactory, tournamentProcessorFactory, sessionRepository, configurationRepository, notificationService);
    }

    @Bean
    public ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> processorFactory() {
        return new ServerGameAspectFactory<>(MatchGameAspectFactory.class);
    }

    @Bean
    public GameStateFactory fakeStateFactory() {
        return new GameStateFactory() {

            @Override
            public Game getGame() {
                return Game.num;
            }

            @Override
            public GameState constructState(GameInitiation initiation, MatchGameContext context) {
                // TODO Auto-generated method stub
                return null;
            }

        };
    }

}
