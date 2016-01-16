package com.mygdx.logic;

public class Wall extends Unit {
    protected Wall() {}

    public Wall(int row, int column) {
        super(row, column);
    }

    @Override
    public Interaction interactOnEnter(Ball ball, Direction direction) {
        return new Interaction(Direction.NONE, null);
    }

    @Override
    public Interaction interactOnLeave(Ball ball, Direction direction) {
        return new Interaction(Direction.NONE, null);
    }

    @Override
    public char getLetter() {
        return '#';
    }
}
