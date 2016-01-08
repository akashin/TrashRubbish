package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.logic.Pedestal;

public class PedestalActor extends Actor {
    private Pedestal pedestal;
    private Sprite sprite;

    public PedestalActor(Pedestal pedestal, AssetManager assetManager) {
        this.pedestal = pedestal;
        sprite = new Sprite(assetManager.get("pedestal.png", Texture.class));
        sprite.setColor(pedestal.getUnitColor().color);
        sprite.setSize(64, 64);
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch, parentAlpha);
    }
}
