package com.gogomaya.server.game.tictactoe.action;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class TicTacToeCell {

    final public static TicTacToeCell DEFAULT = new TicTacToeCell(Byte.MIN_VALUE, Byte.MIN_VALUE);

    final private byte row;
    final private byte column;

    private TicTacToeCell(byte row, byte column) {
        this.row = row;
        this.column = column;
    }

    public byte getRow() {
        return row;
    }

    public byte getColumn() {
        return column;
    }

    @JsonCreator
    public static TicTacToeCell create(@JsonProperty("row") byte row, @JsonProperty("column") byte column) {
        return new TicTacToeCell(row, column);
    }

    public static TicTacToeCell create(int row, int column) {
        return create((byte) row, (byte) column);
    }

    @Override
    public String toString() {
        return "{" + row + ", " + column + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
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
        TicTacToeCell other = (TicTacToeCell) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }
}
