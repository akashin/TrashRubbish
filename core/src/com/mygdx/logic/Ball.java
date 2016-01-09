package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Ball extends ColoredUnit {
    protected Ball() {}

    public Ball(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public Direction interact(Ball ball, Direction direction, Array<Event> events) {
        return Direction.NONE;
    }

    @Override
    public char getLetter() {
        return 'B';
    }
}
