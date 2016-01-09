package com.mygdx.logic;

import com.badlogic.gdx.utils.Array;
import com.mygdx.logic.event.Event;

public class Pipe extends Unit {
    Direction[] directions = new Direction[2];

    protected Pipe() { }

    public Pipe(int row, int column, Direction firstDirection, Direction secondDirection) {
        super(row, column);
        directions[0] = firstDirection;
        directions[1] = secondDirection;
    }

    @Override
    public Direction interact(Ball ball, Direction direction, Array<Event> events) {
        for (int i = 0; i < 2; ++i) {
            if (directions[i].getOpposite() == direction) {
                return directions[1 - i];
            }
        }
        return Direction.NONE;
    }

    public Direction[] getDirections() {
        return directions;
    }

    @Override
    public char getLetter() {
        return '=';
    }
}
