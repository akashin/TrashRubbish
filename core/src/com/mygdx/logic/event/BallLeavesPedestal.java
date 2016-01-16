package com.mygdx.logic.event;

public class BallLeavesPedestal implements Event {
    public final int objectId;
    public final int row, column;

    public BallLeavesPedestal(int objectId, int row, int column) {
        this.objectId = objectId;
        this.row = row;
        this.column = column;
    }
}
