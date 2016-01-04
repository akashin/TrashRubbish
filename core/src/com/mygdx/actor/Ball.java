package com.mygdx.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.Color;

public class Ball extends Actor {
    private Sprite sprite;
    public final int id;

    public Ball(int id, Color color, TrashRubbishGame game) {
        this.id = id;
        sprite = new Sprite(game.getAssetManager().get("boulder.png", Texture.class));
        if (color == Color.RED) {
            sprite.setColor(com.badlogic.gdx.graphics.Color.RED);
        }
        if (color == Color.BLUE) {
            sprite.setColor(com.badlogic.gdx.graphics.Color.BLUE);
        }
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
