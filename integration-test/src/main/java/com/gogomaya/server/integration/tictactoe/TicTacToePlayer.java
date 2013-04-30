package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.listener.GameListenerControl;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer {

    final private TicTacToeOperations ticTacToeOperations;

    final private Object versionLock = new Object();

    final private AtomicReference<GameTable<TicTacToeState>> table = new AtomicReference<GameTable<TicTacToeState>>();

    final private Player player;

    private GameListenerControl listenerControl;

    public TicTacToePlayer(final Player player,
            final GameTable<TicTacToeState> table,
            final TicTacToeOperations operations) {
        this.player = checkNotNull(player);
        this.table.set(checkNotNull(table));
        this.ticTacToeOperations = checkNotNull(operations);
    }

    public Player getPlayer() {
        return player;
    }

    public GameTable<TicTacToeState> getTable() {
        return checkNotNull(table.get());
    }

    public TicTacToePlayer setTable(GameTable<TicTacToeState> newTable) {
        synchronized (versionLock) {
            if (this.table.get() == null
                    || (this.table.get().getState() != null ? TicTacToePlayerUtils.getVersion(this.table.get().getState()) : -1) < (newTable.getState() != null ? TicTacToePlayerUtils
                            .getVersion(newTable.getState()) : -1)) {
                this.table.set(newTable);
                versionLock.notifyAll();
            }
        }
        return this;
    }

    public GameListenerControl getListenerControl() {
        return listenerControl;
    }

    public TicTacToePlayer setListenerControl(GameListenerControl listenerControl) {
        this.listenerControl = listenerControl;
        return this;
    }

    public void waitVersion(int expectedVersion) {
        if (this.getTable().getState() != null && TicTacToePlayerUtils.getVersion(this.getTable().getState()) >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.getTable().getState() != null && TicTacToePlayerUtils.getVersion(this.getTable().getState()) < expectedVersion) {
                try {
                    versionLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void select(int row, int column) {
        ticTacToeOperations.select(this, row, column);
    }

    public void bet(int ammount) {
        ticTacToeOperations.bet(this, ammount);
    }
}
