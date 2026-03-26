package com.blocksnake000896.app2762.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class StatsScreen extends ScreenAdapter {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;

    public StatsScreen(MainGame game) {
        this.game = game;

        camera        = new OrthographicCamera();
        viewport      = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage         = new Stage(viewport, game.batch);
        shapeRenderer = new ShapeRenderer();

        buildUI();
        setupInput();
    }

    private void buildUI() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int highScore       = prefs.getInteger(Constants.PREF_HIGH_SCORE,       0);
        int gamesPlayed     = prefs.getInteger(Constants.PREF_GAMES_PLAYED,     0);
        int pelletsEaten    = prefs.getInteger(Constants.PREF_PELLETS_EATEN,    0);
        int bricksDestroyed = prefs.getInteger(Constants.PREF_BRICKS_DESTROYED, 0);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PRIMARY));
        Label titleLabel = new Label("STATS", titleStyle);
        titleLabel.setSize(320f, 52f);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 730f);
        titleLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        stage.addActor(titleLabel);

        // Stat rows: label + value pairs
        Label.LabelStyle keyStyle = new Label.LabelStyle(game.fontBody, Color.valueOf(Constants.COLOR_PRIMARY));
        Label.LabelStyle valStyle = new Label.LabelStyle(game.fontScore, Color.WHITE);

        addStatRow("HIGH SCORE",       String.valueOf(highScore),       keyStyle, valStyle, 600f);
        addStatRow("GAMES PLAYED",     String.valueOf(gamesPlayed),     keyStyle, valStyle, 490f);
        addStatRow("PELLETS EATEN",    String.valueOf(pelletsEaten),    keyStyle, valStyle, 380f);
        addStatRow("BRICKS SMASHED",   String.valueOf(bricksDestroyed), keyStyle, valStyle, 270f);

        // MAIN MENU button
        TextButton backBtn = UiFactory.makeButton("MAIN MENU", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        backBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        backBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        backBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 100f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_BACK);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
    }

    private void addStatRow(String key, String value,
                             Label.LabelStyle keyStyle, Label.LabelStyle valStyle,
                             float topY) {
        // Key label
        Label keyLbl = new Label(key, keyStyle);
        keyLbl.setSize(430f, 28f);
        keyLbl.setPosition((Constants.WORLD_WIDTH - 430f) / 2f, topY + 36f);
        keyLbl.setAlignment(com.badlogic.gdx.utils.Align.center);
        stage.addActor(keyLbl);

        // Value label
        Label valLbl = new Label(value, valStyle);
        valLbl.setSize(430f, 44f);
        valLbl.setPosition((Constants.WORLD_WIDTH - 430f) / 2f, topY);
        valLbl.setAlignment(com.badlogic.gdx.utils.Align.center);
        stage.addActor(valLbl);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputMultiplexer(
                stage,
                new InputAdapter() {
                    @Override public boolean keyDown(int keycode) {
                        if (keycode == Input.Keys.BACK) {
                            game.playSound(Constants.SFX_BACK);
                            game.setScreen(new MainMenuScreen(game));
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
        Gdx.gl.glClearColor(0.04f, 0.06f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Background
        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.BG_MENU_1, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // Dark panel + border
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Semi-transparent card
        shapeRenderer.setColor(0f, 0f, 0f, 0.72f);
        shapeRenderer.rect(24f, 82f, Constants.WORLD_WIDTH - 48f, 720f);

        // Divider lines between stat rows
        shapeRenderer.setColor(0.11f, 0.17f, 0.29f, 1f);
        shapeRenderer.rect(40f, 452f, Constants.WORLD_WIDTH - 80f, 2f);
        shapeRenderer.rect(40f, 341f, Constants.WORLD_WIDTH - 80f, 2f);
        shapeRenderer.rect(40f, 230f, Constants.WORLD_WIDTH - 80f, 2f);

        // Border lines
        shapeRenderer.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        shapeRenderer.rect(24f, 800f, Constants.WORLD_WIDTH - 48f, 3f);
        shapeRenderer.rect(24f, 82f,  Constants.WORLD_WIDTH - 48f, 3f);

        shapeRenderer.end();

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
        shapeRenderer.dispose();
    }
}
