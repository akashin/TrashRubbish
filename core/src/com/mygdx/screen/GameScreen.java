package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.actor.*;
import com.mygdx.actor.action.BasicAction;
import com.mygdx.actor.action.InstantAction;
import com.mygdx.actor.action.MovementAction;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.*;
import com.mygdx.logic.event.*;
import com.mygdx.util.Constants;

import java.util.HashMap;

public class GameScreen extends BasicScreen {
    public static final int MAX_CELLS = 6;
    public static final float LEVEL_SIZE = 0.9f;

    private Level level;
    private Queue<BasicAction> actionQueue;
    private boolean levelCompleted = false;

    private HashMap<Integer, Actor> actors;
    private Group levelGroup;
    private Group floor;
    private Group middle;
    private Group ceil;
    private Group leftBottom;
    private Group rightBottom;
    private Group centerBottom;

    private final String packageDirectory;
    private final int levelIndex;
    private final String levelFile;

    public GameScreen(TrashRubbishGame game, String packageDirectory, int levelIndex) {
        super(game);
        this.packageDirectory = packageDirectory;
        this.levelIndex = levelIndex;
        this.levelFile = game.getPackage("default").get(levelIndex);
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
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        level = json.fromJson(Level.class, Gdx.files.internal("packages/" + packageDirectory + "/" + levelFile));

        levelGroup = new Group();
        levelGroup.setScale(game.getScale());

        final LevelBackground levelBackground = new LevelBackground(
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

        leftBottom = new Group();
        TextButton retryButton = new TextButton("Retry", game.getSkin());
        leftBottom.addActor(retryButton);
        stage.addActor(leftBottom);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, packageDirectory, levelIndex));
            }
        });

        rightBottom = new Group();
        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.setPosition(-backButton.getWidth(), 0);
        rightBottom.addActor(backButton);
        stage.addActor(rightBottom);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        centerBottom = new Group();
        stage.addActor(centerBottom);

        TextButton levelNumber = new TextButton(String.valueOf(levelIndex + 1), game.getSkin());
        levelNumber.setPosition(-levelNumber.getWidth() / 2, 0);
        centerBottom.addActor(levelNumber);

        TextButton previousLevel = new TextButton("<", game.getSkin());
        previousLevel.setPosition(
                -(levelNumber.getWidth() / 2) - previousLevel.getWidth() - 10 * game.getScale(),
                0
        );
        if (levelIndex == 0) {
            previousLevel.setDisabled(true);
        }
        previousLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, packageDirectory, levelIndex - 1));
            }
        });
        centerBottom.addActor(previousLevel);

        final TextButton nextLevel = new TextButton(">", game.getSkin());
        nextLevel.setPosition(levelNumber.getWidth() / 2 + 10 * game.getScale(), 0);
        if (levelIndex == game.getLastLevelIndex()) {
            nextLevel.setDisabled(true);
        }
        nextLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, packageDirectory, levelIndex + 1));
            }
        });
        centerBottom.addActor(nextLevel);

        for (final Unit unit : level.getUnits()) {
            Actor actor = null;
            if (unit instanceof Ball) {
                actor = new BallActor((Ball)unit, game.getAssetManager());
                actor.addListener(new ActorGestureListener() {
                    @Override
                    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                        if (actionQueue.size != 0 || levelCompleted) {
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
                            Gdx.app.log("ballActor.fling", "Event " + levelEvent.getClass().getSimpleName());
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
                                levelCompleted = true;
                                game.levelCompleted(levelIndex);
                                actionQueue.addLast(new InstantAction() {
                                    @Override
                                    public void act() {
                                        levelBackground.setCompleted(true);
                                        if (levelIndex != game.getLastLevelIndex()) {
                                            nextLevel.setDisabled(false);
                                        }
                                    }
                                });
                                Gdx.app.log("ballActor.fling", "Level completed!");
                            }

                            if (levelEvent instanceof BallEntersPedestal) {
                                BallEntersPedestal entersPedestal = (BallEntersPedestal) levelEvent;
                                final BallActor ballActor = (BallActor) actors.get(entersPedestal.objectId);
                                actionQueue.addLast(new InstantAction() {
                                    @Override
                                    public void act() {
                                        ballActor.setPlaced(true);
                                    }
                                });
                            }

                            if (levelEvent instanceof BallLeavesPedestal) {
                                BallLeavesPedestal leavesPedestal = (BallLeavesPedestal) levelEvent;
                                final BallActor ballActor = (BallActor) actors.get(leavesPedestal.objectId);
                                actionQueue.addLast(new InstantAction() {
                                    @Override
                                    public void act() {
                                        ballActor.setPlaced(false);
                                    }
                                });
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

        Array<Event> startingEvents = level.getStartingEvents();
        // TODO Do something with me!
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
        levelGroup.setScale(game.getScale());
        levelGroup.setPosition(
                (width - levelGroup.getWidth() * game.getScale()) / 2,
                (height - levelGroup.getHeight() * game.getScale()) / 2
        );
        leftBottom.setPosition(20 * game.getScale(), 20 * game.getScale());
        rightBottom.setPosition(width - 20 * game.getScale(), 20 * game.getScale());
        centerBottom.setPosition(width / 2, 20 * game.getScale());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (levelCompleted && levelIndex + 1 < game.getPackage(packageDirectory).size) {
            game.setScreen(new GameScreen(game, packageDirectory, levelIndex + 1));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
