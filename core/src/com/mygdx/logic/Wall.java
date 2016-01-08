package com.mygdx.logic;

public class Wall extends Unit {
    protected Wall() {}

    public Wall(int row, int column) {
        super(row, column);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }

    @Override
    public char getLetter() {
        return '#';
    }
}
