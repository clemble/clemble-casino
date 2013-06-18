package com.gogomaya.server.game.build;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.event.server.PlayerAddedEvent;
import com.gogomaya.server.game.notification.GameNotificationService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class InstantGameConstructor<State extends GameState> implements GameConstructor<State, InstantGameRequest> {

    final private PendingSessionQueue sessionQueue;
    final private PlayerStateManager activePlayerQueue;

    final private WalletTransactionManager walletTransactionManager;
    final private TableServerRegistry tableRegistry;
    final private GameTableRepository<State> tableRepository;
    final private GameTableFactory<State> tableFactory;
    final private GameNotificationService<State> notificationManager;

    @Inject
    public InstantGameConstructor(final PendingSessionQueue tableManager, final GameTableRepository<State> tableRepository,
            final GameNotificationService<State> notificationManager, final GameTableFactory<State> tableFactory,
            final WalletTransactionManager walletTransactionManager, final PlayerStateManager activePlayerQueue, final TableServerRegistry tableServerRegistry) {
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.sessionQueue = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.tableFactory = checkNotNull(tableFactory);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.tableRegistry = tableServerRegistry;
    }

    @Override
    public GameTable<State> construct(InstantGameRequest request) {
        final long playerId = request.getPlayerId();
        final GameSpecification specification = request.getSpecification();
        Long currentSession = activePlayerQueue.isActive(playerId);
        if (currentSession != null && currentSession != SessionAware.DEFAULT_SESSION) {
            GameTable<State> table = tableRepository.findBySessionId(currentSession);
            if (table == null) {
                activePlayerQueue.markAvailable(playerId);
            } else {
                return tableRegistry.specifyServer(table);
            }
        }
        // Step 0. Checking player can afford to play this game
        if (!walletTransactionManager.canAfford(playerId, specification.extractMoneyNeeded()))
            throw GogomayaException.fromError(GogomayaError.GameConstructionInsufficientMoney);
        // Step 1. Pooling
        Long session = sessionQueue.poll(specification);
        GameTable<State> table = tableFactory.findTable(session, specification);

        if (!activePlayerQueue.markBusy(playerId, table.getCurrentSession().getSession()))
            throw GogomayaException.fromError(GogomayaError.GameMatchPlayerHasPendingSessions);
        table.addPlayer(playerId);
        table = tableRepository.save(table);

        notificationManager.notify(table.getPlayers(), new PlayerAddedEvent().setSession(table.getTableId()).setPlayerId(playerId));

        PlayerNumberRule numberRule = specification.getNumberRule();
        if (table.getPlayers().size() >= numberRule.getMinPlayers()) {
            tableFactory.startGame(table);
            tableRepository.saveAndFlush(table);
            // Step 3. Initializing start of the game session
            State state = table.getCurrentSession().getState();

            notificationManager.notify(table.getPlayers(),
                    new GameStartedEvent<State>().setNextMoves(state.getNextMoves()).setState(state).setSession(table.getTableId()));
        } else {
            sessionQueue.add(table.getTableId(), table.getSpecification());
        }

        return tableRegistry.specifyServer(table);

    }

}
