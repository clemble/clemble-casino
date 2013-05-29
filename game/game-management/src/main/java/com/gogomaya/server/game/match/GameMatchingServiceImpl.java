package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.connection.GameConnection;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.event.GameStartedEvent;
import com.gogomaya.server.game.event.PlayerAddedEvent;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.GameTableRepository;

public class GameMatchingServiceImpl<State extends GameState> implements GameMatchingService<State> {

    final private GameTableQueue tableManager;
    final private GameTableRepository<State> tableRepository;
    final private GameTableFactory<State> tableFactory;
    final private GameNotificationService<State> notificationManager;

    @Inject
    public GameMatchingServiceImpl(final GameTableQueue tableManager,
            final GameTableRepository<State> tableRepository,
            final GameNotificationService<State> notificationManager,
            final GameTableFactory<State> tableFactory) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.tableFactory = checkNotNull(tableFactory);
    }

    @Override
    public GameTable<State> reserve(final long playerId, final GameSpecification specification) {
        // Step 1. Pooling
        Long tableId = tableManager.poll(specification);
        GameTable<State> table = tableFactory.findTable(tableId, specification);

        table.addPlayer(playerId);
        table = tableRepository.save(table);

        notificationManager.notify(new GameConnection().setRoutingKey(table.getTableId()).setServerConnection(table.getServerResource()),
                new PlayerAddedEvent().setSession(table.getTableId()).setPlayerId(playerId));

        PlayerNumberRule numberRule = specification.getNumberRule();
        if (table.getPlayers().size() >= numberRule.getMinPlayers()) {
            tableFactory.startGame(table);
            tableRepository.saveAndFlush(table);
            // Step 3. Initializing start of the game session
            State state = table.getCurrentSession().getState();

            notificationManager.notify(new GameConnection().setRoutingKey(table.getTableId()).setServerConnection(table.getServerResource()),
                    new GameStartedEvent<State>().setNextMoves(state.getNextMoves()).setState(state).setSession(table.getTableId()));
        } else {
            tableManager.add(table.getTableId(), table.getSpecification());
        }

        return table;
    }

}
