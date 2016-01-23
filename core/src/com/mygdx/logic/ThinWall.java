package com.mygdx.logic;

public class ThinWall extends FenceUnit {
    protected ThinWall() {}

    public ThinWall(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        super(firstRow, firstColumn, secondRow, secondColumn);
    }

    @Override
    public boolean canPass() {
        return false;
    }

    @Override
    public Interaction interactOnPass(Ball ball) {
        return new Interaction(Direction.NONE, null);
    }
}
