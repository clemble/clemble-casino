package com.gogomaya.server.game.tictactoe.action;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.SequentialPlayerIterator;
import com.gogomaya.server.game.action.impl.AbstractGameState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;

@JsonIgnoreProperties({ "winner", "activeUsers" })
public class TicTacToeState extends AbstractGameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -3282042914639667829L;

    private TicTacToeCellState[][] board = new TicTacToeCellState[3][3];

    private TicTacToeCell activeCell;

    public TicTacToeState() {
        // Step 0. Filling the board with empty cell value
        for (TicTacToeCellState[] row : board) {
            Arrays.fill(row, TicTacToeCellState.DEFAULT_CELL_STATE);
        }
    }

    public TicTacToeState(final Collection<GamePlayerState> playersStates) {
        this();

        setPlayerStates(playersStates);
        setPlayerIterator(new SequentialPlayerIterator(playersStates));
        setNextMove(new TicTacToeSelectCellMove(getPlayerIterator().current()));
    }

    public TicTacToeCell getActiveCell() {
        return activeCell;
    }

    public TicTacToeState setActiveCell(TicTacToeCell cell) {
        this.activeCell = cell;
        return this;
    }

    public TicTacToeCellState[][] getBoard() {
        return board;
    }

    public TicTacToeCellState getCellState(int row, int column) {
        return row >= 0 && row < board.length && column >= 0 && column < board.length ? board[row][column] : null;
    }

    public TicTacToeCellState getCellState(TicTacToeCell cell) {
        return getCellState(cell.getRow(), cell.getColumn());
    }

    public TicTacToeState setBoard(TicTacToeCellState[][] board) {
        this.board = board;
        return this;
    }

    @Override
    public boolean complete() {
        // Step 1. Check vertical
        return getWinner() != -1L;
    }

    @Override
    public long getWinner() {
        long completnece[] = new long[8];
        Arrays.fill(completnece, -1L);
        // Checking rows
        completnece[0] = owner(board[0][0], board[0][1], board[0][2]);
        completnece[1] = owner(board[1][0], board[1][1], board[1][2]);
        completnece[2] = owner(board[2][0], board[2][1], board[2][2]);
        // Checking columns
        completnece[3] = owner(board[0][0], board[1][0], board[2][0]);
        completnece[4] = owner(board[0][1], board[1][1], board[2][1]);
        completnece[5] = owner(board[0][2], board[1][2], board[2][2]);
        // Checking diagonals
        completnece[6] = owner(board[0][0], board[1][1], board[2][2]);
        completnece[7] = owner(board[0][2], board[1][1], board[2][0]);
        // Step 2. If at least one complete game is complete
        for (long complete : completnece)
            if (complete != -1L)
                return complete;
        return -1L;
    }

    private long owner(TicTacToeCellState firstCell, TicTacToeCellState secondCell, TicTacToeCellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner()) ? firstCell.getOwner()
                : TicTacToeCellState.DEFAULT_OWNER;
    }

}
