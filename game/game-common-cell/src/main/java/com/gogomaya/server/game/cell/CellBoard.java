package com.gogomaya.server.game.cell;

import java.io.Serializable;

public interface CellBoard extends Serializable {

    public CellState[][] getBoard();

    public boolean owned(int row, int column);

}
