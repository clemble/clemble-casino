package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;

import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer extends GamePlayer<TicTacToeState> {

    final private TicTacToeOperations ticTacToeOperations;

    final private AtomicInteger moneyLeft = new AtomicInteger();

    public TicTacToePlayer(final Player player,
            final GameTable<TicTacToeState> table,
            final TicTacToeOperations operations,
            final GameListenerOperations<TicTacToeState> listenerOperations) {
        super(player, table, listenerOperations);
        this.ticTacToeOperations = checkNotNull(operations);
        this.moneyLeft.set(table.getSpecification().getBetRule().getPrice());
    }

    public void select(int row, int column) {
        int beforeSelecting = this.getState().getVersion();
        ticTacToeOperations.select(this, row, column);
        Assert.assertNotSame(beforeSelecting + " remained " + this.getState().getVersion(), this.getState().getVersion());
    }

    public void bet(int ammount) {
        int beforeBetting = this.getState().getVersion();
        ticTacToeOperations.bet(this, ammount);
        Assert.assertNotSame(beforeBetting + " remained " + this.getState().getVersion(), beforeBetting, this.getState().getVersion());
        moneyLeft.getAndAdd(-ammount);
    }

    @Override
    public void giveUp() {
        // int beforeBetting = this.getState().getVersion();
        ticTacToeOperations.giveUp(this);
        // Assert.assertNotSame(beforeBetting + " remained " + this.getState().getVersion(), beforeBetting, this.getState().getVersion());
    }

    public int getMoneyLeft() {
        return moneyLeft.get();
    }

}
