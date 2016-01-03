package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.game.Ball;
import com.mygdx.game.TrashRubbishGame;

public class GameScreen extends BasicScreen {
    public GameScreen(TrashRubbishGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Ball ball = new Ball(game);

        stage.addActor(ball);
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
