package com.mygdx.logic;

public enum Direction {
    UP(-1, 0, '^'),
    DOWN(1, 0, 'v'),
    LEFT(0, -1, '<'),
    RIGHT(0, 1, '>'),
    NONE(0, 0, '?');

    public final int dRow;
    public final int dColumn;
    char letter;

    Direction(int dRow, int dColumn, char letter) {
        this.dRow = dRow;
        this.dColumn = dColumn;
        this.letter = letter;
    }

    public Direction getOpposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return NONE;
        }
    }
}
