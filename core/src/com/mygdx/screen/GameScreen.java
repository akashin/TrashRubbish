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
import com.mygdx.actor.*;
import com.mygdx.actor.action.BasicAction;
import com.mygdx.actor.action.MovementAction;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.*;
import com.mygdx.logic.event.Event;
import com.mygdx.logic.event.LevelCompleted;
import com.mygdx.logic.event.Movement;
import com.mygdx.util.Constants;

import java.util.HashMap;

public class GameScreen extends BasicScreen {
    private Level level;
    private Queue<BasicAction> actionQueue;

    private HashMap<Integer, Actor> actors;
    private Group levelGroup;
    private Group floor;
    private Group middle;
    private Group ceil;

    private static final int MAX_CELLS = 6;
    private static final float LEVEL_SIZE = 0.9f;
    private float scale;

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
        return new Vector2(column * Constants.CELL_SIZE, row * Constants.CELL_SIZE);
    }

    public Cell vectorToCell(float x, float y) {
        int row = level.getRows() - (int)(y / Constants.CELL_SIZE) - 1;
        int column = (int)(x / Constants.CELL_SIZE);
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
//        level = json.fromJson(Level.class, Gdx.files.internal("levels/1.json"));
        level = Level.createDefaultLevel();

        System.err.println(level.toString());

        levelGroup = new Group();
        updateScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        levelGroup.setScale(scale);

        LevelBackground levelBackground = new LevelBackground(
                level.getRows(),
                level.getColumns(),
                game.getAssetManager()
        );
        levelGroup.addActor(levelBackground);

        levelGroup.setSize(
                level.getColumns() * Constants.CELL_SIZE,
                level.getRows() * Constants.CELL_SIZE
        );

        floor = new Group();
        levelGroup.addActor(floor);

        middle = new Group();
        levelGroup.addActor(middle);

        ceil = new Group();
        levelGroup.addActor(ceil);

        stage.addActor(levelGroup);

        for (final Unit unit : level.getUnits()) {
            Actor actor = null;
            if (unit instanceof Ball) {
                actor = new BallActor((Ball)unit, game.getAssetManager());
                actor.addListener(new ActorGestureListener() {
                    @Override
                    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                        if (actionQueue.size != 0) {
                            return;
                        }

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

                            if (levelEvent instanceof LevelCompleted) {
                                // TODO: Show some text here or change screen to show achievements.
                                Gdx.app.log("ballActor.fling", "Level completed!");
                            }
                        }
                    }
                });
                middle.addActor(actor);
            } else if (unit instanceof Wall) {
                actor = new WallActor((Wall)unit, game.getAssetManager());
                middle.addActor(actor);
            } else if (unit instanceof Pedestal) {
                actor = new PedestalActor((Pedestal)unit, game.getAssetManager());
                floor.addActor(actor);
            } else if (unit instanceof Arrow) {
                actor = new ArrowActor((Arrow)unit, game.getAssetManager());
                floor.addActor(actor);
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
        while (actionQueue.size != 0) {
            BasicAction action = actionQueue.first();
            delta -= action.act(delta);
            if (action.finished()) {
                actionQueue.removeFirst();
            } else {
                break;
            }
        }
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        updateScale(width, height);
        levelGroup.setScale(scale);
        levelGroup.setPosition(
                (width - levelGroup.getWidth() * scale) / 2,
                (height - levelGroup.getHeight() * scale) / 2
        );
    }

    private void updateScale(int width, int height) {
        float size = Math.min(width, height);
        scale = ((size * LEVEL_SIZE) / MAX_CELLS) / Constants.CELL_SIZE;
    }
}
