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

public class SettingsScreen implements Screen {

    private final MainGame game;
    private Stage stage;
    private StretchViewport viewport;

    private boolean musicOn;
    private boolean sfxOn;

    private TextButton musicBtn;
    private TextButton sfxBtn;

    private static final Color COLOR_ON  = Color.valueOf("#00FFB2");
    private static final Color COLOR_OFF = new Color(0.35f, 0.35f, 0.35f, 1f);
    private static final Color COLOR_TEXT_DARK = Color.valueOf("#080812");

    public SettingsScreen(MainGame game) {
        this.game = game;

        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicOn = prefs.getBoolean(Constants.PREF_MUSIC, true);
        sfxOn   = prefs.getBoolean(Constants.PREF_SFX,   true);

        game.musicEnabled = musicOn;
        game.sfxEnabled   = sfxOn;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // ── TITLE ──
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PRIMARY));
        Label title = new Label("SETTINGS", titleStyle);
        title.setSize(340f, 64f);
        title.setPosition((Constants.WORLD_WIDTH - 340f) / 2f, 700f);
        title.setAlignment(Align.center);
        stage.addActor(title);

        // ── MUSIC label ──
        Label.LabelStyle labelStyle = new Label.LabelStyle(game.fontBody, Color.WHITE);
        Label musicLabel = new Label("MUSIC", labelStyle);
        musicLabel.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 560f);
        stage.addActor(musicLabel);

        // ── MUSIC toggle button ──
        musicBtn = UiFactory.makeButton(musicOn ? "ON" : "OFF", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        musicBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 490f);
        musicBtn.setColor(musicOn ? COLOR_ON : COLOR_OFF);
        musicBtn.getLabel().setColor(COLOR_TEXT_DARK);
        musicBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                musicOn = !musicOn;
                game.musicEnabled = musicOn;
                prefs.putBoolean(Constants.PREF_MUSIC, musicOn);
                prefs.flush();
                if (game.currentMusic != null) {
                    if (musicOn) game.currentMusic.play();
                    else         game.currentMusic.pause();
                }
                musicBtn.setText(musicOn ? "ON" : "OFF");
                musicBtn.setColor(musicOn ? COLOR_ON : COLOR_OFF);
                game.playSound(Constants.SFX_TOGGLE, 0.5f);
            }
        });
        stage.addActor(musicBtn);

        // ── SFX label ──
        Label sfxLabel = new Label("SOUND FX", labelStyle);
        sfxLabel.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 400f);
        stage.addActor(sfxLabel);

        // ── SFX toggle button ──
        sfxBtn = UiFactory.makeButton(sfxOn ? "ON" : "OFF", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        sfxBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 330f);
        sfxBtn.setColor(sfxOn ? COLOR_ON : COLOR_OFF);
        sfxBtn.getLabel().setColor(COLOR_TEXT_DARK);
        sfxBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
                sfxOn = !sfxOn;
                game.sfxEnabled = sfxOn;
                prefs.putBoolean(Constants.PREF_SFX, sfxOn);
                prefs.flush();
                sfxBtn.setText(sfxOn ? "ON" : "OFF");
                sfxBtn.setColor(sfxOn ? COLOR_ON : COLOR_OFF);
                game.playSound(Constants.SFX_TOGGLE, 0.5f);
            }
        });
        stage.addActor(sfxBtn);

        // ── BACK button ──
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        backBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 160f);
        backBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        backBtn.getLabel().setColor(COLOR_TEXT_DARK);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.playSound(Constants.SFX_BACK);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
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
        Texture bg = game.manager.get(Constants.BG_MENU_1, Texture.class);
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
