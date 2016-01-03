package com.mygdx.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.game.TrashRubbishGame;

/**
 * Created by acid on 03/01/16.
 */
public class MainMenuScreen extends BasicScreen {
    private Table table;
    private TextButton startGameButton;
    private TextButton exitButton;

    public MainMenuScreen(final TrashRubbishGame game_) {
        super(game_);
    }

    @Override
    public void renderScreen() {
        stage.draw();
    }

    @Override
    public void updateScreen(float delta) {
        stage.act(delta);
    }

    @Override
    public void show() {
        super.show();

        table = new Table();
        table.setFillParent(true);

        startGameButton = new TextButton("Start Game", VisUI.getSkin());
        table.add(startGameButton).width(100);
        table.row();
        exitButton = new TextButton("Exit", VisUI.getSkin());
        table.add(exitButton).width(100);

        stage.addActor(table);
    }
}
