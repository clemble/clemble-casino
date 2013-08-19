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

public class PicPacPoeState implements Serializable {

    private static final long serialVersionUID = -3282042914639667829L;

    final private CellState[][] board;

    final private Cell selected;

    private PicPacPoeState(CellState[][] board, Cell selected) {
        this.selected = selected;
        this.board = board;
    }

    public CellState[][] getBoard() {
        return board;
    }

    public Cell getSelected() {
        return selected;
    }

    public PicPacPoeState setSelected(Cell cell) {
        return new PicPacPoeState(board, cell);
    }

    public PicPacPoeState setSelectedState(CellState cellState) {
        board[selected.getRow()][selected.getColumn()] = cellState;
        return new PicPacPoeState(board, Cell.DEFAULT);
    }

    public static PicPacPoeState create() {
        // Step 1. Creating default board
        CellState[][] board = new CellState[3][3];
        for (CellState[] row : board)
            Arrays.fill(row, CellState.DEFAULT);
        // Step 2. Returning new state
        return new PicPacPoeState(board, Cell.DEFAULT);
    }

    @JsonCreator
    public static PicPacPoeState create(@JsonProperty("board") CellState[][] board, @JsonProperty("selected") Cell selected) {
        return new PicPacPoeState(board, selected);
    }

    public static GameOutcome fetchOutcome(PicPacPoeState state) {
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

    public static long fetchWinner(PicPacPoeState state) {
        CellState[][] board = state.board;
        return fetcSingleOwner(board[0][0], board[0][1], board[0][2]) // Checking rows
                + fetcSingleOwner(board[1][0], board[1][1], board[1][2])
                + fetcSingleOwner(board[2][0], board[2][1], board[2][2])
                + fetcSingleOwner(board[0][0], board[1][0], board[2][0]) // Checking columns
                + fetcSingleOwner(board[0][1], board[1][1], board[2][1])
                + fetcSingleOwner(board[0][2], board[1][2], board[2][2]) // Checking diagonals
                + fetcSingleOwner(board[0][0], board[1][1], board[2][2])
                + fetcSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private static long fetcSingleOwner(CellState firstCell, CellState secondCell, CellState therdCell) {
        return (firstCell.getOwner() == secondCell.getOwner() && secondCell.getOwner() == therdCell.getOwner())
                ? firstCell.getOwner()
                : PlayerAware.DEFAULT_PLAYER;
    }

    public static boolean canHaveWinner(PicPacPoeState state) {
        CellState[][] board = state.board;
        return canHaveSingleOwner(board[0][0], board[0][1], board[0][2])
                || canHaveSingleOwner(board[1][0], board[1][1], board[1][2])
                || canHaveSingleOwner(board[2][0], board[2][1], board[2][2])
                // Checking columns
                || canHaveSingleOwner(board[0][0], board[1][0], board[2][0])
                || canHaveSingleOwner(board[0][1], board[1][1], board[2][1])
                || canHaveSingleOwner(board[0][2], board[1][2], board[2][2])
                // Checking diagonals
                || canHaveSingleOwner(board[0][0], board[1][1], board[2][2])
                || canHaveSingleOwner(board[0][2], board[1][1], board[2][0]);
    }

    private static boolean canHaveSingleOwner(CellState firstCell, CellState secondCell, CellState therdCell) {
        // In arbitrary line XOX if X is free, it can be owned by O, if O is free it also can be owned by X
        return (firstCell.getOwner() == secondCell.getOwner() && (!firstCell.owned() || !therdCell.owned()))
                || (secondCell.getOwner() == therdCell.getOwner() && (!secondCell.owned() || !firstCell.owned()))
                || (therdCell.getOwner() == firstCell.getOwner() && (!therdCell.owned() || !secondCell.owned()));
    }


}
