package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.BallEntersPedestal;
import com.mygdx.logic.event.BallLeavesPedestal;
import com.mygdx.logic.event.Event;

public class Pedestal extends ColoredUnit {
    protected Pedestal() {}

    public Pedestal(int row, int column, UnitColor unitColor) {
        super(row, column, unitColor);
    }

    @Override
    public Interaction interactOnEnter(Ball ball, Direction direction) {
        Array<Event> events = new Array<>();
        if (ball.getUnitColor() == this.getUnitColor()) {
            events.add(new BallEntersPedestal(ball.getId(), getRow(), getColumn()));
        }
        return new Interaction(direction, events);
    }

    @Override
    public Interaction interactOnLeave(Ball ball, Direction direction) {
        Array<Event> events = new Array<>();
        if (ball.getUnitColor() == this.getUnitColor()) {
            events.add(new BallLeavesPedestal(ball.getId(), getRow(), getColumn()));
        }
        return new Interaction(direction, events);
    }

    @Override
    public char getLetter() {
        return 'P';
    }
}
