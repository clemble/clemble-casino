package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.listener.GameListenerControl;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer {

    final private TicTacToeOperations ticTacToeOperations;

    final private Object versionLock = new Object();

    final private AtomicReference<TicTacToeState> state = new AtomicReference<TicTacToeState>();

    final private Player player;

    final private long sessionId;

    final private long tableId;

    final private GameSpecification specification;

    private GameListenerControl listenerControl;

    public TicTacToePlayer(final Player player, final GameTable<TicTacToeState> table, final TicTacToeOperations operations) {
        checkNotNull(table);
        this.player = checkNotNull(player);
        this.sessionId = table.getCurrentSession().getSessionId();
        this.tableId = table.getTableId();
        this.specification = table.getSpecification();
        this.state.set(table.getCurrentSession().getState());
        this.ticTacToeOperations = checkNotNull(operations);
    }

    public Player getPlayer() {
        return player;
    }

    public TicTacToeState getState() {
        return checkNotNull(state.get());
    }

    public TicTacToePlayer setState(TicTacToeState newTable) {
        synchronized (versionLock) {
            if (this.state.get() == null || this.state.get().getVersion() < newTable.getVersion()) {
                this.state.set(newTable);
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
        if (this.state.get() != null && this.state.get().getVersion() >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.state.get() != null && this.state.get().getVersion() < expectedVersion) {
                try {
                    versionLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void select(int row, int column) {
        int beforeSelecting = this.state.get().getVersion();
        ticTacToeOperations.select(this, row, column);
        Assert.assertNotSame(beforeSelecting, this.state.get().getVersion());
    }

    public void bet(int ammount) {
        int beforeBetting = this.state.get().getVersion();
        ticTacToeOperations.bet(this, ammount);
        Assert.assertNotSame(beforeBetting + " remained " + this.state.get().getVersion(), beforeBetting, this.state.get().getVersion());
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getTableId() {
        return tableId;
    }

    public GameSpecification getSpecification() {
        return specification;
    }
}
