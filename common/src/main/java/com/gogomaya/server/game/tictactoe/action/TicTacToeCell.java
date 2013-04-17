package com.gogomaya.server.game.tictactoe.action;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class TicTacToeCell {
    
    final public static TicTacToeCell DEFAULT = new TicTacToeCell(Byte.MIN_VALUE, Byte.MIN_VALUE);

    final private byte row;
    final private byte column;

    @JsonCreator
    public TicTacToeCell(@JsonProperty("row") byte row, @JsonProperty("column") byte column) {
        this.row = row;
        this.column = column;
    }

    public byte getRow() {
        return row;
    }

    public byte getColumn() {
        return column;
    }
}
