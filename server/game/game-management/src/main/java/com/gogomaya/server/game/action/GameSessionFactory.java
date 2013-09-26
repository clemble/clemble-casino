package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionState;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.repository.game.GameSessionRepository;

public class GameSessionFactory<State extends GameState> {

    final private GameStateFactory<State> stateFactory;
    final private GameSessionRepository<State> sessionRepository;

    public GameSessionFactory(final GameStateFactory<State> stateFactory,
            final GameSessionRepository<State> sessionRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    public GameSession<State> construct(GameInitiation initiation) {
        GameSpecification specification = initiation.getSpecification();

        GameSession<State> session = new GameSession<State>();
        session.setSession(initiation.getSession());
        session.setSpecification(specification);
        session.setSessionState(GameSessionState.active);
        session.setPlayers(initiation.getParticipants());
        session.setState(stateFactory.constructState(initiation));

        return sessionRepository.saveAndFlush(session);
    }

}
