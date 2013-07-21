package com.gogomaya.server.integration.game.tictactoe;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;

import com.gogomaya.server.game.tictactoe.Cell;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.game.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.game.tictactoe.event.client.TicTacToeSelectCellEvent;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.GenericGameSessionPlayer;

public class TicTacToeSessionPlayer extends GenericGameSessionPlayer<TicTacToeState> {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -2100664121282462477L;

    final private AtomicInteger moneySpent = new AtomicInteger();

    public TicTacToeSessionPlayer(final GameSessionPlayer<TicTacToeState> delegate) {
        super(delegate);
    }

    public void select(int row, int column) {
        int beforeSelecting = this.getState() != null ? this.getState().getVersion() : -1;
        // Step 1. Generating bet move
        perform(new TicTacToeSelectCellEvent(actualPlayer.getPlayerId(), Cell.create(row, column)));
        Assert.assertNotSame(beforeSelecting + " remained " + this.getState().getVersion(), this.getState().getVersion());
    }

    public void bet(int ammount) {
        int beforeBetting = this.getState().getVersion();
        perform(new TicTacToeBetOnCellEvent(actualPlayer.getPlayerId(), ammount));
        Assert.assertNotSame(beforeBetting + " remained " + this.getState().getVersion(), beforeBetting, this.getState().getVersion());
        moneySpent.getAndAdd(-ammount);
    }
    
    public long getMoneyLeft(){
        return getState().getPlayerState(getPlayerId()).getMoneyLeft();
    }

    public int getMoneySpent() {
        return moneySpent.get();
    }

}
