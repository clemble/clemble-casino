package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionState;
import com.gogomaya.game.GameState;
import com.gogomaya.game.GameTable;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.repository.game.GameTableRepository;

public class GameTableFactory<State extends GameState> {

    final private GameStateFactory<State> stateFactory;

    final private TableServerRegistry tableRegistry;
    final private GameTableRepository<State> tableRepository;

    public GameTableFactory(final GameStateFactory<State> stateFactory,
            final GameTableRepository<State> tableRepository,
            final TableServerRegistry serverRegistry) {
        this.stateFactory = checkNotNull(stateFactory);
        this.tableRepository = checkNotNull(tableRepository);
        this.tableRegistry = checkNotNull(serverRegistry);
    }

    public GameTable<State> constructTable(GameInitiation initiation) {
        GameSpecification specification = initiation.getSpecification();

        GameSession<State> session = new GameSession<State>();
        session.setSession(initiation.getSession());
        session.setSpecification(specification);
        session.setSessionState(GameSessionState.active);
        session.setPlayers(initiation.getParticipants());
        session.setState(stateFactory.constructState(initiation));

        GameTable<State> table = poll(specification);
        table.setPlayers(initiation.getParticipants());
        table.setCurrentSession(session);

        table = tableRepository.saveAndFlush(table);
        tableRegistry.specifyServer(table);

        return table;
    }

    private GameTable<State> poll(GameSpecification specification) {
        GameTable<State> table = new GameTable<State>();

        table.setSpecification(specification);

        return table;
    }

}
