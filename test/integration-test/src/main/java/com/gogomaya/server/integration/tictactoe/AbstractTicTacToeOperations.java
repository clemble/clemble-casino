package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;

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
        TicTacToeSelectCellMove move = new TicTacToeSelectCellMove(player.getPlayer().getPlayerId(), TicTacToeCell.create(row, column));
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }

    final public TicTacToeState bet(TicTacToePlayer player, int ammount) {
        // Step 1. Generating bet move
        TicTacToeMove move = new TicTacToeBetOnCellMove(player.getPlayer().getPlayerId(), ammount);
        // Step 2. Performing actual TicTacToeMove
        return perform(player, move);
    }

}
