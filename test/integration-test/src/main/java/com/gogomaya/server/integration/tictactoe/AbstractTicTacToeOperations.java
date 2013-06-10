package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.tictactoe.TicTacToeCell;
import com.gogomaya.server.tictactoe.TicTacToeState;
import com.gogomaya.server.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.tictactoe.event.client.TicTacToeEvent;
import com.gogomaya.server.tictactoe.event.client.TicTacToeSelectCellEvent;

abstract public class AbstractTicTacToeOperations implements TicTacToeOperations {

    final private GameListenerOperations<TicTacToeState> listenerOperations;

    public AbstractTicTacToeOperations(final GameListenerOperations<TicTacToeState> tableListenerOperations) {
        this.listenerOperations = checkNotNull(tableListenerOperations);
    }

    @Override
    final public GamePlayer<TicTacToeState> construct(Player player, GameTable<TicTacToeState> table) {
        return new TicTacToePlayer(player, table, this, listenerOperations);
    }

    final public TicTacToeState select(TicTacToePlayer player, int row, int column) {
        // Step 1. Generating bet move
        TicTacToeSelectCellEvent move = new TicTacToeSelectCellEvent(player.getPlayer().getPlayerId(), TicTacToeCell.create(row, column));
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }
    
    @Override
    final public TicTacToeState giveUp(TicTacToePlayer player) {
        return perform(player, new GiveUpEvent(player.getPlayer().getPlayerId()));
    }

    final public TicTacToeState bet(TicTacToePlayer player, int ammount) {
        // Step 1. Generating bet move
        TicTacToeEvent move = new TicTacToeBetOnCellEvent(player.getPlayer().getPlayerId(), ammount);
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }

}
