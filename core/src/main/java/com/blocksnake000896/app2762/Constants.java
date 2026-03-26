package com.blocksnake000896.app2762;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Color palette (hex strings for Color.valueOf())
    public static final String COLOR_BG       = "#0A0E1A";
    public static final String COLOR_PRIMARY  = "#00FFB2";  // electric snake green
    public static final String COLOR_ACCENT   = "#FF4B4B";  // brick crimson
    public static final String COLOR_GOLD     = "#FFD700";  // pellet gold
    public static final String COLOR_VIOLET   = "#A259FF";  // shockwave / break FX
    public static final String COLOR_GRID     = "#1B2A4A";  // grid line midnight blue

    // Snake grid
    public static final int   GRID_COLS       = 12;         // cells across play area
    public static final int   GRID_ROWS       = 18;         // cells down play area
    public static final float CELL_SIZE       = 32f;        // world units per cell
    public static final float PLAY_AREA_X     = (WORLD_WIDTH  - GRID_COLS * CELL_SIZE) / 2f;
    public static final float PLAY_AREA_Y     = 80f;        // below HUD band
    public static final float PLAY_AREA_W     = GRID_COLS   * CELL_SIZE;  // 384
    public static final float PLAY_AREA_H     = GRID_ROWS   * CELL_SIZE;  // 576

    // Snake movement
    public static final float INITIAL_MOVE_INTERVAL = 0.18f;   // seconds per step at start
    public static final float MIN_MOVE_INTERVAL     = 0.08f;   // fastest possible speed
    public static final float SPEED_INCREMENT       = 0.003f;  // how much faster per pellet

    // Snake initial state
    public static final int   INITIAL_SNAKE_LENGTH  = 3;
    public static final int   SNAKE_GROW_AMOUNT     = 1;       // segments added per pellet

    // Bricks
    public static final int   BRICK_HP_DEFAULT      = 1;       // hits to destroy normal brick
    public static final int   BRICK_HP_HARD         = 3;       // hits to destroy hard brick
    public static final float BRICK_SPAWN_INTERVAL  = 8f;      // seconds between new brick rows
    public static final int   BRICKS_PER_ROW        = 4;       // bricks spawned per wave

    // Pellet
    public static final float PELLET_SIZE           = CELL_SIZE * 0.8f;

    // Scoring
    public static final int   SCORE_PER_PELLET      = 10;
    public static final int   SCORE_PER_BRICK       = 5;
    public static final int   SCORE_PER_HARD_BRICK  = 15;
    public static final int   SCORE_SEGMENT_BONUS   = 2;       // per snake segment on game over

    // HUD
    public static final float HUD_HEIGHT            = 80f;
    public static final float HUD_FONT_SCALE        = 1f;
    public static final float PAUSE_BTN_SIZE        = 44f;

    // Leaderboard
    public static final int   LEADERBOARD_SIZE      = 10;

    // SharedPreferences keys
    public static final String PREFS_NAME   = "BlockSnakePrefs";
    public static final String PREF_MUSIC   = "musicEnabled";
    public static final String PREF_SFX     = "sfxEnabled";
    public static final String PREF_HIGH_SCORE    = "highScore";
    public static final String PREF_SCORE_PREFIX  = "score_";  // score_0 … score_9
    public static final String PREF_VIBRATION     = "vibrationEnabled";

    // UI button standard sizes
    public static final float BTN_W_MAIN   = 260f;
    public static final float BTN_H_MAIN   = 56f;
    public static final float BTN_W_SEC    = 260f;
    public static final float BTN_H_SEC    = 48f;
    public static final float BTN_W_SMALL  = 180f;
    public static final float BTN_H_SMALL  = 44f;
    public static final float BTN_ROUND    = 56f;

    // Screen asset paths
    public static final String UI_MENU     = "ui/menu_screen.png";
    public static final String UI_GAME     = "ui/game_screen.png";
    public static final String UI_GAMEOVER = "ui/game_over_screen.png";
    public static final String UI_PAUSE    = "ui/pause_screen.png";

    public static final String BTN_RECT_UP   = "ui/buttons/button_rectangle_depth_gradient.png";
    public static final String BTN_RECT_DOWN = "ui/buttons/button_rectangle_depth_flat.png";
    public static final String BTN_ROUND_UP  = "ui/buttons/button_round_depth_gradient.png";
    public static final String BTN_ROUND_DN  = "ui/buttons/button_round_depth_flat.png";
    public static final String BTN_STAR      = "ui/buttons/star.png";
    public static final String BTN_STAR_OUT  = "ui/buttons/star_outline.png";

    // Font paths
    public static final String FONT_TITLE = "fonts/PressStart2P.ttf";
    public static final String FONT_BODY  = "fonts/Kongtext.ttf";

    // Music paths
    public static final String MUSIC_MENU     = "sounds/music/music_menu.ogg";
    public static final String MUSIC_GAMEPLAY = "sounds/music/music_gameplay.ogg";
    public static final String MUSIC_GAMEOVER = "sounds/music/music_game_over.ogg";

    // SFX paths
    public static final String SFX_CLICK      = "sounds/sfx/sfx_button_click.ogg";
    public static final String SFX_BACK       = "sounds/sfx/sfx_button_back.ogg";
    public static final String SFX_TOGGLE     = "sounds/sfx/sfx_toggle.ogg";
    public static final String SFX_COLLECT    = "sounds/sfx/sfx_collect.ogg";
    public static final String SFX_HIT        = "sounds/sfx/sfx_hit.ogg";
    public static final String SFX_GAME_OVER  = "sounds/sfx/sfx_game_over.ogg";
    public static final String SFX_CONFIRM    = "sounds/sfx/sfx_confirm.ogg";

    // Background asset paths
    public static final String BG_MENU_1  = "backgrounds/menu/Blue.png";
    public static final String BG_GAME_1  = "backgrounds/game/1.png";

    // Lifetime stats prefs
    public static final String PREF_GAMES_PLAYED      = "gamesPlayed";
    public static final String PREF_BRICKS_DESTROYED  = "bricksDestroyed";
    public static final String PREF_PELLETS_EATEN     = "pelletsEaten";
    public static final String PREF_LAST_SCORE        = "lastScore";
}
