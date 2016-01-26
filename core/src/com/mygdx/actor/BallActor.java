package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.logic.Ball;
import com.mygdx.logic.UnitColor;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class BallActor extends UnitActor<Ball> {
    private static final float SIZE = Constants.CELL_SIZE * 0.7f;
    private static final float SIZE_WITH_BORDER = Constants.CELL_SIZE * 0.8f;

    private Sprite sprite;
    private boolean placed = false;
    private UnitColor unitColor;

    public BallActor(Ball ball, AssetManager assetManager) {
        super(ball, assetManager);
        unitColor = ball.getUnitColor();
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (placed) {
            sprite.setColor(GameColors.LEVEL_COMPLETED);
        } else {
            sprite.setColor(GameColors.BORDER);
        }
        sprite.setSize(SIZE_WITH_BORDER, SIZE_WITH_BORDER);
        sprite.setPosition(
                getX() + (getWidth() - SIZE_WITH_BORDER) / 2,
                getY() + (getHeight() - SIZE_WITH_BORDER) / 2
        );
        sprite.draw(batch, parentAlpha);

        sprite.setColor(this.unitColor.color);
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(getX() + (getWidth() - SIZE) / 2, getY() + (getHeight() - SIZE) / 2);
        sprite.draw(batch, parentAlpha);
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public void setUnitColor(UnitColor unitColor) {
        this.unitColor = unitColor;
    }

    @Override
    public UnitActorLevel getActorLevel() {
        return UnitActorLevel.MIDDLE;
    }
}
