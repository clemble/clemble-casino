package com.gogomaya.server.go;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.cell.CellBoard;
import com.gogomaya.server.game.cell.CellState;

public class GoBoard implements CellBoard {

    private static final long serialVersionUID = -3282042914639667829L;

    private CellState[][] board;

    public GoBoard(int size) {
        board = new CellState[3][3];
        for (CellState[] row : board) {
            Arrays.fill(row, CellState.DEFAULT);
        }
    }

    @JsonCreator
    public GoBoard(@JsonProperty("board") CellState[][] board) {
        this.board = board;
    }

    @Override
    public CellState[][] getBoard() {
        return board;
    }

    @Override
    public boolean owned(int row, int column) {
        return board[row][column].owned();
    }
    
    public void setOwned(long owner, Cell cellToOwn) {
        if(owned(cellToOwn.getRow(), cellToOwn.getColumn()))
            throw GogomayaException.fromError(GogomayaError.CellOwned);
        board[cellToOwn.getRow()][cellToOwn.getColumn()] = new CellState(owner);
    }

}
