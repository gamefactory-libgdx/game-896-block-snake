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

public class LeaderboardScreen implements Screen {

    private final MainGame game;
    private Stage stage;
    private StretchViewport viewport;

    // ── Static helper: insert score into top-10 stored in SharedPreferences ──
    public static void addScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int[] scores = loadScores(prefs);

        // Insert sort
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            if (score > scores[i]) {
                // Shift lower entries down
                for (int j = Constants.LEADERBOARD_SIZE - 1; j > i; j--) {
                    scores[j] = scores[j - 1];
                }
                scores[i] = score;
                break;
            }
        }

        // Persist
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            prefs.putInteger(Constants.PREF_SCORE_PREFIX + i, scores[i]);
        }
        prefs.flush();
    }

    private static int[] loadScores(Preferences prefs) {
        int[] scores = new int[Constants.LEADERBOARD_SIZE];
        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            scores[i] = prefs.getInteger(Constants.PREF_SCORE_PREFIX + i, 0);
        }
        return scores;
    }

    // ─────────────────────────────────────────────────────────────────────────

    public LeaderboardScreen(MainGame game) {
        this.game = game;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        registerInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // ── TITLE ──
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PRIMARY));
        Label title = new Label("TOP  SCORES", titleStyle);
        title.setSize(380f, 64f);
        title.setPosition((Constants.WORLD_WIDTH - 380f) / 2f, 740f);
        title.setAlignment(Align.center);
        stage.addActor(title);

        // ── Score entries ──
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int[] scores = loadScores(prefs);

        float startY   = 650f;
        float rowH     = 52f;
        float gap      = 4f;

        Label.LabelStyle goldStyle   = new Label.LabelStyle(game.fontSmall, Color.valueOf(Constants.COLOR_GOLD));
        Label.LabelStyle whiteStyle  = new Label.LabelStyle(game.fontSmall, Color.WHITE);

        for (int i = 0; i < Constants.LEADERBOARD_SIZE; i++) {
            float y = startY - i * (rowH + gap);

            // Rank
            Label rankLabel = new Label((i + 1) + ".", i == 0 ? goldStyle : whiteStyle);
            rankLabel.setPosition(80f, y);
            rankLabel.setAlignment(Align.left);
            stage.addActor(rankLabel);

            // Score value
            String scoreText = scores[i] > 0 ? String.valueOf(scores[i]) : "---";
            Label scoreLabel = new Label(scoreText, i == 0 ? goldStyle : whiteStyle);
            scoreLabel.setSize(200f, rowH);
            scoreLabel.setPosition(Constants.WORLD_WIDTH - 280f, y);
            scoreLabel.setAlignment(Align.right);
            stage.addActor(scoreLabel);
        }

        // ── MAIN MENU button ──
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyle, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 60f);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        menuBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
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
