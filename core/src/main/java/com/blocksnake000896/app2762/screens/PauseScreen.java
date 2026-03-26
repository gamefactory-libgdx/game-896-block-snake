package com.blocksnake000896.app2762.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class PauseScreen extends ScreenAdapter {

    private final MainGame game;
    private final Screen previousScreen;

    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;

    // FIGMA coordinates (libgdxY = WORLD_HEIGHT - topY - height):
    //   PAUSED LABEL  top-Y=240 h=52  → libgdxY = 854-240-52 = 562
    //   RESUME        top-Y=380 h=56  → libgdxY = 854-380-56 = 418
    //   RESTART       top-Y=450 h=48  → libgdxY = 854-450-48 = 356
    //   MAIN MENU     top-Y=516 h=48  → libgdxY = 854-516-48 = 290
    //   STATS         top-Y=580 h=44  → libgdxY = 854-580-44 = 230  (replaces SOUND TOGGLE)

    public PauseScreen(MainGame game, Screen previousScreen) {
        this.game           = game;
        this.previousScreen = previousScreen;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // "PAUSED" label
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PRIMARY));
        Label pausedLabel = new Label("PAUSED", titleStyle);
        float lw = 260f;
        float lh = 52f;
        pausedLabel.setSize(lw, lh);
        pausedLabel.setPosition((Constants.WORLD_WIDTH - lw) / 2f, 562f);
        pausedLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        stage.addActor(pausedLabel);

        // RESUME
        TextButton resumeBtn = UiFactory.makeButton("RESUME", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        resumeBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        resumeBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        resumeBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 418f);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(previousScreen);
            }
        });
        stage.addActor(resumeBtn);

        // RESTART
        TextButton restartBtn = UiFactory.makeButton("RESTART", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        restartBtn.setColor(Color.valueOf(Constants.COLOR_ACCENT));
        restartBtn.getLabel().setColor(Color.WHITE);
        restartBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 356f);
        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new GameScreen(game)); // fresh instance
            }
        });
        stage.addActor(restartBtn);

        // MAIN MENU
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        menuBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 290f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_BACK);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);

        // STATS
        TextButton statsBtn = UiFactory.makeButton("STATS", rectStyle,
                Constants.BTN_W_SMALL, Constants.BTN_H_SMALL);
        statsBtn.setColor(Color.valueOf(Constants.COLOR_GRID));
        statsBtn.getLabel().setColor(Color.WHITE);
        statsBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SMALL) / 2f, 230f);
        statsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new StatsScreen(game));
            }
        });
        stage.addActor(statsBtn);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputMultiplexer(
                stage,
                new InputAdapter() {
                    @Override public boolean keyDown(int keycode) {
                        if (keycode == Input.Keys.BACK) {
                            game.playSound(Constants.SFX_BACK);
                            game.setScreen(previousScreen);
                            return true;
                        }
                        return false;
                    }
                }
        ));
    }

    @Override
    public void show() {
        setupInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.UI_PAUSE, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
