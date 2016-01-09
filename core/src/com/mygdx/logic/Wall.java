package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Wall extends Unit {
    protected Wall() {}

    public Wall(int row, int column) {
        super(row, column);
    }

    @Override
    public Direction interact(Ball ball, Direction direction, Array<Event> events) {
        return Direction.NONE;
    }

    @Override
    public char getLetter() {
        return '#';
    }
}
