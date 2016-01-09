package com.mygdx.logic;

public class Pipe extends Unit {
    Direction[] directions = new Direction[2];

    protected Pipe() { }

    public Pipe(int row, int column, Direction firstDirection, Direction secondDirection) {
        super(row, column);
        directions[0] = firstDirection;
        directions[1] = secondDirection;
    }

    @Override
    public boolean blocksMovement(Direction direction) {
        for (int i = 0; i < 2; ++i) {
            if (directions[i].getOpposite() == direction) {
                return false;
            }
        }
        return true;
    }

    @Override
    public char getLetter() {
        return '=';
    }
}
