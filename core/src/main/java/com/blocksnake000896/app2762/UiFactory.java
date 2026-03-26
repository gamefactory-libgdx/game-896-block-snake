package com.blocksnake000896.app2762;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UiFactory {

    /** Rectangle button style — standard menu / action buttons. */
    public static TextButton.TextButtonStyle makeRectStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font = font;
        s.up   = new TextureRegionDrawable(new TextureRegion(
                mgr.get(Constants.BTN_RECT_UP,   Texture.class)));
        s.down = new TextureRegionDrawable(new TextureRegion(
                mgr.get(Constants.BTN_RECT_DOWN, Texture.class)));
        return s;
    }

    /** Round button style — icon / small action buttons. */
    public static TextButton.TextButtonStyle makeRoundStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font = font;
        s.up   = new TextureRegionDrawable(new TextureRegion(
                mgr.get(Constants.BTN_ROUND_UP, Texture.class)));
        s.down = new TextureRegionDrawable(new TextureRegion(
                mgr.get(Constants.BTN_ROUND_DN, Texture.class)));
        return s;
    }

    /** Convenience: create and size a rectangle button in one call. */
    public static TextButton makeButton(String label, TextButton.TextButtonStyle style,
                                        float w, float h) {
        TextButton btn = new TextButton(label, style);
        btn.setSize(w, h);
        return btn;
    }

    /** Convenience: create a round button of standard size. */
    public static TextButton makeRoundButton(String label, TextButton.TextButtonStyle style) {
        TextButton btn = new TextButton(label, style);
        btn.setSize(Constants.BTN_ROUND, Constants.BTN_ROUND);
        return btn;
    }
}
