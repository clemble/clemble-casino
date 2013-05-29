package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableRepository;

public class GameTableFactory<State extends GameState> {

    final private GameStateFactory<State> stateFactory;

    final private GameServerConnectionManager connectionManager;

    final private GameTableRepository<State> tableRepository;

    final private GameSessionRepository<State> sessionRepository;

    public GameTableFactory(final GameStateFactory<State> stateFactory,
            final GameServerConnectionManager connectionManager,
            final GameTableRepository<State> tableRepository,
            final GameSessionRepository<State> sessionRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.connectionManager = checkNotNull(connectionManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    public GameTable<State> findTable(Long tableId, GameSpecification specification) {
        if (tableId == null)
            return create(specification);

        GameTable<State> table = tableRepository.findOne(tableId);
        if (table == null)
            return create(specification);

        return table;
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
        session.setSessionState(GameSessionState.inactive);
        session.setState(stateFactory.create());
        session = sessionRepository.saveAndFlush(session);

        table.setSpecification(specification);
        table.setCurrentSession(session);

        table = tableRepository.save(table);

        GameServerConnection serverConnection = connectionManager.reserve(table);
        table.setServerResource(serverConnection);

        table = tableRepository.save(table);
        return table;
    }
}
