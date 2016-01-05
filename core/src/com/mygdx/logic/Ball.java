package com.mygdx.logic;

public class Ball extends Unit {
    public Color color;

    protected Ball() {}

    public Ball(int row, int column, Color color) {
        super(row, column);
        this.color = color;
    }
}
