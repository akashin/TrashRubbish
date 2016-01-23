package com.mygdx.logic;

public class Wall extends GridUnit {
    protected Wall() {}

    public Wall(int row, int column) {
        super(row, column);
    }

    @Override
    public boolean canEnter(Ball ball, Direction direction) {
        return false;
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
