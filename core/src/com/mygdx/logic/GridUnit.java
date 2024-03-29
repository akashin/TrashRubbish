package com.mygdx.logic;

public abstract class GridUnit extends Unit {
    private int row;
    private int column;

    protected GridUnit() {}

    public GridUnit(int row, int column) {
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

    public abstract boolean canEnter(Ball ball, Direction direction);

    public abstract Interaction interactOnEnter(Ball ball, Direction direction);

    public abstract Interaction interactOnLeave(Ball ball, Direction direction);

    public abstract char getLetter();
}
