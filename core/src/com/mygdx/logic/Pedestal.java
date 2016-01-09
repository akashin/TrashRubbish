package com.mygdx.logic;

public class Pedestal extends ColoredUnit{
    protected Pedestal() {}

    public Pedestal(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public boolean blocksMovement(Direction direction) {
        return false;
    }

    @Override
    public char getLetter() {
        return 'P';
    }
}
