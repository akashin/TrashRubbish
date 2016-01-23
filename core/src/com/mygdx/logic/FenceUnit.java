package com.mygdx.logic;

public abstract class FenceUnit extends Unit {
    private int firstRow;
    private int firstColumn;

    private int secondRow;
    private int secondColumn;

    protected FenceUnit() {}

    public FenceUnit(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        this.firstRow = firstRow;
        this.firstColumn = firstColumn;
        this.secondRow = secondRow;
        this.secondColumn = secondColumn;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public int getSecondRow() {
        return secondRow;
    }

    public int getSecondColumn() {
        return secondColumn;
    }

    boolean isOnPath(int row, int column, Direction direction) {
        int nextRow = row + direction.dRow;
        int nextColumn = column + direction.dColumn;

        if (row == getFirstRow() && column == getFirstColumn()) {
            return (nextRow == getSecondRow() && nextColumn == getSecondColumn());
        }

        if (row == getSecondRow() && column == getSecondColumn()) {
            return (nextRow == getFirstRow() && nextColumn == getFirstColumn());
        }

        return false;
    }

    public boolean isHorizontal() {
        return firstRow == secondRow;
    }

    public abstract boolean canPass();

    public abstract Interaction interactOnPass(Ball ball);
}
