package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.actor.LevelBackground;
import com.mygdx.actor.UnitActor;
import com.mygdx.actor.UnitActorLevel;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.*;
import com.mygdx.util.Constants;

import java.util.HashMap;

public class EditorScreen extends BasicScreen {
    private Level level;

    private HashMap<Integer, Actor> actors;
    private Group levelGroup;
    private LevelBackground levelBackground;

    private Group floor;
    private Group middle;

    private Group leftBottom;
    private Group rightBottom;

    private int stroke = -1;
    private LevelHelper.Cell cellUnderMouse = null;

    public EditorScreen(TrashRubbishGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        FileHandle fileHandle = Gdx.files.local("untitled.json");
        if (fileHandle.exists()) {
            Json json = new Json(JsonWriter.OutputType.json);
            json.setUsePrototypes(false);
            level = json.fromJson(Level.class, fileHandle);
        } else {
            level = new Level(6, 6, "untitled");
        }

        actors = new HashMap<>();

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
        levelBackground.setSize(levelGroup.getWidth(), levelGroup.getHeight());
        levelBackground.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelHelper.Cell cell = LevelHelper.vectorToCell(level, x, y);
                float delta = 0.1f * Constants.CELL_SIZE;

                for (int column = 1; column < level.getColumns(); ++column) {
                    float columnX = LevelHelper.cellToVector(level, 0, column).x;
                    if (x >= columnX - delta && x <= columnX + delta) {
                        int row = cell.row;
                        Array<FenceUnit> units = level.findFenceUnits(row, column - 1, Direction.RIGHT);
                        if (units.size == 0) {
                            addUnit(new ThinWall(row, column - 1, row, column), true);
                        } else {
                            for (Unit unit : units) {
                                removeUnit(unit);
                            }
                        }
                        return;
                    }
                }

                for (int row = 0; row < level.getRows(); ++row) {
                    float rowY = LevelHelper.cellToVector(level, row, 0).y;
                    if (y >= rowY - delta && y <= rowY + delta) {
                        int column = cell.column;
                        Array<FenceUnit> units = level.findFenceUnits(row, column, Direction.DOWN);
                        if (units.size == 0) {
                            addUnit(new ThinWall(row, column, row + 1, column), true);
                        } else {
                            for (Unit unit : units) {
                                removeUnit(unit);
                            }
                        }
                        return;
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    Gdx.app.log("Editor", "Change at " + cell.row + " " + cell.column);

                    Array<GridUnit> units = level.findGridUnits(cell.row, cell.column);
                    if (units.size > 0) {
                        GridUnit foundUnit = units.get(0);
                        for (Unit unit : units) {
                            removeUnit(unit);
                        }
                        LevelHelper.changeGridUnit(foundUnit);
                        addUnit(foundUnit, true);
                    }
                } else {
                    Gdx.app.log("Editor", "Create " + stroke + " at " + cell.row + " " + cell.column);

                    Array<GridUnit> units = level.findGridUnits(cell.row, cell.column);
                    for (Unit unit : units) {
                        removeUnit(unit);
                    }
                    if (stroke != -1) {
                        addUnit(LevelHelper.createGridUnit(cell.row, cell.column, stroke), true);
                    }
                }
            }
        });
        levelGroup.addActor(levelBackground);

        floor = new Group();
        levelGroup.addActor(floor);

        middle = new Group();
        levelGroup.addActor(middle);

        stage.addActor(levelGroup);

        rightBottom = new Group();
        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.setPosition(-backButton.getWidth(), 0);
        rightBottom.addActor(backButton);
        stage.addActor(rightBottom);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Json json = new Json(JsonWriter.OutputType.json);
                json.setUsePrototypes(false);
                Gdx.files.local("untitled.json").writeString(json.prettyPrint(level), false);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        for (Unit unit : level.getUnits()) {
            addUnit(unit, false);
        }
    }

    private void addUnit(Unit unit, boolean addToLevel) {
        if (addToLevel) {
            level.addUnit(unit);
        }

        UnitActor unitActor = LevelHelper.createUnitActor(level, game, unit);
        if (unitActor == null) return;

        unitActor.setTouchable(Touchable.disabled);

        if (unitActor.getActorLevel() == UnitActorLevel.FLOOR) {
            floor.addActor(unitActor);
        } else {
            middle.addActor(unitActor);
        }

        actors.put(unit.getId(), unitActor);
    }

    private void removeUnit(Unit unit) {
        level.removeUnit(unit);

        Actor actor = actors.get(unit.getId());
        floor.removeActor(actor);
        middle.removeActor(actor);
        actors.remove(unit.getId());
    }

    @Override
    public void renderScreen() {
        stage.draw();
    }

    @Override
    public void updateScreen(float delta) {
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
        rightBottom.setPosition(width - 20 * game.getScale(), 20 * game.getScale());
    }

    @Override
    public boolean keyTyped(char character) {
        if (character >= '1' && character <= '5') {
            stroke = character - '0' - 1;
            Gdx.app.log("Editor", "Stroke = " + stroke);
            return true;
        }
        if (character == 'e') {
            stroke = -1;
            Gdx.app.log("Editor", "Stroke = erase" + stroke);
            return true;
        }
        return false;
    }
}
