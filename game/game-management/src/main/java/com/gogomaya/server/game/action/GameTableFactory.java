package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameSessionState;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableRepository;

public class GameTableFactory<State extends GameState> {

    final private GameStateFactory<State> stateFactory;

    final private GameTableRepository<State> tableRepository;

    final private GameSessionRepository<State> sessionRepository;

    final private TableServerRegistry tableRegistry;

    public GameTableFactory(final GameStateFactory<State> stateFactory,
            final GameTableRepository<State> tableRepository,
            final GameSessionRepository<State> sessionRepository,
            final TableServerRegistry serverRegistry) {
        this.stateFactory = checkNotNull(stateFactory);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.tableRegistry = checkNotNull(serverRegistry);
    }

    public GameTable<State> findTable(Long session, GameSpecification specification) {
        if (session == null)
            return create(specification);

        GameTable<State> table = tableRepository.findBySessionId(session);
        if (table == null)
            return create(specification);

        return tableRegistry.specifyServer(table);
    }

    public GameTable<State> startGame(GameTable<State> table) {
        State state = stateFactory.create(table.getSpecification(), table.getPlayers());

        table.getCurrentSession().setState(state);
        table.getCurrentSession().setSessionState(GameSessionState.active);

        return table;
    }

    private GameTable<State> create(GameSpecification specification) {
        GameTable<State> table = new GameTable<State>();

        GameSession<State> session = new GameSession<>();
        session.setSpecification(specification);
        session.setSessionState(GameSessionState.construction);
        session.setState(stateFactory.create());
        session = sessionRepository.saveAndFlush(session);

        table.setSpecification(specification);
        table.setCurrentSession(session);

        table = tableRepository.save(table);

        return tableRegistry.specifyServer(table);
    }
}
