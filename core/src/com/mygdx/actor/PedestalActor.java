package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Pedestal;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class PedestalActor extends UnitActor<Pedestal> {
    private static final float SIZE = Constants.CELL_SIZE * 0.9f;
    private static final float INNER_SIZE = Constants.CELL_SIZE * 0.8f;

    private Sprite sprite;

    public PedestalActor(Pedestal pedestal, AssetManager assetManager) {
        super(pedestal, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setColor(unit.getUnitColor().color);
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(getX() + (getWidth() - SIZE) / 2, getY() + (getHeight() - SIZE) / 2);
        sprite.draw(batch, parentAlpha);

        sprite.setColor(GameColors.BACKGROUND);
        sprite.setSize(INNER_SIZE, INNER_SIZE);
        sprite.setPosition(getX() + (getWidth() - INNER_SIZE) / 2, getY() + (getHeight() - INNER_SIZE) / 2);
        sprite.draw(batch, parentAlpha);
    }
}
