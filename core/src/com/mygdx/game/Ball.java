package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by acid on 03/01/16.
 */
public class Ball extends Actor {
    Texture boulderImage;

    public Ball(Texture boulderImage) {
        this.boulderImage = boulderImage;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(boulderImage, getX(), getY());
    }
}
