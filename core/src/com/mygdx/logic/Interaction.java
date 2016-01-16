package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Interaction {
    Direction direction;
    Array<Event> events;

    public Interaction(Direction direction, Array<Event> events) {
        this.direction = direction;
        this.events = events;
    }
}
