package com.gogomaya.server.game.tictactoe;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.cell.CellState;

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

    static public PicPacPoeState create() {
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

}
