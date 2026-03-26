package com.blocksnake000896.app2762.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private Stage stage;
    private StretchViewport viewport;

    public MainMenuScreen(MainGame game) {
        this.game = game;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        registerInput();

        game.playMusic(Constants.MUSIC_MENU);
    }

    private void buildUI() {
        // Button styles
        TextButton.TextButtonStyle rectMain = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle rectSec  = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // ── TITLE LABEL ── libgdxY = 854-120-64 = 670
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PRIMARY));
        Label titleLabel = new Label("BLOCK SNAKE", titleStyle);
        titleLabel.setSize(340f, 64f);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 340f) / 2f, 670f);
        titleLabel.setAlignment(Align.center);
        stage.addActor(titleLabel);

        // ── PLAY button ── libgdxY = 854-380-56 = 418
        TextButton playBtn = UiFactory.makeButton("PLAY", rectMain, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        playBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 418f);
        playBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        playBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(playBtn);

        // ── HOW TO PLAY button ── libgdxY = 854-454-48 = 352
        TextButton howBtn = UiFactory.makeButton("HOW TO PLAY", rectSec, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        howBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 352f);
        howBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        howBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        howBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new HowToPlayScreen(game));
            }
        });
        stage.addActor(howBtn);

        // ── LEADERBOARD button ── libgdxY = 854-516-48 = 290
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectSec, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        lbBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 290f);
        lbBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        lbBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(lbBtn);

        // ── SETTINGS button ── libgdxY = 854-578-48 = 228
        TextButton settBtn = UiFactory.makeButton("SETTINGS", rectSec, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        settBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 228f);
        settBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        settBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        settBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new SettingsScreen(game));
            }
        });
        stage.addActor(settBtn);

        // ── HIGH SCORE label ── libgdxY = 854-700-36 = 118
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int highScore = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontSmall, Color.valueOf(Constants.COLOR_GOLD));
        Label scoreLabel = new Label("BEST: " + highScore, scoreStyle);
        scoreLabel.setSize(300f, 36f);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 118f);
        scoreLabel.setAlignment(Align.center);
        stage.addActor(scoreLabel);
    }

    private void registerInput() {
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void show() {
        registerInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        Texture bg = game.manager.get(Constants.UI_MENU, Texture.class);
        game.batch.draw(bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
