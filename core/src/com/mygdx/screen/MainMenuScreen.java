package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TrashRubbishGame;

/**
 * Created by acid on 03/01/16.
 */
public class MainMenuScreen extends BasicScreen {
    SpriteBatch batch;
    Texture img;

    public MainMenuScreen(final TrashRubbishGame game_) {
        super(game_);

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void renderScreen() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void updateScreen(float delta) {

    }
}
