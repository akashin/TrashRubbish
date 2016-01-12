package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class LevelBackground extends Actor {
    private static final float BORDER_SIZE = 3;

    private final int rows;
    private final int columns;
    private final Sprite floor;

    public LevelBackground(int rows, int columns, AssetManager assetManager) {
        this.rows = rows;
        this.columns = columns;
        floor = new Sprite(assetManager.get("empty.png", Texture.class));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        floor.setSize(
                Constants.CELL_SIZE * columns + BORDER_SIZE * 2,
                Constants.CELL_SIZE * rows + BORDER_SIZE * 2
        );
        floor.setPosition(getX() - BORDER_SIZE, getY() - BORDER_SIZE);
        floor.setColor(GameColors.BORDER);
        floor.draw(batch, parentAlpha);

        floor.setSize(Constants.CELL_SIZE * columns, Constants.CELL_SIZE * rows);
        floor.setPosition(getX(), getY());
        floor.setColor(GameColors.SCREEN_BACKGROUND);
        floor.draw(batch, parentAlpha);

        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                float x = Constants.CELL_SIZE * column;
                float y = Constants.CELL_SIZE * (rows - 1 - row);

                floor.setSize(Constants.CELL_SIZE - 2, Constants.CELL_SIZE - 2);
                floor.setColor(GameColors.BACKGROUND);
                floor.setPosition(x + 1, y + 1);
                floor.draw(batch, parentAlpha);
            }
        }
    }
}
