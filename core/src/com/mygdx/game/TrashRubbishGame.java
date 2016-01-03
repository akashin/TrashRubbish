package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.screen.MainMenuScreen;

public class TrashRubbishGame extends Game {
	@Override
	public void create() {
        setScreen(new MainMenuScreen(this));
	}
}
