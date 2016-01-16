package com.mygdx.logic;

public class Arrow extends Unit {
    private Direction direction;

    protected Arrow() {}

    public Arrow(int row, int column, Direction direction) {
        super(row, column);
        this.direction = direction;
    }

    @Override
    public Interaction interact(Ball ball, Direction direction) {
        return new Interaction(this.direction, null);
    }

    @Override
    public char getLetter() {
        return direction.letter;
    }

    public Direction getDirection() {
        return direction;
    }
}
