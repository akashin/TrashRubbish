package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.ThinWall;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class ThinWallActor extends UnitActor<ThinWall> {
    private static final float HEIGHT = Constants.CELL_SIZE * 1.0f;
    private static final float LENGTH = Constants.CELL_SIZE * 0.05f;

    private Sprite sprite;

    public ThinWallActor(ThinWall unit, AssetManager assetManager) {
        super(unit, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        sprite.setColor(GameColors.WALL);
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);

        if (unit.isHorizontal()) {
            sprite.setSize(LENGTH, HEIGHT);
        } else {
            sprite.setSize(HEIGHT, LENGTH);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (unit.isHorizontal()) {
            sprite.setPosition(getX() + (getWidth() - sprite.getWidth()) / 2, getY());
        } else {
            sprite.setPosition(getX(), getY() + (getHeight() - sprite.getHeight()) / 2);
        }
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public UnitActorLevel getActorLevel() {
        return UnitActorLevel.FLOOR;
    }
}
