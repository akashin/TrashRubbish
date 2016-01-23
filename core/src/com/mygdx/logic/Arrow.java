package com.mygdx.logic;

public class Arrow extends GridUnit {
    private Direction direction;

    protected Arrow() {}

    public Arrow(int row, int column, Direction direction) {
        super(row, column);
        this.direction = direction;
    }

    @Override
    public boolean canEnter(Ball ball, Direction direction) {
        return true;
    }

    @Override
    public Interaction interactOnEnter(Ball ball, Direction direction) {
        return new Interaction(this.direction, null);
    }

    @Override
    public Interaction interactOnLeave(Ball ball, Direction direction) {
        return new Interaction(Direction.NONE, null);
    }

    @Override
    public char getLetter() {
        return direction.letter;
    }

    public Direction getDirection() {
        return direction;
    }
}
