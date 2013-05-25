package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import org.junit.Assert;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer extends GamePlayer<TicTacToeState> {

    final private TicTacToeOperations ticTacToeOperations;

    public TicTacToePlayer(final Player player, final GameTable<TicTacToeState> table, final TicTacToeOperations operations, final GameListenerOperations<TicTacToeState> listenerOperations) {
        super(player, table, listenerOperations);
        checkNotNull(table);
        this.ticTacToeOperations = checkNotNull(operations);
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
    }

}
