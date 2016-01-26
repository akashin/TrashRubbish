package com.mygdx.screen;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.actor.*;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.*;
import com.mygdx.util.Constants;

public class LevelHelper {
    public static class Cell {
        int row, column;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static UnitActor createUnitActor(Level level, TrashRubbishGame game, Unit unit) {
        UnitActor unitActor = null;
        if (unit instanceof Ball) {
            unitActor = new BallActor((Ball)unit, game.getAssetManager());
        } else if (unit instanceof Wall) {
            unitActor = new WallActor((Wall)unit, game.getAssetManager());
        } else if (unit instanceof Pedestal) {
            unitActor = new PedestalActor((Pedestal)unit, game.getAssetManager());
        } else if (unit instanceof Arrow) {
            unitActor = new ArrowActor((Arrow)unit, game.getAssetManager());
        } else if (unit instanceof Colorer) {
            unitActor = new ColorerActor((Colorer)unit, game.getAssetManager());
        } else if (unit instanceof ThinWall) {
            unitActor = new ThinWallActor((ThinWall)unit, game.getAssetManager());
        }
        if (unitActor == null) return null;

        if (unit instanceof GridUnit) {
            GridUnit gridUnit = (GridUnit) unit;
            Vector2 position = LevelHelper.cellToVector(level, gridUnit.getRow(), gridUnit.getColumn());
            unitActor.setPosition(position.x, position.y);
        } else if (unit instanceof FenceUnit) {
            FenceUnit fenceUnit = (FenceUnit) unit;
            Vector2 firstPosition = LevelHelper.cellToVector(
                    level,
                    fenceUnit.getFirstRow(),
                    fenceUnit.getFirstColumn()
            );
            Vector2 secondPosition = LevelHelper.cellToVector(
                    level,
                    fenceUnit.getSecondRow(),
                    fenceUnit.getSecondColumn()
            );
            unitActor.setPosition(
                    (firstPosition.x + secondPosition.x) / 2,
                    (firstPosition.y + secondPosition.y) / 2
            );
        }
        return unitActor;
    }

    public static Vector2 cellToVector(Level level, int row, int column) {
        row = level.getRows() - row - 1;
        return new Vector2(column * Constants.CELL_SIZE, row * Constants.CELL_SIZE);
    }

    public static Cell vectorToCell(Level level, float x, float y) {
        int row = level.getRows() - (int)(y / Constants.CELL_SIZE) - 1;
        int column = (int)(x / Constants.CELL_SIZE);
        return new Cell(row, column);
    }

    public static GridUnit createGridUnit(int row, int column, int index) {
        if (index == 0) {
            return new Arrow(row, column, Direction.DOWN);
        } else if (index == 1) {
            return new Ball(row, column, UnitColor.BLUE);
        } else if (index == 2) {
            return new Colorer(row, column, UnitColor.BLUE);
        } else if (index == 3) {
            return new Pedestal(row, column, UnitColor.BLUE);
        } else if (index == 4) {
            return new Wall(row, column);
        } else {
            throw new IllegalArgumentException("No unit type with index " + index);
        }
    }

    public static void changeGridUnit(GridUnit gridUnit) {
        if (gridUnit instanceof Arrow) {
            ((Arrow) gridUnit).setDirection(((Arrow) gridUnit).getDirection().next());
        } else if (gridUnit instanceof Ball) {
            ((Ball) gridUnit).setUnitColor(((Ball) gridUnit).getUnitColor().next());
        } else if (gridUnit instanceof Colorer) {
            ((Colorer) gridUnit).setUnitColor(((Colorer) gridUnit).getUnitColor().next());
        } else if (gridUnit instanceof Pedestal) {
            ((Pedestal) gridUnit).setUnitColor(((Pedestal) gridUnit).getUnitColor().next());
        }
    }
}
