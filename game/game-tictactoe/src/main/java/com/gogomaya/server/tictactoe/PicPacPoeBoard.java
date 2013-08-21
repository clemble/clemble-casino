package com.gogomaya.server.tictactoe;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.cell.CellState;
import com.gogomaya.server.game.outcome.DrawOutcome;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.player.PlayerAware;

public class PicPacPoeBoard implements Serializable {

    private static final long serialVersionUID = -3282042914639667829L;

    private CellState[][] board;

    private Cell selected;

    public PicPacPoeBoard() {
        board = new CellState[3][3];
        for (CellState[] row : board) {
            Arrays.fill(row, CellState.DEFAULT);
        }
        selected = null;
    }

    @JsonCreator
    public PicPacPoeBoard(@JsonProperty("board") CellState[][] board, @JsonProperty("selected") Cell selected) {
        this.selected = selected;
        this.board = board;
    }

    public CellState[][] getBoard() {
        return board;
    }

    public Cell getSelected() {
        return selected;
    }

    public void setSelected(Cell cell) {
        selected = cell;
    }

    public void setSelectedState(CellState cellState) {
        if (selected != null)
            board[selected.getRow()][selected.getColumn()] = cellState;
    }
    
    public boolean owned(int row, int column) {
        return board[row][column].owned();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(board);
        result = prime * result + ((selected == null) ? 0 : selected.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PicPacPoeBoard other = (PicPacPoeBoard) obj;
        if (!Arrays.deepEquals(board, other.board))
            return false;
        if (selected == null) {
            if (other.selected != null)
                return false;
        } else if (!selected.equals(other.selected))
            return false;
        return true;
    }

    public static PicPacPoeBoard create() {
        // Step 1. Creating default board
        CellState[][] board = new CellState[3][3];
        for (CellState[] row : board)
            Arrays.fill(row, CellState.DEFAULT);
        // Step 2. Returning new state
        return new PicPacPoeBoard(board, Cell.DEFAULT);
    }

    @JsonCreator
    public static PicPacPoeBoard create(@JsonProperty("board") CellState[][] board, @JsonProperty("selected") Cell selected) {
        return new PicPacPoeBoard(board, selected);
    }

    public static GameOutcome fetchOutcome(PicPacPoeBoard state) {
        // Step 1. Checking if there is a single winner
        long winner = fetchWinner(state);
        if (winner != PlayerAware.DEFAULT_PLAYER) {
            return new PlayerWonOutcome(winner);
        }
        // Step 2. Checking if game ended in draw
        if (!canHaveWinner(state)) {
            return new DrawOutcome();
        }
        return null;
    }

    public static long fetchWinner(PicPacPoeBoard state) {
        CellState[][] board = state.board;
        return fetcSingleOwner(board[0][0], board[0][1], board[0][2]) // Checking rows
                + fetcSingleOwner(board[1][0], board[1][1], board[1][2])
                + fetcSingleOwner(board[2][0], board[2][1], board[2][2])
                + fetcSingleOwner(board[0][0], board[1][0], board[2][0]) // Checking columns
                + fetcSingleOwner(board[0][1], board[1][1], board[2][1]) + fetcSingleOwner(board[0][2], board[1][2], board[2][2]) // Checking diagonals
                + fetcSingleOwner(board[0][0], board[1][1], board[2][2]) + fetcSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private static long fetcSingleOwner(CellState firstCell, CellState secondCell, CellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner()) ? firstCell.getOwner()
                : PlayerAware.DEFAULT_PLAYER;
    }

    public static boolean canHaveWinner(PicPacPoeBoard state) {
        CellState[][] board = state.board;
        return canHaveSingleOwner(board[0][0], board[0][1], board[0][2]) || canHaveSingleOwner(board[1][0], board[1][1], board[1][2])
                || canHaveSingleOwner(board[2][0], board[2][1], board[2][2])
                // Checking columns
                || canHaveSingleOwner(board[0][0], board[1][0], board[2][0]) || canHaveSingleOwner(board[0][1], board[1][1], board[2][1])
                || canHaveSingleOwner(board[0][2], board[1][2], board[2][2])
                // Checking diagonals
                || canHaveSingleOwner(board[0][0], board[1][1], board[2][2]) || canHaveSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private static boolean canHaveSingleOwner(CellState firstCell, CellState secondCell, CellState therdCell) {
        // In arbitrary line XOX if X is free, it can be owned by O, if O is free it also can be owned by X
        return (firstCell.getOwner() == secondCell.getOwner() && (!firstCell.owned() || !therdCell.owned()))
                || (secondCell.getOwner() == therdCell.getOwner() && (!secondCell.owned() || !firstCell.owned()))
                || (therdCell.getOwner() == firstCell.getOwner() && (!therdCell.owned() || !secondCell.owned()));
    }

}
