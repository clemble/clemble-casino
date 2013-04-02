package com.gogomaya.server.game.action.tictactoe;

import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.tictactoe.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.action.tictactoe.move.TicTacToeMove;
import com.gogomaya.server.game.action.tictactoe.move.TicTacToeSelectCellMove;

public class TicTacToeProcessor implements GameProcessor<TicTacToeState, TicTacToeMove> {

    @Override
    public TicTacToeState process(final TicTacToeState oldState, final TicTacToeMove gameMove) {
        // Step 0. Sanity check
        if (oldState == null)
            throw new IllegalArgumentException("old state can't be null");
        if (gameMove == null)
            return oldState;
        // Step 1. Processing Select cell move
        if (gameMove instanceof TicTacToeSelectCellMove) {
            return process(oldState, (TicTacToeSelectCellMove) gameMove);
        } else if (gameMove instanceof TicTacToeBetOnCellMove) {
            return process(oldState, (TicTacToeBetOnCellMove) gameMove);
        }

        return oldState;
    }

    private TicTacToeState process(final TicTacToeState oldState, final TicTacToeBetOnCellMove betMove) {
        return oldState;
    }

    private TicTacToeState process(final TicTacToeState oldState, final TicTacToeSelectCellMove selectCellMove) {
        return oldState;
    }

}
