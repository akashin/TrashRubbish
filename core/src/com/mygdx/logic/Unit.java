package com.mygdx.logic;

public abstract class Unit {
    private int row;
    private int column;
    private int id;

    protected Unit() {}

    public Unit(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract boolean blocksMovement();

    public abstract char getLetter();
}
