package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {
    private Sprite boulderSprite;

    public Ball(TrashRubbishGame game) {
        boulderSprite = new Sprite(game.getAssetManager().get("boulder.png", Texture.class));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boulderSprite.setPosition(getX(), getY());
        boulderSprite.draw(batch, parentAlpha);
    }
}
