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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class HowToPlayScreen extends ScreenAdapter {

    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;

    public HowToPlayScreen(MainGame game) {
        this.game = game;

        camera        = new OrthographicCamera();
        viewport      = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage         = new Stage(viewport, game.batch);
        shapeRenderer = new ShapeRenderer();

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PRIMARY));
        Label titleLabel = new Label("HOW TO PLAY", titleStyle);
        titleLabel.setSize(440f, 52f);
        titleLabel.setPosition((Constants.WORLD_WIDTH - 440f) / 2f, 760f);
        titleLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        stage.addActor(titleLabel);

        // Instructions text rows — using fontBody
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        Label.LabelStyle accentStyle = new Label.LabelStyle(game.fontBody, Color.valueOf(Constants.COLOR_PRIMARY));
        Label.LabelStyle redStyle    = new Label.LabelStyle(game.fontBody, Color.valueOf(Constants.COLOR_ACCENT));

        // Row data: {yPosition, text, style}
        Object[][] rows = {
            { 680f, "SWIPE to steer your snake", accentStyle },
            { 620f, "Eat GOLD pellets to grow",  bodyStyle   },
            { 560f, "Eating = more SPEED!",       bodyStyle   },
            { 500f, "RED bricks block your path", redStyle    },
            { 440f, "Move INTO bricks to smash",  bodyStyle   },
            { 380f, "Hard bricks need 3 hits",    bodyStyle   },
            { 320f, "Hit a WALL = game over",      redStyle    },
            { 260f, "Hit YOURSELF = game over",    redStyle    },
        };

        for (Object[] row : rows) {
            float y    = (Float) row[0];
            String txt = (String) row[1];
            Label.LabelStyle ls = (Label.LabelStyle) row[2];
            Label lbl = new Label(txt, ls);
            lbl.setSize(440f, 36f);
            lbl.setPosition((Constants.WORLD_WIDTH - 440f) / 2f, y);
            lbl.setAlignment(com.badlogic.gdx.utils.Align.center);
            stage.addActor(lbl);
        }

        // BACK / MAIN MENU button
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

        // Background — use menu background (no specific ui PNG for this screen)
        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.BG_MENU_1, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // Draw instruction panels (card backgrounds)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Semi-transparent dark panel behind text area
        shapeRenderer.setColor(0f, 0f, 0f, 0.65f);
        shapeRenderer.rect(20f, 90f, Constants.WORLD_WIDTH - 40f, 720f);

        // Accent border top/bottom lines
        shapeRenderer.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        shapeRenderer.rect(20f, 808f, Constants.WORLD_WIDTH - 40f, 3f);  // top line
        shapeRenderer.rect(20f, 90f,  Constants.WORLD_WIDTH - 40f, 3f);  // bottom line

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
