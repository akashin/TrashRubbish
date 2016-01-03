package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.game.Ball;
import com.mygdx.game.TrashRubbishGame;

/**
 * Created by acid on 03/01/16.
 */
public class GameScreen extends BasicScreen {
    Texture boulderImage;

    public GameScreen(TrashRubbishGame game_) {
        super(game_);
    }

    @Override
    public void show() {
        super.show();
        boulderImage = new Texture(Gdx.files.internal("boulder.png"));
    }

    @Override
    public void dispose() {
        super.dispose();
        boulderImage.dispose();
    }

    @Override
    public void renderScreen() {
        Ball ball = new Ball(boulderImage);
        stage.addActor(ball);
    }

    @Override
    public void updateScreen(float delta) {

    }
}
