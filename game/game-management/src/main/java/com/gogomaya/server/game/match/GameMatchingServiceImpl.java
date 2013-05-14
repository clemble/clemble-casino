package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameNotificationService;
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
        GameTable<State> gameTable = tableManager.reserve(specification);
        gameTable.addPlayer(playerId);

        PlayerNumberRule numberRule = specification.getNumberRule();
        if (gameTable.getPlayers().size() >= numberRule.getMinPlayers()) {
            State gameState = stateFactory.create(specification, gameTable.getPlayers());
            gameTable.getCurrentSession().setState(gameState);
            // Step 3. Initializing start of the game session
            GameSession<State> gameSession = new GameSession<State>();
            gameSession.addPlayers(gameTable.getPlayers());
            gameSession.setSessionState(GameSessionState.active);
            gameSession.setSpecification(specification);
            gameSession = sessionRepository.save(gameSession);

            gameTable.setCurrentSession(gameSession);
            gameTable = tableRepository.save(gameTable);
        } else {
            gameTable.setCurrentSession(new GameSession<State>());
            gameTable = tableRepository.save(gameTable);
            tableManager.addReservable(gameTable);
        }

        notificationManager.notify(gameTable);

        return gameTable;
    }

}
