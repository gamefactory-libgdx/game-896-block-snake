package com.blocksnake000896.app2762.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.blocksnake000896.app2762.Constants;
import com.blocksnake000896.app2762.MainGame;
import com.blocksnake000896.app2762.UiFactory;

public class GameScreen extends ScreenAdapter {

    // -------------------------------------------------------------------------
    // Inner type
    // -------------------------------------------------------------------------
    private static class GridPos {
        int col, row;
        GridPos(int c, int r) { col = c; row = r; }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private final MainGame game;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;

    // HUD labels — updated every frame
    private final Label scoreLabel;
    private final Label segmentLabel;

    // Snake: index 0 = head, last index = tail
    private final Array<GridPos> snake = new Array<>();
    private int dirX, dirY;
    private int nextDirX, nextDirY;
    private float moveTimer;
    private float moveInterval;

    // Pellet
    private int pelletCol, pelletRow;

    // Bricks: int[]{col, row, currentHp, maxHp}
    private final Array<int[]> bricks = new Array<>();
    private float brickTimer;

    // State
    private int score;
    private int highScore;
    private boolean gameOver;

    // Lifetime stats (accumulated this session, merged on game-over)
    private int sessionBricksDestroyed;
    private int sessionPelletsEaten;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public GameScreen(MainGame game) {
        this.game = game;

        camera        = new OrthographicCamera();
        viewport      = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage         = new Stage(viewport, game.batch);
        shapeRenderer = new ShapeRenderer();

        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        highScore = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);

        // Build HUD actors
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore,
                Color.valueOf(Constants.COLOR_PRIMARY));
        Label.LabelStyle segStyle = new Label.LabelStyle(game.fontSmall,
                Color.valueOf(Constants.COLOR_PRIMARY));

        scoreLabel   = new Label("0",  scoreStyle);
        segmentLabel = new Label("x3", segStyle);

        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);
        TextButton pauseBtn = UiFactory.makeButton("II", roundStyle,
                Constants.PAUSE_BTN_SIZE + 20, Constants.PAUSE_BTN_SIZE);
        pauseBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        pauseBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.playSound(Constants.SFX_CLICK);
                game.setScreen(new PauseScreen(game, GameScreen.this));
            }
        });

        // HUD table — top strip
        Table hud = new Table();
        hud.setFillParent(true);
        hud.top().pad(20f, 16f, 0f, 16f);
        hud.add(segmentLabel).left().width(140f).height(36f);
        hud.add(scoreLabel).expandX().center().width(200f).height(36f);
        hud.add(pauseBtn).right().size(64f, 44f);
        stage.addActor(hud);

        initGame();
        game.playMusic(Constants.MUSIC_GAMEPLAY);
        setupInput();
    }

    // -------------------------------------------------------------------------
    // Init / reset
    // -------------------------------------------------------------------------
    private void initGame() {
        score                 = 0;
        gameOver              = false;
        moveInterval          = Constants.INITIAL_MOVE_INTERVAL;
        moveTimer             = moveInterval;
        brickTimer            = 0f;
        sessionBricksDestroyed = 0;
        sessionPelletsEaten   = 0;

        snake.clear();
        bricks.clear();

        int sc = Constants.GRID_COLS / 2;
        int sr = Constants.GRID_ROWS / 2;
        snake.add(new GridPos(sc,     sr));
        snake.add(new GridPos(sc - 1, sr));
        snake.add(new GridPos(sc - 2, sr));

        dirX = 1; dirY = 0;
        nextDirX = 1; nextDirY = 0;

        spawnPellet();
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    private void setupInput() {
        GestureDetector gd = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean fling(float velX, float velY, int button) {
                if (gameOver) return false;
                if (Math.abs(velX) > Math.abs(velY)) {
                    // Horizontal swipe
                    int nd = velX > 0 ? 1 : -1;
                    if (dirX != 0 && nd == -dirX) return false; // 180-degree blocked
                    nextDirX = nd; nextDirY = 0;
                } else {
                    // Vertical swipe (screen-Y inverted vs grid-Y)
                    int nd = velY < 0 ? 1 : -1;
                    if (dirY != 0 && nd == -dirY) return false; // 180-degree blocked
                    nextDirX = 0; nextDirY = nd;
                }
                return true;
            }
        });

        InputAdapter keys = new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:    if (dirY != -1) { nextDirX=0;  nextDirY=1;  } break;
                    case Input.Keys.DOWN:  if (dirY !=  1) { nextDirX=0;  nextDirY=-1; } break;
                    case Input.Keys.LEFT:  if (dirX !=  1) { nextDirX=-1; nextDirY=0;  } break;
                    case Input.Keys.RIGHT: if (dirX != -1) { nextDirX=1;  nextDirY=0;  } break;
                    case Input.Keys.BACK:
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                }
                return false;
            }
        };

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, gd, keys));
    }

    @Override
    public void show() {
        setupInput();
    }

    // -------------------------------------------------------------------------
    // Game logic
    // -------------------------------------------------------------------------
    private void update(float delta) {
        // Brick spawner
        brickTimer += delta;
        if (brickTimer >= Constants.BRICK_SPAWN_INTERVAL) {
            brickTimer = 0f;
            spawnBrickRow();
        }

        // Snake movement
        moveTimer -= delta;
        if (moveTimer <= 0f) {
            moveTimer = moveInterval;
            moveSnake();
        }

        // Keep HUD current
        scoreLabel.setText(String.valueOf(score));
        segmentLabel.setText("x" + snake.size);
    }

    private void moveSnake() {
        // Apply buffered direction, prevent 180-degree reverse
        boolean is180 = (nextDirX != 0 && nextDirX == -dirX)
                     || (nextDirY != 0 && nextDirY == -dirY);
        if (!is180) { dirX = nextDirX; dirY = nextDirY; }

        int newCol = snake.get(0).col + dirX;
        int newRow = snake.get(0).row + dirY;

        // Wall collision
        if (newCol < 0 || newCol >= Constants.GRID_COLS
                || newRow < 0 || newRow >= Constants.GRID_ROWS) {
            triggerGameOver();
            return;
        }

        // Brick collision — attack brick, snake doesn't advance this tick
        int[] brick = getBrickAt(newCol, newRow);
        if (brick != null) {
            brick[2]--;
            game.playSound(Constants.SFX_HIT);
            if (brick[2] <= 0) {
                bricks.removeValue(brick, true);
                score += (brick[3] == Constants.BRICK_HP_HARD)
                        ? Constants.SCORE_PER_HARD_BRICK
                        : Constants.SCORE_PER_BRICK;
                sessionBricksDestroyed++;
            }
            return; // snake stays — was attacking
        }

        // Self-collision — check body except tail (tail vacates this tick)
        for (int i = 0; i < snake.size - 1; i++) {
            if (snake.get(i).col == newCol && snake.get(i).row == newRow) {
                triggerGameOver();
                return;
            }
        }

        // Pellet check
        boolean eatPellet = (newCol == pelletCol && newRow == pelletRow);

        // Advance snake
        snake.insert(0, new GridPos(newCol, newRow));
        if (eatPellet) {
            score += Constants.SCORE_PER_PELLET;
            moveInterval = Math.max(Constants.MIN_MOVE_INTERVAL,
                    moveInterval - Constants.SPEED_INCREMENT);
            game.playSound(Constants.SFX_COLLECT);
            sessionPelletsEaten++;
            spawnPellet();
            // tail stays — snake grows
        } else {
            snake.removeIndex(snake.size - 1);
        }
    }

    private void triggerGameOver() {
        if (gameOver) return;
        gameOver = true;

        // Persist lifetime stats (high score + leaderboard handled by GameOverScreen)
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        prefs.putInteger(Constants.PREF_GAMES_PLAYED,
                prefs.getInteger(Constants.PREF_GAMES_PLAYED, 0) + 1);
        prefs.putInteger(Constants.PREF_BRICKS_DESTROYED,
                prefs.getInteger(Constants.PREF_BRICKS_DESTROYED, 0) + sessionBricksDestroyed);
        prefs.putInteger(Constants.PREF_PELLETS_EATEN,
                prefs.getInteger(Constants.PREF_PELLETS_EATEN, 0) + sessionPelletsEaten);
        prefs.putInteger(Constants.PREF_LAST_SCORE, score);
        prefs.flush();

        // GameOverScreen handles music, sfx, high score update, and leaderboard insert
        game.setScreen(new GameOverScreen(game, score, snake.size));
    }

    private void spawnPellet() {
        for (int attempt = 0; attempt < 200; attempt++) {
            int c = MathUtils.random(Constants.GRID_COLS - 1);
            int r = MathUtils.random(Constants.GRID_ROWS - 1);
            if (!isSnakeAt(c, r) && getBrickAt(c, r) == null) {
                pelletCol = c;
                pelletRow = r;
                return;
            }
        }
        // Grid too full — game over
        triggerGameOver();
    }

    private void spawnBrickRow() {
        int topRow = Constants.GRID_ROWS - 1;

        // Collect valid columns (top row only)
        Array<Integer> valid = new Array<>();
        for (int c = 0; c < Constants.GRID_COLS; c++) {
            if (!isSnakeAt(c, topRow) && getBrickAt(c, topRow) == null
                    && !(c == pelletCol && topRow == pelletRow)) {
                valid.add(c);
            }
        }

        // Fisher-Yates shuffle
        for (int i = valid.size - 1; i > 0; i--) {
            int j = MathUtils.random(i);
            int tmp = valid.get(i);
            valid.set(i, valid.get(j));
            valid.set(j, tmp);
        }

        int count = Math.min(Constants.BRICKS_PER_ROW, valid.size);
        for (int i = 0; i < count; i++) {
            int c = valid.get(i);
            int hp = (MathUtils.random(4) == 0) ? Constants.BRICK_HP_HARD : Constants.BRICK_HP_DEFAULT;
            bricks.add(new int[]{c, topRow, hp, hp});
        }
    }

    private boolean isSnakeAt(int col, int row) {
        for (int i = 0; i < snake.size; i++) {
            GridPos p = snake.get(i);
            if (p.col == col && p.row == row) return true;
        }
        return false;
    }

    private int[] getBrickAt(int col, int row) {
        for (int i = 0; i < bricks.size; i++) {
            int[] b = bricks.get(i);
            if (b[0] == col && b[1] == row) return b;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Render
    // -------------------------------------------------------------------------
    @Override
    public void render(float delta) {
        if (!gameOver) update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. Background
        game.batch.setColor(Color.WHITE);
        game.batch.begin();
        game.batch.draw(game.manager.get(Constants.UI_GAME, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // 2. Game elements via ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Grid dots
        shapeRenderer.setColor(0.11f, 0.17f, 0.29f, 0.85f);
        for (int c = 0; c < Constants.GRID_COLS; c++) {
            for (int r = 0; r < Constants.GRID_ROWS; r++) {
                float gx = Constants.PLAY_AREA_X + c * Constants.CELL_SIZE + Constants.CELL_SIZE * 0.5f;
                float gy = Constants.PLAY_AREA_Y + r * Constants.CELL_SIZE + Constants.CELL_SIZE * 0.5f;
                shapeRenderer.circle(gx, gy, 1.5f, 6);
            }
        }

        // Bricks
        for (int i = 0; i < bricks.size; i++) {
            int[] b = bricks.get(i);
            float bx = Constants.PLAY_AREA_X + b[0] * Constants.CELL_SIZE + 2f;
            float by = Constants.PLAY_AREA_Y + b[1] * Constants.CELL_SIZE + 2f;
            float bw = Constants.CELL_SIZE - 4f;
            float bh = Constants.CELL_SIZE - 4f;
            if (b[3] == Constants.BRICK_HP_HARD) {
                // Lighten as HP drops so player can see damage
                float t = (float) b[2] / b[3];
                shapeRenderer.setColor(0.7f * t + 0.3f, 0.1f, 0.1f, 1f);
            } else {
                shapeRenderer.setColor(Color.valueOf(Constants.COLOR_ACCENT));
            }
            shapeRenderer.rect(bx, by, bw, bh);
        }

        // Pellet (gold circle)
        shapeRenderer.setColor(Color.valueOf(Constants.COLOR_GOLD));
        float px = Constants.PLAY_AREA_X + pelletCol * Constants.CELL_SIZE + Constants.CELL_SIZE * 0.5f;
        float py = Constants.PLAY_AREA_Y + pelletRow * Constants.CELL_SIZE + Constants.CELL_SIZE * 0.5f;
        shapeRenderer.circle(px, py, Constants.CELL_SIZE * 0.32f, 12);

        // Snake body segments
        Color snakeGreen = Color.valueOf(Constants.COLOR_PRIMARY);
        shapeRenderer.setColor(snakeGreen);
        for (int i = 1; i < snake.size; i++) {
            GridPos seg = snake.get(i);
            shapeRenderer.rect(
                    Constants.PLAY_AREA_X + seg.col * Constants.CELL_SIZE + 2f,
                    Constants.PLAY_AREA_Y + seg.row * Constants.CELL_SIZE + 2f,
                    Constants.CELL_SIZE - 4f,
                    Constants.CELL_SIZE - 4f);
        }

        // Snake head — white for clear visibility
        if (!snake.isEmpty()) {
            GridPos head = snake.get(0);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(
                    Constants.PLAY_AREA_X + head.col * Constants.CELL_SIZE + 1f,
                    Constants.PLAY_AREA_Y + head.row * Constants.CELL_SIZE + 1f,
                    Constants.CELL_SIZE - 2f,
                    Constants.CELL_SIZE - 2f);
        }

        shapeRenderer.end();

        // 3. Brick HP overlays (only for hard bricks with HP > 1)
        game.batch.begin();
        for (int i = 0; i < bricks.size; i++) {
            int[] b = bricks.get(i);
            if (b[2] > 1) {
                float tx = Constants.PLAY_AREA_X + b[0] * Constants.CELL_SIZE + 9f;
                float ty = Constants.PLAY_AREA_Y + b[1] * Constants.CELL_SIZE + Constants.CELL_SIZE * 0.65f;
                game.fontSmall.draw(game.batch, String.valueOf(b[2]), tx, ty);
            }
        }
        game.batch.end();

        // 4. HUD stage — always act so pause button fires
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
