package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.util.Constants;

public class LevelBackground extends Actor {
    private final int rows;
    private final int columns;
    private final Sprite floor;

    public LevelBackground(int rows, int columns, AssetManager assetManager) {
        this.rows = rows;
        this.columns = columns;
        floor = new Sprite(assetManager.get("floor2.png", Texture.class));
        floor.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                float x = Constants.CELL_SIZE * column;
                float y = Constants.CELL_SIZE * (rows - 1 - row);
                floor.setPosition(x, y);
                floor.draw(batch, parentAlpha);
            }
        }
    }
}
