package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class Ball extends Actor {
    private Sprite boulderSprite;

    public Ball(TrashRubbishGame game) {
        boulderSprite = new Sprite(game.getAssetManager().get("boulder.png", Texture.class));
        setSize(boulderSprite.getWidth(), boulderSprite.getHeight());

        addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                setX(getX() + deltaX);
                setY(getY() + deltaY);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boulderSprite.setPosition(getX(), getY());
        boulderSprite.draw(batch, parentAlpha);
    }
}
