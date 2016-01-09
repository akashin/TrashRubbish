package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Pedestal extends ColoredUnit{
    protected Pedestal() {}

    public Pedestal(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public Direction interact(Ball ball, Direction direction, Array<Event> events) {
        return direction;
    }

    @Override
    public char getLetter() {
        return 'P';
    }
}
