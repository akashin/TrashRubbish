package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.ColorChanged;
import com.mygdx.logic.event.Event;

public class Colorer extends ColoredUnit {
    @Override
    public Interaction interactOnEnter(Ball ball, Direction direction) {
        Array<Event> events = new Array<>();
        events.add(new ColorChanged(ball.getId(), ball.getRow(), ball.getColumn(), getUnitColor()));
        ball.setUnitColor(getUnitColor());
        return new Interaction(direction, events);
    }

    @Override
    public Interaction interactOnLeave(Ball ball, Direction direction) {
        return new Interaction(Direction.NONE, null);
    }

    @Override
    public char getLetter() {
        return 'C';
    }
}
