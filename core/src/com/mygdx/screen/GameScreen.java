package com.mygdx.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.actor.Ball;
import com.mygdx.actor.Pedestal;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.actor.Wall;
import com.mygdx.logic.Level;

import java.util.HashMap;

public class GameScreen extends BasicScreen {
    int cellHeight = 64;
    int cellWidth = 64;

    public GameScreen(TrashRubbishGame game) {
        super(game);
    }

    HashMap<Integer, Actor> actors;

    class Cell
    {
        int row, column;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
    };

    Vector2 cellToVector(int row, int column)
    {
        return new Vector2(row * cellHeight, column * cellWidth);
    }

    Cell vectorToCell(float x, float y)
    {
        return new Cell((int)(x / cellHeight), (int)(y / cellWidth));
    }

    @Override
    public void show() {
        super.show();

        actors = new HashMap<>();
        Level level = Level.createDefaultLevel();
        System.err.println(level.toString());

        for (com.mygdx.logic.Ball ball : level.getBalls()) {
            Ball ballActor = new Ball(game);
            Vector2 v = cellToVector(ball.row, ball.column);
            ballActor.setPosition(v.x, v.y);
            ballActor.addListener(new ActorGestureListener() {
                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    Cell cell = vectorToCell(x, y);
                }
            });
            actors.put(ball.id, ballActor);
            stage.addActor(ballActor);
        }

        for (com.mygdx.logic.Pedestal pedestal : level.getPedestals()) {
            Pedestal pedestalActor = new Pedestal(game);
            Vector2 v = cellToVector(pedestal.row, pedestal.column);
            pedestalActor.setPosition(v.x, v.y);
            actors.put(pedestal.id, pedestalActor);
            stage.addActor(pedestalActor);
        }

        for (com.mygdx.logic.Wall wall : level.getWalls()) {
            Wall wallActor = new Wall(game);
            Vector2 v = cellToVector(wall.row, wall.column);
            wallActor.setPosition(v.x, v.y);
            actors.put(wall.id, wallActor);
            stage.addActor(wallActor);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void renderScreen() {
        stage.draw();
    }

    @Override
    public void updateScreen(float delta) {
        stage.act(delta);
    }
}
