package com.mygdx.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.util.Accumulator;

/**
 * Created by acid on 03/01/16.
 */
public abstract class BasicScreen extends InputAdapter implements Screen {
    public static final float TIME_STEP = 1.0f / 60;
    public static final float MAX_DELTA = 0.25f;

    protected final TrashRubbishGame game;
    protected Stage stage;

    private final Accumulator accumulator;

    public BasicScreen(final TrashRubbishGame game_) {
        game = game_;
        accumulator = new Accumulator(TIME_STEP);
    }

    public abstract void renderScreen();

    public abstract void updateScreen(float delta);

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        stage.getBatch().enableBlending();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, MAX_DELTA);
        accumulator.update(delta);

        while (accumulator.useIfReady()) {
            updateScreen(accumulator.getBound());
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderScreen();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
        Gdx.app.log(getClass().getSimpleName(), "Hide");
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "Dispose");
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return false;
    }
}
