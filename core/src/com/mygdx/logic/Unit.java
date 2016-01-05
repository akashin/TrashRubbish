package com.mygdx.logic;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Unit {
    public int row, column;
    public int id;

    protected Unit() {}

    public Unit(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
