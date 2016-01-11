package com.mygdx.logic;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.util.GameColors;

public enum UnitColor {
    RED(GameColors.RED),
    BLUE(GameColors.BLUE);

    public final Color color;

    UnitColor(Color color) {
        this.color = color;
    }
}
