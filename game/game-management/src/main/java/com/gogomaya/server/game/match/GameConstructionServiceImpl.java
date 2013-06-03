package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.active.ActivePlayerQueue;
import com.gogomaya.server.game.connection.GameConnection;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.event.GameStartedEvent;
import com.gogomaya.server.game.event.PlayerAddedEvent;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class GameConstructionServiceImpl<State extends GameState> implements GameConstructionService<State> {

    final private GameTableQueue tableManager;
    final private GameTableRepository<State> tableRepository;
    final private GameTableFactory<State> tableFactory;
    final private GameNotificationService<State> notificationManager;

    final private ActivePlayerQueue activePlayerQueue;

    final private WalletTransactionManager walletTransactionManager;

    @Inject
    public GameConstructionServiceImpl(final GameTableQueue tableManager,
            final GameTableRepository<State> tableRepository,
            final GameNotificationService<State> notificationManager,
            final GameTableFactory<State> tableFactory,
            final WalletTransactionManager walletTransactionManager,
            final ActivePlayerQueue activePlayerQueue) {
        this.activePlayerQueue = checkNotNull(activePlayerQueue);
        this.tableManager = checkNotNull(tableManager);
        this.tableRepository = checkNotNull(tableRepository);
        this.notificationManager = checkNotNull(notificationManager);
        this.tableFactory = checkNotNull(tableFactory);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
    }

    @Override
    public GameTable<State> findOpponent(final long playerId, final GameSpecification specification) {
        Long currentSession = activePlayerQueue.isActive(playerId);
        if (currentSession != null) {
            GameTable<State> table = tableRepository.findBySessionId(currentSession);
            if (table == null) {
                activePlayerQueue.markInActive(playerId);
            } else {
                return table;
            }
        }
        // Step 0. Checking player can afford to play this game
        WalletOperation operation = new WalletOperation().setPlayerId(playerId)
                .setAmmount(new Money(specification.getCurrency(), specification.getBetRule().getPrice())).setOperation(Operation.Credit);
        if (!walletTransactionManager.canAfford(operation))
            throw GogomayaException.create(GogomayaError.GameSpecificationInsufficientMoney);
        // Step 1. Pooling
        Long tableId = tableManager.poll(specification);
        GameTable<State> table = tableFactory.findTable(tableId, specification);

        if (!activePlayerQueue.markActive(playerId, table.getCurrentSession().getSessionId()))
            throw GogomayaException.create(GogomayaError.GameMatchPlayerHasPendingSessions);
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

    @Override
    public ScheduledGame schedule(long initiator, Collection<Long> playerIds, GameSpecification specification) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduledGame cancel(long initiator, long scheduledGameId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduledGame accept(long playerId, long scheduledGameId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduledGame decline(long playerId, long scheduledGameId) {
        // TODO Auto-generated method stub
        return null;
    }

}
