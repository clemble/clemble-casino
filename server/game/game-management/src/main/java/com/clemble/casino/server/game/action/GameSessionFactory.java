package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.repository.game.GameSessionRepository;

public class GameSessionFactory<State extends GameState> {

    final private GameStateFactory<State> stateFactory;
    final private GameSessionRepository<State> sessionRepository;

    public GameSessionFactory(final GameStateFactory<State> stateFactory,
            final GameSessionRepository<State> sessionRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    public GameSession<State> construct(GameInitiation initiation, MatchGameConfiguration configuration) {

        State state = stateFactory.constructState(initiation, new GameContext(initiation, configuration));

        GameSession<State> session = new GameSession<State>()
            .setSession(initiation.getSession())
            .setConfiguration(configuration.getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants())
            .setState(state);

        return sessionRepository.saveAndFlush(session);
    }

}
