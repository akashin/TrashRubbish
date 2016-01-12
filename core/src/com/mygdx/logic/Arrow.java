package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Arrow extends Unit {
    private Direction direction;

    protected Arrow() {}

    public Arrow(int row, int column, Direction direction) {
        super(row, column);
        this.direction = direction;
    }

    @Override
    public Direction interact(Ball ball, Direction direction, Array<Event> events) {
        return this.direction;
    }

    @Override
    public char getLetter() {
        return direction.letter;
    }

    public Direction getDirection() {
        return direction;
    }
}
