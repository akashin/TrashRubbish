package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.logic.Ball;

public class BallActor extends Actor {
    private Ball ball;
    private Sprite sprite;

    public BallActor(Ball ball, AssetManager assetManager) {
        this.ball = ball;
        sprite = new Sprite(assetManager.get("boulder.png", Texture.class));
        sprite.setColor(ball.getUnitColor().color);
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
