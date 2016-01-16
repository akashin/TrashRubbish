package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.BallEntersPedestal;
import com.mygdx.logic.event.Event;

public class Pedestal extends ColoredUnit{
    protected Pedestal() {}

    public Pedestal(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public Interaction interact(Ball ball, Direction direction) {
        Array<Event> events = new Array<>();
        if (ball.getUnitColor() == this.getUnitColor()) {
            events.add(new BallEntersPedestal());
        }
        return new Interaction(direction, events);
    }

    @Override
    public char getLetter() {
        return 'P';
    }
}
