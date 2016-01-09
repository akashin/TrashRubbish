package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.logic.Direction;
import com.mygdx.logic.Pipe;

public class PipeActor extends Actor {
    private Pipe pipe;
    private Sprite sprite;

    public PipeActor(Pipe pipe, AssetManager assetManager) {
        this.pipe = pipe;
        sprite = new Sprite(assetManager.get("pipe.png", Texture.class));
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
