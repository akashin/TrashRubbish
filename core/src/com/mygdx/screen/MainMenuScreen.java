package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.game.TrashRubbishGame;

public class MainMenuScreen extends BasicScreen {
    private Table table;
    private TextButton startGameButton;
    private TextButton exitButton;

    public MainMenuScreen(TrashRubbishGame game) {
        super(game);
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
        stage.addActor(table);

        startGameButton = new TextButton("Start Game", VisUI.getSkin());
        table.add(startGameButton).width(100);

        table.row();
        exitButton = new TextButton("Exit", VisUI.getSkin());
        table.add(exitButton).width(100);

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}
