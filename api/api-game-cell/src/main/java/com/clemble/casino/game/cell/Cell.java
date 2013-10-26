package com.clemble.casino.game.cell;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cell {

    final public static Cell DEFAULT = new Cell(Byte.MIN_VALUE, Byte.MIN_VALUE);

    final private int row;
    final private int column;

    @JsonCreator
    private Cell(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @JsonCreator
    public static Cell create(@JsonProperty("row") byte row, @JsonProperty("column") byte column) {
        return new Cell(row, column);
    }

    public static Cell create(int row, int column) {
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
        Cell other = (Cell) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }
}
