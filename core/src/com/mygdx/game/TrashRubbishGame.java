package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.screen.MainMenuScreen;

public class TrashRubbishGame extends Game {
	private AssetManager assetManager;

    @Override
	public void create() {
        assetManager = new AssetManager();
        assetManager.load("boulder.png", Texture.class);
        assetManager.load("wall.png", Texture.class);
        assetManager.load("pedestal.png", Texture.class);
        assetManager.load("floor2.png", Texture.class);
        assetManager.load("pipe.png", Texture.class);
        assetManager.finishLoading();

        VisUI.load();

        setScreen(new MainMenuScreen(this));
	}

    public AssetManager getAssetManager() {
        return assetManager;
	}

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
