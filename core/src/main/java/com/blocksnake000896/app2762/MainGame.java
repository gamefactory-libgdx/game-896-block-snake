package com.blocksnake000896.app2762;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.Preferences;
import com.blocksnake000896.app2762.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch  batch;
    public AssetManager manager;

    // Shared fonts — generated once, reused across all screens
    public BitmapFont fontTitle;   // PressStart2P — large titles / scores
    public BitmapFont fontBody;    // Kongtext — buttons, labels
    public BitmapFont fontSmall;   // Kongtext small — secondary labels
    public BitmapFont fontScore;   // PressStart2P large — in-game score display

    // Audio state
    public boolean musicEnabled    = true;
    public boolean sfxEnabled      = true;
    public boolean vibrationEnabled = true;
    public Music   currentMusic    = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        // Register FreeType loaders
        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Load saved preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicEnabled     = prefs.getBoolean(Constants.PREF_MUSIC,     true);
        sfxEnabled       = prefs.getBoolean(Constants.PREF_SFX,       true);
        vibrationEnabled = prefs.getBoolean(Constants.PREF_VIBRATION, true);

        // Generate fonts using FreeTypeFontGenerator (synchronous — before any screen loads)
        generateFonts();

        // Load core assets (buttons, UI images, sounds) used by all screens
        loadCoreAssets();
        manager.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    private void generateFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal(Constants.FONT_TITLE));
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(
                Gdx.files.internal(Constants.FONT_BODY));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.borderColor = new Color(0f, 0f, 0f, 0.85f);

        // fontBody — Kongtext 20px
        p.size        = 20;
        p.borderWidth = 2;
        fontBody = bodyGen.generateFont(p);

        // fontSmall — Kongtext 14px
        p.size        = 14;
        p.borderWidth = 1;
        fontSmall = bodyGen.generateFont(p);

        // fontTitle — PressStart2P 36px
        p.size        = 36;
        p.borderWidth = 3;
        fontTitle = titleGen.generateFont(p);

        // fontScore — PressStart2P 28px (HUD score)
        p.size        = 28;
        p.borderWidth = 3;
        fontScore = titleGen.generateFont(p);

        bodyGen.dispose();
        titleGen.dispose();
    }

    private void loadCoreAssets() {
        // Button sprites
        manager.load(Constants.BTN_RECT_UP,   Texture.class);
        manager.load(Constants.BTN_RECT_DOWN, Texture.class);
        manager.load(Constants.BTN_ROUND_UP,  Texture.class);
        manager.load(Constants.BTN_ROUND_DN,  Texture.class);
        manager.load(Constants.BTN_STAR,      Texture.class);
        manager.load(Constants.BTN_STAR_OUT,  Texture.class);

        // Generated UI backgrounds
        manager.load(Constants.UI_MENU,     Texture.class);
        manager.load(Constants.UI_GAME,     Texture.class);
        manager.load(Constants.UI_GAMEOVER, Texture.class);
        manager.load(Constants.UI_PAUSE,    Texture.class);

        // Fallback backgrounds
        manager.load(Constants.BG_MENU_1, Texture.class);
        manager.load(Constants.BG_GAME_1, Texture.class);

        // Music
        manager.load(Constants.MUSIC_MENU,     Music.class);
        manager.load(Constants.MUSIC_GAMEPLAY, Music.class);
        manager.load(Constants.MUSIC_GAMEOVER, Music.class);

        // SFX
        manager.load(Constants.SFX_CLICK,     Sound.class);
        manager.load(Constants.SFX_BACK,      Sound.class);
        manager.load(Constants.SFX_TOGGLE,    Sound.class);
        manager.load(Constants.SFX_COLLECT,   Sound.class);
        manager.load(Constants.SFX_HIT,       Sound.class);
        manager.load(Constants.SFX_GAME_OVER, Sound.class);
        manager.load(Constants.SFX_CONFIRM,   Sound.class);
    }

    // -------------------------------------------------------------------------
    // Music helpers
    // -------------------------------------------------------------------------

    /** Start a looping music track. No-ops if the same track is already playing. */
    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play a music track exactly once (game over jingle). */
    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play an SFX sound if sfx is enabled. */
    public void playSound(String path) {
        if (sfxEnabled) manager.get(path, Sound.class).play(1.0f);
    }

    /** Play an SFX sound at given volume if sfx is enabled. */
    public void playSound(String path, float volume) {
        if (sfxEnabled) manager.get(path, Sound.class).play(volume);
    }

    // -------------------------------------------------------------------------

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        if (fontBody  != null) fontBody.dispose();
        if (fontSmall != null) fontSmall.dispose();
        if (fontTitle != null) fontTitle.dispose();
        if (fontScore != null) fontScore.dispose();
    }
}
