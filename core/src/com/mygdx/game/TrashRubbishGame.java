package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.screen.MainMenuScreen;
import com.mygdx.util.Constants;
import com.mygdx.util.GameColors;

import java.util.HashMap;

public class TrashRubbishGame extends Game {
	private AssetManager assetManager;
    private Skin skin;
    private float scale;
    private HashMap<String, Array<String>> packages = new HashMap<>();
    private int lastLevelIndex = 0;

    @Override
	public void create() {
        JsonReader jsonReader = new JsonReader();

        FileHandle packagesDirectory = Gdx.files.internal("packages");

        JsonValue packages = jsonReader.parse(packagesDirectory.child("packages.json")).get("packages");
        for (int index = 0; index < packages.size; ++index) {
            String directory = packages.get(index).getString("directory");
            FileHandle packageDirectory = packagesDirectory.child(directory);
            JsonValue levels = jsonReader.parse(packageDirectory.child("levels.json"));
            Array<String> pkg = new Array<>(levels.get("levels").asStringArray());
            this.packages.put(directory, pkg);
        }

        updateScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        assetManager = new AssetManager();
        assetManager.load("boulder.png", Texture.class);
        assetManager.load("wall.png", Texture.class);
        assetManager.load("pedestal.png", Texture.class);
        assetManager.load("floor2.png", Texture.class);
        assetManager.load("empty.png", Texture.class);
        assetManager.finishLoading();

        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("teen.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (scale * 30);
        BitmapFont menuFont = generator.generateFont(parameter);
        skin.add("menu", menuFont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = menuFont;
        textButtonStyle.fontColor = GameColors.LINES;
        textButtonStyle.disabledFontColor = GameColors.DISABLED;
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = menuFont;
        labelStyle.fontColor = GameColors.LINES;
        skin.add("default", labelStyle);

        setScreen(new MainMenuScreen(this));
	}

    public AssetManager getAssetManager() {
        return assetManager;
	}

    public Skin getSkin() {
        return skin;
    }

    public float getScale() {
        return scale;
    }

    public Array<String> getPackage(String directory) {
        return packages.get(directory);
    }

    public int getLastLevelIndex() {
        return lastLevelIndex;
    }

    public void levelCompleted(int levelIndex) {
        lastLevelIndex = Math.min(getPackage("default").size - 1, Math.max(lastLevelIndex, levelIndex + 1));
    }

    public void updateScale(int width, int height) {
        float size = Math.min(width, height);
        scale = ((size * Constants.LEVEL_SIZE) / Constants.MAX_CELLS) / Constants.CELL_SIZE;
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }

    @Override
    public void resize(int width, int height) {
        updateScale(width, height);
        super.resize(width, height);
    }
}
