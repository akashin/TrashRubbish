package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class Ball extends Actor {
    private Sprite sprite;

    public Ball(TrashRubbishGame game) {
        sprite = new Sprite(game.getAssetManager().get("boulder.png", Texture.class));
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());

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
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
