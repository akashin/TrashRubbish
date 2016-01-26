package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Colorer;
import com.mygdx.util.Constants;

public class ColorerActor extends UnitActor<Colorer> {
    private static final float SIZE = Constants.CELL_SIZE * 0.5f;
    private static final float INNER_SIZE = Constants.CELL_SIZE * 0.4f;

    private Sprite sprite;

    public ColorerActor(Colorer unit, AssetManager assetManager) {
        super(unit, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setColor(unit.getUnitColor().color);
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(getX() + (getWidth() - SIZE) / 2, getY() + (getHeight() - SIZE) / 2);
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public UnitActorLevel getActorLevel() {
        return UnitActorLevel.FLOOR;
    }
}
