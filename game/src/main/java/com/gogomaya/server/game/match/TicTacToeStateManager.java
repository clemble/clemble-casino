package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.session.TicTacToeSessionRepository;
import com.gogomaya.server.game.table.TicTacToeTableManager;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;

public class TicTacToeStateManager {

    final private GameNotificationManager notificationManager;

    final private TicTacToeTableManager tableManager;
    final private TicTacToeTableRepository tableRepository;
    final private TicTacToeSessionRepository sessionRepository;
    final private TicTacToeStateFactory stateFactory;

    @Inject
    public TicTacToeStateManager(final TicTacToeTableManager tableManager,
            final TicTacToeTableRepository tableRepository,
            final TicTacToeSessionRepository sessionRepository,
            final GameNotificationManager notificationManager,
            final TicTacToeStateFactory stateFactory) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.stateFactory = checkNotNull(stateFactory);
    }

    public TicTacToeTable reserve(final long playerId, final TicTacToeSpecification gameSpecification) {
        // Step 1. Pooling
        TicTacToeTable gameTable = tableManager.poll(gameSpecification);
        gameTable.addPlayer(playerId);

        PlayerNumberRule numberRule = gameSpecification.getNumberRule();
        if (gameTable.getPlayers().size() >= numberRule.getMinPlayers()) {
            TicTacToeState gameState = stateFactory.initialize(gameSpecification, gameTable.getPlayers());
            // Step 3. Initializing start of the game session
            TicTacToeSession gameSession = new TicTacToeSession();
            gameSession.addPlayers(gameTable.getPlayers());
            gameSession.setSessionState(GameSessionState.ACTIVE);
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
