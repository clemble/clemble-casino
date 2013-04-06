package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.session.GameSessionState;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;

public class GameStateManager {

    final private GameTableManager tableManager;
    final private GameTableRepository tableRepository;
    final private GameSessionRepository sessionRepository;
    final private GameNotificationManager notificationManager;
    final private GameStateFactory stateFactory;

    @Inject
    public GameStateManager(final GameTableManager tableManager,
            final GameTableRepository tableRepository,
            final GameSessionRepository sessionRepository,
            final GameNotificationManager notificationManager,
            final GameStateFactory stateFactory) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.stateFactory = checkNotNull(stateFactory);
    }

    public GameTable reserve(final long playerId, final GameSpecification gameSpecification) {
        // Step 1. Pooling
        GameTable gameTable = tableManager.poll(gameSpecification);
        gameTable.addPlayer(playerId);

        GameTablePlayerNumberRule numberRule = gameSpecification.getTableSpecification().getNumberRule();
        if (gameTable.getPlayers().size() >= numberRule.getMinPlayers()) {
            GameState<?, ?> gameState = stateFactory.initialize(gameSpecification, gameTable.getPlayers());
            // Step 3. Initializing start of the game session
            GameSession gameSession = new GameSession();
            gameSession.setPlayers(gameTable.getPlayers());
            gameSession.setSessionState(GameSessionState.Active);
            gameSession.setTable(gameTable);

            gameSession.setGameState(gameState);

            gameSession = sessionRepository.save(gameSession);
            gameTable.setCurrentSession(gameSession);

            tableRepository.save(gameTable);
        } else {
            gameTable = tableRepository.save(gameTable);
            tableManager.setReservable(gameTable);
        }

        notificationManager.notify(gameTable);

        return gameTable;
    }

}
