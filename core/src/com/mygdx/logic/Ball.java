package com.mygdx.logic;

public class Ball extends ColoredUnit {
    protected Ball() {}

    public Ball(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public Interaction interact(Ball ball, Direction direction) {
        return new Interaction(Direction.NONE, null);
    }

    @Override
    public char getLetter() {
        return 'B';
    }
}
