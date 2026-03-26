package com.blocksnake000896.app2762.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final int score;
    private final int extra;   // e.g. snake length bonus or any extra value

    private Stage stage;
    private StretchViewport viewport;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game  = game;
        this.score = score;
        this.extra = extra;

        // Save score and update personal best
        LeaderboardScreen.addScore(score);
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int prev = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > prev) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        buildUI(prefs.getInteger(Constants.PREF_HIGH_SCORE, score));
        registerInput();

        game.playMusicOnce(Constants.MUSIC_GAMEOVER);
        game.playSound(Constants.SFX_GAME_OVER);
    }

    private void buildUI(int bestScore) {
        TextButton.TextButtonStyle rectMain = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // ── GAME OVER label ── libgdxY = 854-160-60 = 634
        Label.LabelStyle accentStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_ACCENT));
        Label gameOverLabel = new Label("GAME OVER", accentStyle);
        gameOverLabel.setSize(340f, 60f);
        gameOverLabel.setPosition((Constants.WORLD_WIDTH - 340f) / 2f, 634f);
        gameOverLabel.setAlignment(Align.center);
        stage.addActor(gameOverLabel);

        // ── SCORE VALUE label ── libgdxY = 854-310-48 = 496
        Label.LabelStyle goldStyle = new Label.LabelStyle(game.fontScore, Color.valueOf(Constants.COLOR_GOLD));
        Label scoreLabel = new Label("SCORE  " + score, goldStyle);
        scoreLabel.setSize(280f, 48f);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - 280f) / 2f, 496f);
        scoreLabel.setAlignment(Align.center);
        stage.addActor(scoreLabel);

        // ── BEST label ── libgdxY = 854-366-36 = 452
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, Color.valueOf(Constants.COLOR_PRIMARY));
        Label bestLabel = new Label("BEST: " + bestScore, smallStyle);
        bestLabel.setSize(220f, 36f);
        bestLabel.setPosition((Constants.WORLD_WIDTH - 220f) / 2f, 452f);
        bestLabel.setAlignment(Align.center);
        stage.addActor(bestLabel);

        // ── PLAY AGAIN button ── libgdxY = 854-530-56 = 268
        TextButton retryBtn = UiFactory.makeButton("PLAY AGAIN", rectMain, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        retryBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 268f);
        retryBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        retryBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(retryBtn);

        // ── MAIN MENU button ── libgdxY = 854-604-48 = 202
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectMain, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 202f);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_ACCENT));
        menuBtn.getLabel().setColor(Color.WHITE);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_BACK);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
    }

    private void registerInput() {
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.playSound(Constants.SFX_BACK);
                    game.setScreen(new MainMenuScreen(game));
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

        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        Texture bg = game.manager.get(Constants.UI_GAMEOVER, Texture.class);
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
