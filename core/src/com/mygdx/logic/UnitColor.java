package com.mygdx.logic;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.util.GameColors;

public enum UnitColor {
    RED(GameColors.RED),
    BLUE(GameColors.BLUE),
    GREEN(GameColors.GREEN);

    public final Color color;

    UnitColor(Color color) {
        this.color = color;
    }
}
