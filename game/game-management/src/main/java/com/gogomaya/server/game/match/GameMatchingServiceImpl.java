package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;

public class GameMatchingServiceImpl<State extends GameState, Spec extends GameSpecification> implements GameMatchingService<State, Spec> {

    final private GameNotificationManager notificationManager;

    final private GameTableManager<State> tableManager;
    final private GameTableRepository<GameTable<State>, State> tableRepository;
    final private GameSessionRepository<GameSession<State>, State> sessionRepository;
    final private GameStateFactory<State> stateFactory;

    final private Class<GameSession<State>> sessionClass;

    @Inject
    public GameMatchingServiceImpl(final GameTableManager<State> tableManager,
            final GameTableRepository<GameTable<State>, State> tableRepository,
            final GameSessionRepository<GameSession<State>, State> sessionRepository,
            final GameNotificationManager notificationManager,
            final GameStateFactory<State> stateFactory,
            final Class<GameSession<State>> sessionClass) {
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.stateFactory = checkNotNull(stateFactory);
        this.sessionClass = sessionClass;
    }

    @Override
    public GameTable<State> reserve(final long playerId, final Spec gameSpecification) {
        // Step 1. Pooling
        GameTable<State> gameTable = tableManager.poll(gameSpecification);
        gameTable.addPlayer(playerId);

        PlayerNumberRule numberRule = gameSpecification.getNumberRule();
        if (gameTable.getPlayers().size() >= numberRule.getMinPlayers()) {
            State gameState = stateFactory.initialize(gameSpecification, gameTable.getPlayers());
            gameTable.setState(gameState);
            // Step 3. Initializing start of the game session
            GameSession<State> gameSession = null;
            try {
                gameSession = sessionClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            gameSession.addPlayers(gameTable.getPlayers());
            gameSession.setSessionState(GameSessionState.active);
            gameSession.setTable(gameTable);
            gameSession = sessionRepository.save(gameSession);

            gameTable.setCurrentSession(gameSession);
            gameTable = tableRepository.save(gameTable);
        } else {
            gameTable = tableRepository.save(gameTable);
            tableManager.setReservable(gameTable);
        }

        notificationManager.notify(gameTable);

        return gameTable;
    }

}
