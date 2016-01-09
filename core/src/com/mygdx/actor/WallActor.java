package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.logic.Wall;

public class WallActor extends Actor {
    private Wall wall;
    private Sprite sprite;

    public WallActor(Wall wall, AssetManager assetManager) {
        this.wall = wall;
        sprite = new Sprite(assetManager.get("wall.png", Texture.class));
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
