package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.logic.Direction;
import com.mygdx.logic.Arrow;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

public class ArrowActor extends UnitActor<Arrow> {
    private static final float HEIGHT = Constants.CELL_SIZE * 0.4f;
    private static final float LENGTH = Constants.CELL_SIZE * 0.4f;

    private Sprite sprite;
    private Texture texture;
    private TextureRegion region;

    public ArrowActor(Arrow arrow, AssetManager assetManager) {
        super(arrow, assetManager);
        texture = assetManager.get("empty.png", Texture.class);
        region = new TextureRegion(texture, 50, 0, 100, 100);
        sprite = new Sprite(assetManager.get("empty.png", Texture.class));
        sprite.setColor(GameColors.ARROW);
        setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    protected float[] createTriangle(TextureRegion region, float x, float y, float width, float height, Color color) {
        float c = color.toFloatBits();
        float u = region.getU();
        float v = region.getV();
        float u2 = region.getU2();
        float v2 = region.getV2();

        return new float[] {
            x - width / 2, y - height / 2, c, u, v,
            x, y + height / 2, c, u, v2,
            x + width / 2, y - height / 2, c, u2, v,
            x, y, c, u, v
        };
    }

    protected float[] rotate90(float[] vertices, float originX, float originY) {
        for (int i = 0; i < vertices.length; i += 5) {
            float x = vertices[i] - originX;
            float y = vertices[i + 1] - originY;
            vertices[i] = y + originX;
            vertices[i + 1] = -x + originY;
        }
        return vertices;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float cx = getX() + getWidth() / 2;
        float cy = getY() + getHeight() / 2;

        float vertices[] = createTriangle(region, cx, cy, LENGTH, HEIGHT, GameColors.ARROW);

        Direction direction = unit.getDirection();
        int rotates = 0;
        if (direction == Direction.LEFT) {
            rotates = 3;
        } else if (direction == Direction.RIGHT) {
            rotates = 1;
        } else if (direction == Direction.UP) {
            rotates = 0;
        } else if (direction == Direction.DOWN) {
            rotates = 2;
        }
        for (int i = 0; i < rotates; ++i) {
            vertices = rotate90(vertices, cx, cy);
        }
        batch.draw(texture, vertices, 0, vertices.length);
    }

    @Override
    public UnitActorLevel getActorLevel() {
        return UnitActorLevel.FLOOR;
    }
}
