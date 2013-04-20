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
        // Step 1. Check vertical
        boolean completnece[] = new boolean[11];
        Arrays.fill(completnece, true);
        for (int i = 1; i < board.length; i++) {
            // Checking rows
            completnece[0] = completnece[0] && board[0][i].getOwner() == board[0][i - 1].getOwner();
            completnece[1] = completnece[1] && board[1][i].getOwner() == board[1][i - 1].getOwner();
            completnece[2] = completnece[2] && board[2][i].getOwner() == board[2][i - 1].getOwner();
            // Checking columns
            completnece[3] = completnece[3] && board[i][0].getOwner() == board[i - 1][0].getOwner();
            completnece[4] = completnece[4] && board[i][1].getOwner() == board[i - 1][1].getOwner();
            completnece[5] = completnece[5] && board[i][2].getOwner() == board[i - 1][2].getOwner();
            // Checking diagonals
            completnece[6] = completnece[6] && board[i - 1][i - 1].getOwner() == board[i][i].getOwner();
            completnece[7] = completnece[7] && board[3 - i][3 - i].getOwner() == board[2 - i][2 - i].getOwner();
        }
        // Step 2. If at least one complete game is complete
        for (boolean complete : completnece)
            if (complete)
                return true;
        return false;
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
