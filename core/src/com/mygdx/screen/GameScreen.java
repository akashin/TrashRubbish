package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.actor.BallActor;
import com.mygdx.actor.LevelBackground;
import com.mygdx.actor.PedestalActor;
import com.mygdx.actor.WallActor;
import com.mygdx.actor.action.BasicAction;
import com.mygdx.actor.action.MovementAction;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.*;

import java.util.HashMap;

public class GameScreen extends BasicScreen {
    static final int CELL_HEIGHT = 64;
    static final int CELL_WIDTH = 64;

    private Level level;
    private Queue<BasicAction> actionQueue;

    private HashMap<Integer, Actor> actors;
    private Group background;
    private Group foreground;

    public GameScreen(TrashRubbishGame game) {
        super(game);
    }

    class Cell {
        int row, column;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public Vector2 cellToVector(int row, int column) {
        row = level.getRows() - row - 1;
        return new Vector2(column * CELL_WIDTH, row * CELL_HEIGHT);
    }

    public Cell vectorToCell(float x, float y) {
        int row = level.getRows() - (int)(y / CELL_HEIGHT) - 1;
        int column = (int)(x / CELL_WIDTH);
        return new Cell(row, column);
    }

    @Override
    public void show() {
        super.show();

        actionQueue = new Queue<>();
        actors = new HashMap<>();

        // Load level from file.
        Json json = new Json();
        json.setUsePrototypes(false);
        level = json.fromJson(Level.class, Gdx.files.internal("levels/1.json"));

        System.err.println(level.toString());

        LevelBackground levelBackground = new LevelBackground(
                level.getRows(),
                level.getColumns(),
                game.getAssetManager()
        );
        stage.addActor(levelBackground);

        background = new Group();
        foreground = new Group();

        stage.addActor(background);
        stage.addActor(foreground);

        for (final Unit unit : level.getUnits()) {
            Actor actor = null;
            if (unit instanceof Ball) {
                actor = new BallActor((Ball)unit, game.getAssetManager());
                actor.addListener(new ActorGestureListener() {
                    @Override
                    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                        float maxAbsDelta = Math.max(Math.abs(velocityX), Math.abs(velocityY));
                        if (maxAbsDelta < 10) {
                            return;
                        }

                        Direction direction;
                        if (Math.abs(velocityX) > Math.abs(velocityY)) {
                            direction = velocityX > 0 ? Direction.RIGHT : Direction.LEFT;
                        } else {
                            direction = velocityY > 0 ? Direction.UP : Direction.DOWN;
                        }

                        Array<Event> events = level.move(unit.getId(), direction);
                        System.err.println(level.toString());
                        Gdx.app.log("ballActor.fling", "Got " + events.size + " events");
                        for (Event levelEvent : events) {
                            if (levelEvent instanceof Movement) {
                                Movement movement = (Movement) levelEvent;
                                Vector2 src = cellToVector(movement.srcRow, movement.srcColumn);
                                Vector2 dst = cellToVector(movement.dstRow, movement.dstColumn);
                                actionQueue.addLast(new MovementAction(
                                        src.x,
                                        src.y,
                                        dst.x,
                                        dst.y,
                                        actors.get(movement.objectId)
                                ));
                            }
                        }
                    }
                });
                foreground.addActor(actor);
            } else if (unit instanceof Wall) {
                actor = new WallActor((Wall)unit, game.getAssetManager());
                foreground.addActor(actor);
            } else if (unit instanceof Pedestal) {
                actor = new PedestalActor((Pedestal)unit, game.getAssetManager());
                background.addActor(actor);
            }
            if (actor != null) {
                Vector2 position = cellToVector(unit.getRow(), unit.getColumn());
                actor.setPosition(position.x, position.y);
            }
            actors.put(unit.getId(), actor);
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
