package com.lovely.games;

import static com.lovely.games.DialogLine.line;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class DialogContainer {

    Map<String, List<DialogLine>> dialogs = new HashMap<>();
    String tom = "tom";
    String paul = "paul";
    Map<String, TextureRegion> portraits;

    {
        dialogs.put("1", Arrays.asList(
                line(tom, "I hope that idiot doesn't catch up"),
                line(paul, "... I think I can hear him. I better push on, the sooner this is over the better.")
        ));
        dialogs.put("2", Arrays.asList(
                line(tom, "What kind of trial is this? A child could cross it.")
        ));
        dialogs.put("3", Arrays.asList(
                line(tom, "!? I've never seen a flying ball of green fire before. Magic!")
        ));
        dialogs.put("4", Arrays.asList(
                line(tom, "(press 'R' to restart a level)")
        ));
        dialogs.put("5", Arrays.asList(
                line(tom, "Here we are, the prize!")
        ));
    }

    String currentDialog;
    Integer dialogIndex;
    private BitmapFont font;
    private float timer;
    private Texture dialogBox;
    private Color fontColorMain = new Color(202.0f / 256.0f, 253.0f  / 256.0f, 255.0f / 256.0f, 1);
    private Color fontColorSecondary = new Color(7.0f / 256.0f, 0.0f  / 256.0f, 7.0f / 256.0f, 1);

    public DialogContainer(Texture dialogBox) {
        currentDialog = null;
        dialogIndex = 0;
        font = loadFonts();
        timer = 0;
        this.dialogBox = dialogBox;
    }

    void render(SpriteBatch batch, Vector2 offset, DialogLine dialogLine) {
            batch.draw(dialogBox, offset.x - 240, offset.y - 160);
            font.setColor(fontColorSecondary);
            font.draw(batch, dialogLine.getLine(), offset.x - 230, offset.y - 96);
            font.setColor(fontColorMain);
            font.draw(batch, dialogLine.getLine(), offset.x - 230, offset.y - 94);
    }

    private BitmapFont loadFonts() {
        FileHandle handle = Gdx.files.internal("roboto.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 22;
        BitmapFont font = generator.generateFont(parameter);
        font.setUseIntegerPositions(false);
        font.setColor(fontColorMain);
        return font;
    }
}
