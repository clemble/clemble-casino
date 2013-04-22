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
}
