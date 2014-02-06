package com.clemble.casino.server.spring.game;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.game.action.MatchGameProcessorFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

@Ignore
@Configuration
@Import(GameManagementSpringConfiguration.class)
@SuppressWarnings({ "rawtypes" })
public class SimpleGameSpringConfiguration {

    @Bean
    public GameManagerFactory gameProcessor(PotGameRecordRepository potRepository,
            GameStateFactory<GameState> stateFactory,
            MatchGameProcessorFactory<GameState> processorFactory,
            MatchGameRecordRepository sessionRepository,
            ServerGameConfigurationRepository configurationRepository,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GameManagerFactory(potRepository, stateFactory, processorFactory, sessionRepository, configurationRepository, notificationService);
    }

    @Bean
    public MatchGameProcessorFactory<?> processorFactory() {
        return new MatchGameProcessorFactory<>();
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
