package com.mygdx.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.Arrays;

public class Level implements Json.Serializable {
    int rows, columns;

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    int firstFreeObjectId = 1;

    Array<Ball> balls = new Array<>();
    Array<Pedestal> pedestals = new Array<>();
    Array<Wall> walls = new Array<>();

    private Level() {
    }

    public Level(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    int addBall(Ball ball) {
        assert !isOccupied(ball.row, ball.column);
        ball.id = firstFreeObjectId++;
        balls.add(ball);
        return ball.id;
    }

    public Array<Ball> getBalls() {
        return balls;
    }

    public Array<Pedestal> getPedestals() {
        return pedestals;
    }

    public Array<Wall> getWalls() {
        return walls;
    }

    int addPedestal(Pedestal pedestal) {
        assert !isOccupied(pedestal.row, pedestal.column);
        pedestal.id = firstFreeObjectId++;
        pedestals.add(pedestal);
        return pedestal.id;
    }

    int addWall(Wall wall) {
        assert !isOccupied(wall.row, wall.column);
        wall.id = firstFreeObjectId++;
        walls.add(wall);
        return wall.id;
    }

    boolean isOnField(int row, int column) {
        return (row >= 0 && column >= 0) && (row < rows && column < columns);
    }

    boolean isOccupied(int row, int column) {
        Ball ball = findObject(row, column, balls);
        if (ball != null) {
            return true;
        }
        Wall wall = findObject(row, column, walls);
        if (wall != null) {
            return true;
        }
        return false;
    }

    public Array<Event> move(int id, Direction direction) {
        for (Ball ball : balls) {
            if (ball.id == id) {
                return move(ball, direction);
            }
        }
        Gdx.app.log("Level::move", "No ball with id (" + id + ")");
        return new Array<Event>();
    }

    Array<Event> move(Ball ball, Direction direction) {
        Array<Event> events = new Array<Event>();

        int dRow = 0;
        int dColumn = 0;
        switch (direction) {
            case UP:
                dRow = -1;
                break;
            case DOWN:
                dRow = +1;
                break;
            case LEFT:
                dColumn = -1;
                break;
            case RIGHT:
                dColumn = +1;
                break;
        }

        int curRow = ball.row;
        int curColumn = ball.column;
        while (true) {
            int nextRow = curRow + dRow;
            int nextColumn = curColumn + dColumn;
            if (!isOnField(nextRow, nextColumn) || isOccupied(nextRow, nextColumn)) {
                break;
            }
            curRow = nextRow;
            curColumn = nextColumn;
        }

        events.add(new Movement(ball.id, ball.row, ball.column, curRow, curColumn));
        ball.row = curRow;
        ball.column = curColumn;
        return events;
    }

    public <T extends Unit> T findObject(int row, int column, Array<T> objects) {
        for (T object : objects) {
            if (object.row == row && object.column == column) {
                return object;
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
        for (Ball ball : balls) {
            array[ball.row][ball.column] = 'B';
        }
        for (Pedestal pedestal : pedestals) {
            array[pedestal.row][pedestal.column] = 'P';
        }
        for (Wall wall : walls) {
            array[wall.row][wall.column] = '#';
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
        level.addBall(new Ball(0, 2, Color.BLUE));
        level.addBall(new Ball(3, 1, Color.RED));
        level.addPedestal(new Pedestal(0, 1, Color.BLUE));
        level.addPedestal(new Pedestal(3, 2, Color.RED));
        level.addWall(new Wall(0, 3));
        level.addWall(new Wall(3, 0));
        return level;
    }

    @Override
    public void write(Json json) {
        json.writeValue("rows", rows);
        json.writeValue("columns", columns);

        json.writeArrayStart("pedestals");
        for (Pedestal pedestal : pedestals) {
            json.writeValue(pedestal);
        }
        json.writeArrayEnd();

        json.writeArrayStart("balls");
        for (Ball ball : balls) {
            json.writeValue(ball);
        }
        json.writeArrayEnd();

        json.writeArrayStart("walls");
        for (Wall wall : walls) {
            json.writeValue(wall);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        rows = jsonData.getInt("rows");
        columns = jsonData.getInt("columns");
        Array<Pedestal> jsonPedestals = json.readValue("pedestals", Array.class, Pedestal.class, jsonData);
        for (Pedestal pedestal : jsonPedestals) {
            addPedestal(pedestal);
        }
        Array<Wall> jsonWalls = json.readValue("walls", Array.class, Wall.class, jsonData);
        for (Wall wall : jsonWalls) {
            addWall(wall);
        }
        Array<Ball> jsonBalls = json.readValue("balls", Array.class, Ball.class, jsonData);
        for (Ball ball : jsonBalls) {
            addBall(ball);
        }
    }
}
