package com.mygdx.logic;

public abstract class ColoredUnit extends GridUnit {
    private UnitColor unitColor;

    protected ColoredUnit() {}

    public ColoredUnit(int row, int column, UnitColor unitColor) {
        super(row, column);
        this.unitColor = unitColor;
    }

    public UnitColor getUnitColor() {
        return unitColor;
    }

    public void setUnitColor(UnitColor unitColor) {
        this.unitColor = unitColor;
    }

}
