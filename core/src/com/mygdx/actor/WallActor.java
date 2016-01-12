package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Wall;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class WallActor extends UnitActor<Wall> {
    private static final float SIZE = Constants.CELL_SIZE * 0.9f;

    private Sprite sprite;

    public WallActor(Wall wall, AssetManager assetManager) {
        super(wall, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        sprite.setColor(GameColors.WALL);
        sprite.setSize(SIZE, SIZE);
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX() + (getWidth() - SIZE) / 2, getY() + (getHeight() - SIZE) / 2);
        sprite.draw(batch, parentAlpha);
    }
}
