package com.mygdx.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.Arrays;

public class Level implements Json.Serializable {
    private int rows;
    private int columns;
    private int firstFreeObjectId = 1;

    private Array<Unit> units = new Array<>();

    protected Level() {
    }

    public Level(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    private int addUnit(Unit unit) {
        unit.setId(firstFreeObjectId++);
        units.add(unit);
        return unit.getId();
    }

    public Array<Unit> getUnits() {
        return units;
    }

    public Array<Ball> getBalls() {
        Array<Ball> balls = new Array<>();
        for (Unit unit : units) {
            if (unit instanceof Ball) {
                balls.add((Ball)unit);
            }
        }
        return balls;
    }

    public boolean isOnField(int row, int column) {
        return (row >= 0 && column >= 0) && (row < rows && column < columns);
    }

    public boolean isOccupied(int row, int column) {
        Unit unit = findUnit(row, column);
        return unit != null && unit.blocksMovement();
    }

    public Array<Event> move(int id, Direction direction) {
        for (Ball ball : getBalls()) {
            if (ball.getId() == id) {
                return move(ball, direction);
            }
        }
        Gdx.app.log("Level::move", "No ball with id (" + id + ")");
        return new Array<>();
    }

    private Array<Event> move(Ball ball, Direction direction) {
        Array<Event> events = new Array<>();

        int row = ball.getRow();
        int column = ball.getColumn();

        while (true) {
            int nextRow = row + direction.dRow;
            int nextColumn = column + direction.dColumn;
            if (!isOnField(nextRow, nextColumn) || isOccupied(nextRow, nextColumn)) {
                break;
            }
            row = nextRow;
            column = nextColumn;
        }

        events.add(new Movement(ball.getId(), ball.getRow(), ball.getColumn(), row, column));
        ball.setRow(row);
        ball.setColumn(column);
        return events;
    }

    public Unit findUnit(int row, int column) {
        for (Unit unit : units) {
            if (unit.getRow() == row && unit.getColumn() == column) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        char array[][] = new char[rows][columns];
        for (int row = 0; row < rows; ++row) {
            Arrays.fill(array[row], '.');
        }

        for (Unit unit : units) {
            array[unit.getRow()][unit.getColumn()] = unit.getLetter();
        }

        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < rows; ++row) {
            builder.append(array[row]);
            builder.append("\n");
        }
        return builder.toString();
    }

    public static Level createDefaultLevel()
    {
        Level level = new Level(4, 4);
        level.addUnit(new Ball(0, 2, UnitColor.BLUE));
        level.addUnit(new Ball(3, 1, UnitColor.RED));
        level.addUnit(new Pedestal(0, 1, UnitColor.BLUE));
        level.addUnit(new Pedestal(3, 2, UnitColor.RED));
        level.addUnit(new Wall(0, 3));
        level.addUnit(new Wall(3, 0));
        return level;
    }

    @Override
    public void write(Json json) {
        json.writeValue("rows", rows);
        json.writeValue("columns", columns);

        json.writeArrayStart("units");
        for (Unit unit : units) {
            json.writeValue(unit);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
//        rows = jsonData.getInt("rows");
//        columns = jsonData.getInt("columns");
//        Array<Pedestal> jsonPedestals = json.readValue("pedestals", Array.class, Pedestal.class, jsonData);
//        for (Pedestal pedestal : jsonPedestals) {
//            addPedestal(pedestal);
//        }
//        Array<WallActor> jsonWalls = json.readValue("walls", Array.class, WallActor.class, jsonData);
//        for (WallActor wall : jsonWalls) {
//            addWall(wall);
//        }
//        Array<Ball> jsonBalls = json.readValue("balls", Array.class, Ball.class, jsonData);
//        for (Ball ball : jsonBalls) {
//            addBall(ball);
//        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
