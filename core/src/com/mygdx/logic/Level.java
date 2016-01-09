package com.mygdx.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.logic.event.Event;
import com.mygdx.logic.event.LevelCompleted;
import com.mygdx.logic.event.Movement;

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

    private boolean isCompleted() {
        for (Ball ball : getBalls()) {
            Array<Unit> cellUnits = findUnits(ball.getRow(), ball.getColumn());
            boolean isOnPedestal = false;
            for (Unit unit : cellUnits) {
                if (unit instanceof Pedestal) {
                    if (((Pedestal) unit).getUnitColor() == ball.getUnitColor()) {
                        isOnPedestal = true;
                    }
                }
            }
            if (!isOnPedestal) {
                return false;
            }
        }
        return true;
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
            if (!isOnField(nextRow, nextColumn)) {
                break;
            }
            Array<Unit> cellUnits = findUnits(row, column);
            for (Unit unit : cellUnits) {
                if (unit.blocksMovement(direction)) {
                    break;
                }
            }
            row = nextRow;
            column = nextColumn;
        }

        events.add(new Movement(ball.getId(), ball.getRow(), ball.getColumn(), row, column));
        ball.setRow(row);
        ball.setColumn(column);

        if (isCompleted()) {
            events.add(new LevelCompleted());
        }

        return events;
    }

    public Array<Unit> findUnits(int row, int column) {
        Array<Unit> cellUnits = new Array<>();
        for (Unit unit : units) {
            if (unit.getRow() == row && unit.getColumn() == column) {
                cellUnits.add(unit);
            }
        }
        return cellUnits;
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
            json.writeObjectStart(unit.getClass(), Unit.class);
            json.writeFields(unit);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        rows = jsonData.getInt("rows");
        columns = jsonData.getInt("columns");
        Array<Unit> jsonUnits = json.readValue("units", Array.class, Unit.class, jsonData);
        for (Unit unit : jsonUnits) {
            addUnit(unit);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
