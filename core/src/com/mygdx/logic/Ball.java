package com.mygdx.logic;

public class Ball extends ColoredUnit {
    protected Ball() {}

    public Ball(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }

    @Override
    public char getLetter() {
        return 'B';
    }
}
