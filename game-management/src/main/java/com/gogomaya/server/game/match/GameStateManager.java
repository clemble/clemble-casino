package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.session.GameSessionState;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.table.rule.PlayerNumberRule;

public class GameStateManager {

    final private GameTableManager tableManager;
    final private GameTableRepository tableRepository;
    final private GameSessionRepository sessionRepository;

    public GameStateManager(final GameTableManager tableManager, final GameTableRepository tableRepository, final GameSessionRepository sessionRepository) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    public GameTable reserve(final long playerId, final GameSpecification gameSpecification) {
        // Step 1. Pooling
        GameTable gameTable = tableManager.poll(gameSpecification);
        gameTable.addPlayer(playerId);

        PlayerNumberRule numberRule = gameSpecification.getTableSpecification().getNumberRule();
        if (gameTable.getPlayers().size() >= numberRule.getMinPlayers()) {
            // Step 3. Initializing start of the game session
            GameSession gameSession = new GameSession();
            gameSession.setPlayers(gameTable.getPlayers());
            gameSession.setSessionState(GameSessionState.Active);
            gameSession.setTable(gameTable);

            gameSession = sessionRepository.save(gameSession);
            gameTable.setCurrentSession(gameSession);

            tableRepository.save(gameTable);
        } else {
            tableRepository.save(gameTable);
            tableManager.setReservable(gameTable);
        }

        return gameTable;
    }

}
