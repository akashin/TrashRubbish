package com.mygdx.logic.event;

import com.mygdx.logic.UnitColor;

public class ColorChanged implements Event {
    public final int objectId;
    public final int row, column;
    public final UnitColor color;

    public ColorChanged(int objectId, int row, int column, UnitColor color) {
        this.objectId = objectId;
        this.row = row;
        this.column = column;
        this.color = color;
    }
}
