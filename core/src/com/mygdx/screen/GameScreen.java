package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.actor.Ball;
import com.mygdx.actor.LevelBackground;
import com.mygdx.actor.Pedestal;
import com.mygdx.actor.Wall;
import com.mygdx.actor.action.BasicAction;
import com.mygdx.actor.action.MovementAction;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.Direction;
import com.mygdx.logic.Event;
import com.mygdx.logic.Level;
import com.mygdx.logic.Movement;

import java.util.HashMap;

public class GameScreen extends BasicScreen {
    int cellHeight = 64;
    int cellWidth = 64;

    Level level;
    Queue<BasicAction> actionQueue;

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
        row = level.getRows() - row - 1;
        return new Vector2(column * cellWidth, row * cellHeight);
    }

    Cell vectorToCell(float x, float y)
    {
        int row = level.getRows() - (int)(y / cellHeight) - 1;
        int column = (int)(x / cellWidth);
        return new Cell(row, column);
    }

    @Override
    public void show() {
        super.show();

        actionQueue = new Queue<>();
        actors = new HashMap<>();
        level = Level.createDefaultLevel();
//        FileHandle handle = Gdx.files.internal("levels/1.json");
        Json json = new Json();
//        level = json.fromJson(Level.class, handle);
        String jsonRepr = json.toJson(level);
        System.err.println(jsonRepr);
        System.err.println(level.toString());

        LevelBackground levelBackground = new LevelBackground(
                level.getHeight(),
                level.getWidth(),
                game.getAssetManager()
        );
        stage.addActor(levelBackground);

        for (com.mygdx.logic.Pedestal pedestal : level.getPedestals()) {
            Pedestal pedestalActor = new Pedestal(pedestal.color, game);
            Vector2 v = cellToVector(pedestal.row, pedestal.column);
            pedestalActor.setPosition(v.x, v.y);
            actors.put(pedestal.id, pedestalActor);
            stage.addActor(pedestalActor);
        }

        for (final com.mygdx.logic.Ball ball : level.getBalls()) {
            final Ball ballActor = new Ball(ball.id, ball.color, game);
            Vector2 v = cellToVector(ball.row, ball.column);
            ballActor.setPosition(v.x, v.y);
            ballActor.addListener(new ActorGestureListener() {
                @Override
                public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                    float maxAbsDelta = Math.max(Math.abs(velocityX), Math.abs(velocityY));
                    if (maxAbsDelta < 10) {
                        return;
                    }

                    Cell cell = vectorToCell(ballActor.getX(), ballActor.getY());
                    Direction direction;

                    if (Math.abs(velocityX) > Math.abs(velocityY)) {
                        direction = velocityX > 0 ? Direction.RIGHT : Direction.LEFT;
                    } else {
                        direction = velocityY > 0 ? Direction.UP : Direction.DOWN;
                    }

                    Array<Event> events = level.move(ballActor.id, direction);
                    System.err.println(level.toString());
                    Gdx.app.log("ballActor.fling", "Got " + events.size + " events");
                    for (Event levelEvent : events) {
                        if (levelEvent instanceof Movement) {
                            Movement movement = (Movement) levelEvent;
                            Vector2 src = cellToVector(movement.srcRow, movement.srcColumn);
                            Vector2 dst = cellToVector(movement.dstRow, movement.dstColumn);
                            actionQueue.addLast(
                                    new MovementAction(src.x, src.y, dst.x, dst.y, actors.get(movement.objectId)));
                        }
                    }
                }
            });
            actors.put(ball.id, ballActor);
            stage.addActor(ballActor);
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
        while (actionQueue.size != 0 && delta > 0) {
            BasicAction action = actionQueue.removeFirst();
            float change = action.act(delta);
            delta -= change;
            if (change > 0) {
                actionQueue.addFirst(action);
            }
        }
        stage.act(delta);
    }
}
