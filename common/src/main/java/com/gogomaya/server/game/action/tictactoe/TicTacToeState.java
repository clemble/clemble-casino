package com.gogomaya.server.game.action.tictactoe;

import java.util.Collection;

import com.gogomaya.server.game.action.AbstractGameState;
import com.gogomaya.server.game.action.tictactoe.move.TicTacToeMove;

public class TicTacToeState extends AbstractGameState<TicTacToeMove, TicTacToePlayerState> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    final private TicTacToeCellState[][] board;

    public TicTacToeState(final Collection<TicTacToePlayerState> playerState,
            final Collection<TicTacToeMove> gameMoves,
            final Collection<TicTacToeMove> nextMoves,
            final TicTacToeCellState[][] board) {
        super(playerState, gameMoves, nextMoves);

        this.board = board.clone();
    }

    public TicTacToeCellState[][] getBoard() {
        return board;
    }

}
