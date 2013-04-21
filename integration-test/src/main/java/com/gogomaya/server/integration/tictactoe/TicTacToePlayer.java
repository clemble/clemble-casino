package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.integration.game.listener.GameListenerControl;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer {

    final private TicTacToeOperations ticTacToeOperations;

    final private Object versionLock = new Object();

    final private AtomicReference<TicTacToeTable> table = new AtomicReference<TicTacToeTable>();
    private Player player;
    private GameListenerControl listenerControl;

    public TicTacToePlayer(TicTacToeOperations operations) {
        this.ticTacToeOperations = operations;
    }

    public Player getPlayer() {
        return player;
    }

    public TicTacToePlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public TicTacToeTable getTable() {
        return checkNotNull(table.get());
    }

    public TicTacToePlayer setTable(TicTacToeTable newTable) {
        synchronized (versionLock) {
            if (this.table.get() == null
                    || (this.table.get().getState() != null ? this.table.get().getState().getVersion() : -1) < (newTable.getState() != null ? newTable
                            .getState().getVersion() : -1)) {
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
        if (this.getTable().getState() != null && this.getTable().getState().getVersion() >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.getTable().getState() != null && this.getTable().getState().getVersion() < expectedVersion) {
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
