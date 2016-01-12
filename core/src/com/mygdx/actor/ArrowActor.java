package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Direction;
import com.mygdx.logic.Arrow;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class ArrowActor extends UnitActor<Arrow> {
    private static final float HEIGHT = Constants.CELL_SIZE * 0.25f;
    private static final float LENGTH = Constants.CELL_SIZE * 0.4f;

    private Sprite sprite;

    public ArrowActor(Arrow arrow, AssetManager assetManager) {
        super(arrow, assetManager);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        sprite.setColor(GameColors.ARROW);
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float cx = getX() + getWidth() / 2;
        float cy = getY() + getHeight() / 2;

        Direction direction = unit.getDirection();
        if (direction == Direction.LEFT) {
            sprite.setSize(LENGTH + HEIGHT / 2, HEIGHT);
            sprite.setPosition(
                    cx - LENGTH,
                    cy - HEIGHT / 2
            );
        } else if (direction == Direction.RIGHT) {
            sprite.setSize(LENGTH + HEIGHT / 2, HEIGHT);
            sprite.setPosition(
                    cx - HEIGHT / 2,
                    cy - HEIGHT / 2
            );
        } else if (direction == Direction.UP) {
            sprite.setSize(HEIGHT, LENGTH + HEIGHT / 2);
            sprite.setPosition(
                    cx - HEIGHT / 2,
                    cy - HEIGHT / 2
            );
        } else if (direction == Direction.DOWN) {
            sprite.setSize(HEIGHT, LENGTH + HEIGHT / 2);
            sprite.setPosition(
                    cx - HEIGHT / 2,
                    cy - LENGTH
            );
        }
        sprite.draw(batch, parentAlpha);
    }
}