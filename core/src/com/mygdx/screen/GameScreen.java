package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private Level level;
    private Queue<BasicAction> actionQueue;
    private boolean levelCompleted = false;

    private HashMap<Integer, Actor> actors;
    private Group levelGroup;
    private LevelBackground levelBackground;

    private Group floor;
    private Group middle;

    private Group leftBottom;
    private Group rightBottom;
    private Group centerBottom;
    private Group centerTop;

    private TextButton previousLevel;
    private TextButton nextLevel;

    private final String packageDirectory;
    private final int levelIndex;
    private final String levelFile;

    public GameScreen(TrashRubbishGame game, String packageDirectory, int levelIndex) {
        super(game);
        this.packageDirectory = packageDirectory;
        this.levelIndex = levelIndex;
        this.levelFile = game.getPackage("default").get(levelIndex);
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
//        level = json.fromJson(Level.class, Gdx.files.local("untitled.json"));
//        level = Level.generateRandomLevel();
//        level = Level.createDefaultLevel();

        levelGroup = new Group();
        levelGroup.setSize(
                level.getColumns() * Constants.CELL_SIZE,
                level.getRows() * Constants.CELL_SIZE
        );
        levelGroup.setScale(game.getScale());

        levelBackground = new LevelBackground(
                level.getRows(),
                level.getColumns(),
                game.getAssetManager()
        );
        levelGroup.addActor(levelBackground);

        floor = new Group();
        levelGroup.addActor(floor);

        middle = new Group();
        levelGroup.addActor(middle);

        stage.addActor(levelGroup);

        leftBottom = new Group();
        TextButton retryButton = new TextButton("Retry", game.getSkin());
        leftBottom.addActor(retryButton);
        stage.addActor(leftBottom);

        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, packageDirectory, levelIndex));
            }
        });

        rightBottom = new Group();
        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.setPosition(-backButton.getWidth(), 0);
        rightBottom.addActor(backButton);
        stage.addActor(rightBottom);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        centerBottom = new Group();
        stage.addActor(centerBottom);

        Label levelNumber = new Label(String.valueOf(levelIndex + 1), game.getSkin());
        levelNumber.setPosition(-levelNumber.getWidth() / 2, 0);
        centerBottom.addActor(levelNumber);

        previousLevel = new TextButton("<", game.getSkin());
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

        nextLevel = new TextButton(">", game.getSkin());
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

        centerTop = new Group();
        stage.addActor(centerTop);

        Label levelTitle = new Label(level.getTitle(), game.getSkin());
        levelTitle.setPosition(-levelTitle.getWidth() / 2, -levelTitle.getHeight());
        centerTop.addActor(levelTitle);

        for (final Unit unit : level.getUnits()) {
            UnitActor unitActor = LevelHelper.createUnitActor(level, game, unit);
            if (unitActor == null) continue;

            if (unitActor.getActorLevel() == UnitActorLevel.FLOOR) {
                floor.addActor(unitActor);
            } else {
                middle.addActor(unitActor);
            }

            if (unit instanceof Ball) {
                unitActor.addListener(new ActorGestureListener() {
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
                            handleEvent(levelEvent);
                        }
                    }
                });
            }

            actors.put(unit.getId(), unitActor);
        }

        for (Event event : level.getStartingEvents()) {
            handleEvent(event);
        }
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
        centerTop.setPosition(width / 2, height - 20 * game.getScale());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (levelCompleted && levelIndex + 1 < game.getPackage(packageDirectory).size) {
            game.setScreen(new GameScreen(game, packageDirectory, levelIndex + 1));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void handleEvent(Event event) {
        Gdx.app.log("ballActor.fling", "Event " + event.getClass().getSimpleName());
        if (event instanceof Movement) {
            Movement movement = (Movement) event;
            Vector2 src = LevelHelper.cellToVector(level, movement.srcRow, movement.srcColumn);
            Vector2 dst = LevelHelper.cellToVector(level, movement.dstRow, movement.dstColumn);
            actionQueue.addLast(new MovementAction(
                    src.x,
                    src.y,
                    dst.x,
                    dst.y,
                    actors.get(movement.objectId)
            ));
        }

        if (event instanceof LevelCompleted) {
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

        if (event instanceof BallEntersPedestal) {
            BallEntersPedestal entersPedestal = (BallEntersPedestal) event;
            final BallActor ballActor = (BallActor) actors.get(entersPedestal.objectId);
            actionQueue.addLast(new InstantAction() {
                @Override
                public void act() {
                    ballActor.setPlaced(true);
                }
            });
        }

        if (event instanceof BallLeavesPedestal) {
            BallLeavesPedestal leavesPedestal = (BallLeavesPedestal) event;
            final BallActor ballActor = (BallActor) actors.get(leavesPedestal.objectId);
            actionQueue.addLast(new InstantAction() {
                @Override
                public void act() {
                    ballActor.setPlaced(false);
                }
            });
        }

        if (event instanceof ColorChanged) {
            final ColorChanged colorChanged = (ColorChanged) event;
            final BallActor ballActor = (BallActor) actors.get(colorChanged.objectId);
            actionQueue.addLast(new InstantAction() {
                @Override
                public void act() {
                    ballActor.setUnitColor(colorChanged.color);
                }
            });
        }
    }
}
