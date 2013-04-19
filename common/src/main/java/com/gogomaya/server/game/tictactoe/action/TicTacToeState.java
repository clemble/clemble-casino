package com.gogomaya.server.game.tictactoe.action;

import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonCreator;

import com.gogomaya.server.game.action.impl.AbstractGameState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

public class TicTacToeState extends AbstractGameState<TicTacToeMove, TicTacToePlayerState> {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    private TicTacToeCellState[][] board = new TicTacToeCellState[3][3];

    private TicTacToeCell activeCell;

    @JsonCreator()
    public TicTacToeState() {
        for (TicTacToeCellState[] row : board) {
            Arrays.fill(row, TicTacToeCellState.DEFAULT_CELL_STATE);
        }
    }

    public TicTacToeCellState[][] getBoard() {
        return board;
    }

    public TicTacToeState setBoard(TicTacToeCellState[][] board) {
        this.board = board;
        return this;
    }

    public boolean complete() {
        long owner = board[activeCell.getRow()][activeCell.getColumn()].getOwner();
        // Step 1. Check vertical
        boolean complete = true;
        for (int i = 0; i < board.length; i++) {
            complete = complete && board[activeCell.getRow()][i].getOwner() == owner;
        }
        if (complete)
            return complete;
        // Step 2. Check horizontal
        complete = true;
        for (int i = 0; i < board.length; i++) {
            complete = complete && board[i][activeCell.getColumn()].getOwner() == owner;
        }
        if (complete)
            return complete;
        // Step 3. Check diagonal
        for (int i = 0, j = 0; i < board.length; i++, j++)
            complete = complete && board[i][j].getOwner() == owner;
        if (complete)
            return complete;
        // Step 4. Check diagonal
        for (int i = 0, j = board.length - 1; i < board.length; i++, j--)
            complete = complete && board[i][j].getOwner() == owner;
        if (complete)
            return complete;

        return complete;
    }

    public boolean isOwned(TicTacToeCell activeCell) {
        return board[activeCell.getRow()][activeCell.getColumn()].owned();
    }

    public TicTacToeCell getActiveCell() {
        return activeCell;
    }

    public void setActiveCell(TicTacToeCell cell) {
        this.activeCell = cell;
    }

    public void setActiveCellState(TicTacToeCellState cellState) {
        TicTacToeCellState[][] newBoard = board.clone();
        newBoard[activeCell.getRow()][activeCell.getColumn()] = cellState;
    }

}
