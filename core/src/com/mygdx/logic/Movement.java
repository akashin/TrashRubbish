package com.mygdx.logic;

public class Movement implements Event {
    int objectId;
    int srcRow, srcColumn;
    int dstRow, dstColumn;

    public Movement(int objectId, int srcRow, int srcColumn, int dstRow, int dstColumn) {
        this.objectId = objectId;
        this.srcRow = srcRow;
        this.srcColumn = srcColumn;
        this.dstRow = dstRow;
        this.dstColumn = dstColumn;
    }
}
