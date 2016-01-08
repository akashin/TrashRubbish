package com.mygdx.logic;

import com.badlogic.gdx.graphics.Color;

public enum UnitColor {
    RED(Color.RED),
    BLUE(Color.BLUE);

    public final Color color;

    UnitColor(Color color) {
        this.color = color;
    }
}
