package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameConnection;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.event.PlayerAddedEvent;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;

public class GameMatchingServiceImpl<State extends GameState> implements GameMatchingService<State> {

    final private GameNotificationService<State> notificationManager;
    final private GameTableManager<State> tableManager;
    final private GameTableRepository<State> tableRepository;
    final private GameStateFactory<State> stateFactory;
    final private GameSessionRepository sessionRepository;

    @Inject
    public GameMatchingServiceImpl(final GameTableManager<State> tableManager,
            final GameTableRepository<State> tableRepository,
            final GameSessionRepository sessionRepository,
            final GameNotificationService<State> notificationManager,
            final GameStateFactory<State> stateFactory) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.stateFactory = checkNotNull(stateFactory);
    }

    @Override
    public GameTable<State> reserve(final long playerId, final GameSpecification specification) {
        // Step 1. Pooling
        GameTable<State> table = tableManager.reserve(specification);
        table.addPlayer(playerId);

        PlayerNumberRule numberRule = specification.getNumberRule();
        if (table.getPlayers().size() >= numberRule.getMinPlayers()) {
            State gameState = stateFactory.create(specification, table.getPlayers());
            table.getCurrentSession().setState(gameState);
            // Step 3. Initializing start of the game session
            GameSession<State> session = new GameSession<State>();
            session.addPlayers(table.getPlayers());
            session.setSessionState(GameSessionState.active);
            session.setSpecification(specification);
            session = sessionRepository.save(session);

            table.setCurrentSession(session);
            table = tableRepository.save(table);
        } else {
            table.setCurrentSession(new GameSession<State>());
            table = tableRepository.save(table);
            tableManager.addReservable(table);
        }

        notificationManager.notify(
            new GameConnection()
                .setRoutingKey(table.getTableId())
                .setServerConnection(table.getServerResource()),
            new PlayerAddedEvent()
                .setSession(table.getTableId())
                .setPlayerId(playerId));

        return table;
    }

}
