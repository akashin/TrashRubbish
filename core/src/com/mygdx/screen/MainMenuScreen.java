package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.TrashRubbishGame;
import com.mygdx.logic.Level;

public class MainMenuScreen extends BasicScreen {
    private Table table;
    private TextButton playButton;
    private TextButton editorButton;
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

        playButton = new TextButton("Play", game.getSkin());
        table.add(playButton).width(100).row();

        editorButton = new TextButton("Editor", game.getSkin());
        table.add(editorButton).width(100).row();

        exitButton = new TextButton("Exit", game.getSkin());
        table.add(exitButton).width(100).row();

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "default", game.getLastLevelIndex()));
            }
        });

        editorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EditorScreen(game, new Level(6, 6, "untitled")));
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
