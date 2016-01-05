package com.mygdx.logic;

public class Pedestal extends Unit {
    public Color color;

    protected Pedestal() {}

    public Pedestal(int row, int column, Color color) {
        super(row, column);
        this.color = color;
    }
}
