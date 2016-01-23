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
    private String title;
    private int firstFreeObjectId = 1;

    private Array<Unit> units = new Array<>();

    protected Level() {
    }

    public Level(int rows, int columns, String title) {
        this.rows = rows;
        this.columns = columns;
        this.title = title;
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

    public Array<Event> getStartingEvents() {
        Array<Event> events = new Array<>();
        for (Ball ball : getBalls()) {
            Array<GridUnit> cellUnits = findGridUnits(ball.getRow(), ball.getColumn());
            for (GridUnit unit : cellUnits) {
                Interaction interaction = unit.interactOnEnter(ball, Direction.NONE);
                if (interaction.events != null) {
                    events.addAll(interaction.events);
                }
            }
        }
        return events;
    }

    public boolean isOnField(int row, int column) {
        return (row >= 0 && column >= 0) && (row < rows && column < columns);
    }

    private boolean isCompleted() {
        for (Ball ball : getBalls()) {
            Array<GridUnit> cellUnits = findGridUnits(ball.getRow(), ball.getColumn());
            boolean isOnPedestal = false;
            for (GridUnit unit : cellUnits) {
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
            Array<GridUnit> nextCellUnits = findGridUnits(nextRow, nextColumn);
            Array<Event> newEvents = new Array<>();
            Array<GridUnit> currentCellUnits = findGridUnits(row, column);
            for (GridUnit unit : currentCellUnits) {
                Interaction interaction = unit.interactOnLeave(ball, direction);
                if (interaction.events != null) {
                    newEvents.addAll(interaction.events);
                }
            }
            boolean canEnter = true;
            for (GridUnit unit : nextCellUnits) {
                canEnter &= unit.canEnter(ball, direction);
            }

            Array<FenceUnit> fenceUnits = findFenceUnits(row, column, direction);
            for (FenceUnit unit : fenceUnits) {
                canEnter &= unit.canPass();
            }

            if (!canEnter) {
                break;
            }

            for (FenceUnit unit : fenceUnits) {
                Interaction interaction = unit.interactOnPass(ball);
                if (interaction.events != null) {
                    newEvents.addAll(interaction.events);
                }
            }

            Direction newDirection = direction;
            for (GridUnit unit : nextCellUnits) {
                Interaction interaction = unit.interactOnEnter(ball, direction);
                if (interaction.events != null) {
                    newEvents.addAll(interaction.events);
                }
                if (interaction.direction != Direction.NONE) {
                    newDirection = interaction.direction;
                }
            }

            row = nextRow;
            column = nextColumn;
            if (newDirection != direction || newEvents.size > 0) {
                events.add(new Movement(ball.getId(), ball.getRow(), ball.getColumn(), row, column));
                direction = newDirection;
                ball.setRow(row);
                ball.setColumn(column);
            }
            events.addAll(newEvents);
        }

        if (ball.getRow() != row || ball.getColumn() != column) {
            events.add(new Movement(ball.getId(), ball.getRow(), ball.getColumn(), row, column));
            ball.setRow(row);
            ball.setColumn(column);
        }

        if (isCompleted()) {
            events.add(new LevelCompleted());
        }

        return events;
    }

    public Array<GridUnit> findGridUnits(int row, int column) {
        Array<GridUnit> cellUnits = new Array<>();
        for (Unit unit : units) {
            if (unit instanceof GridUnit) {
                GridUnit gridUnit = (GridUnit) unit;
                if (gridUnit.getRow() == row && gridUnit.getColumn() == column) {
                    cellUnits.add(gridUnit);
                }
            }
        }
        return cellUnits;
    }

    public Array<FenceUnit> findFenceUnits(int row, int column, Direction direction) {
        Array<FenceUnit> fenceUnits = new Array<>();
        for (Unit unit : units) {
            if (unit instanceof FenceUnit) {
                FenceUnit fenceUnit = (FenceUnit) unit;
                if (fenceUnit.isOnPath(row, column, direction)) {
                    fenceUnits.add(fenceUnit);
                }
            }
        }
        return fenceUnits;
    }

    @Override
    public String toString() {
        char array[][] = new char[rows][columns];
        for (int row = 0; row < rows; ++row) {
            Arrays.fill(array[row], '.');
        }

        for (Unit unit : units) {
            if (unit instanceof GridUnit) {
                GridUnit gridUnit = (GridUnit) unit;
                array[gridUnit.getRow()][gridUnit.getColumn()] = gridUnit.getLetter();
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < rows; ++row) {
            builder.append(array[row]);
            builder.append("\n");
        }
        return builder.toString();
    }

    public static Level createDefaultLevel() {
        Level level = new Level(4, 4, "Default Level");
        level.addUnit(new Ball(0, 2, UnitColor.BLUE));
        level.addUnit(new Ball(3, 1, UnitColor.GREEN));
        level.addUnit(new Pedestal(0, 1, UnitColor.BLUE));
        level.addUnit(new Pedestal(3, 2, UnitColor.GREEN));
        level.addUnit(new Wall(0, 3));
        level.addUnit(new Wall(3, 0));
        level.addUnit(new Arrow(2, 3, Direction.LEFT));
        level.addUnit(new Arrow(1, 3, Direction.DOWN));
        return level;
    }

    @Override
    public void write(Json json) {
        json.writeValue("rows", rows);
        json.writeValue("columns", columns);

        json.writeArrayStart("units");
        for (Unit unit : units) {
            json.writeObjectStart(unit.getClass(), GridUnit.class);
            json.writeFields(unit);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        rows = jsonData.getInt("rows");
        columns = jsonData.getInt("columns");
        title = jsonData.getString("title");
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

    public String getTitle() {
        return title;
    }
}
