package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Pedestal;
import com.mygdx.util.Constants;

public class PedestalActor extends UnitActor<Pedestal> {
    private static final float SIZE = Constants.CELL_SIZE * 0.9f;

    private Sprite sprite;

    public PedestalActor(Pedestal pedestal, AssetManager assetManager) {
        super(pedestal, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        sprite.setColor(pedestal.getUnitColor().color);
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(getX() + (getWidth() - SIZE) / 2, getY() + (getHeight() - SIZE) / 2);
        sprite.draw(batch, parentAlpha);
    }
}
