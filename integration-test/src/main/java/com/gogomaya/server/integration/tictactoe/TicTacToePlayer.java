package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;

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

    public TicTacToePlayer(final Player player, final GameTable<TicTacToeState> table, final TicTacToeOperations operations) {
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
                    || (this.table.get().getCurrentSession().getState() != null ? TicTacToePlayerUtils.getVersion(this.table.get().getCurrentSession().getState()) : -1) < (newTable.getCurrentSession().getState() != null ? TicTacToePlayerUtils
                            .getVersion(newTable.getCurrentSession().getState()) : -1)) {
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
        if (this.getTable().getCurrentSession().getState() != null && TicTacToePlayerUtils.getVersion(this.getTable().getCurrentSession().getState()) >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.getTable().getCurrentSession().getState() != null && TicTacToePlayerUtils.getVersion(this.getTable().getCurrentSession().getState()) < expectedVersion) {
                try {
                    versionLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void select(int row, int column) {
        int beforeSelecting = this.getTable().getCurrentSession().getState().getVersion();
        ticTacToeOperations.select(this, row, column);
        Assert.assertNotSame(beforeSelecting, this.getTable().getCurrentSession().getState().getVersion());
    }

    public void bet(int ammount) {
        int beforeBetting = this.getTable().getCurrentSession().getState().getVersion();
        ticTacToeOperations.bet(this, ammount);
        Assert.assertNotSame(beforeBetting + " remained " + this.getTable().getCurrentSession().getState().getVersion(), beforeBetting, this.getTable().getCurrentSession().getState().getVersion());
    }
}
