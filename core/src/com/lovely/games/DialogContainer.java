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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class DialogContainer {

    Map<String, List<DialogLine>> dialogs = new HashMap<>();
    String pro = "pro";
    String ant = "ant";
    private Sprite portrait;

    {
        dialogs.put("1", Arrays.asList(
                line(pro, "I must push on with all haste, perhaps I can catch him")
        ));
        dialogs.put("2", Arrays.asList(
                line(pro, "( press 'R' to restart a level )")
        ));
        dialogs.put("3", Arrays.asList(
                line(pro, "!? I've never seen a flying ball of green fire before. Magic!")
        ));
        dialogs.put("4", Arrays.asList(
                line(pro, "( press 'R' to restart a level )")
        ));
        dialogs.put("5", Arrays.asList(
                line(pro, "There it is. The magic stone.")
        ));
        dialogs.put("6", Arrays.asList(
                line(pro, "Okay I got you here. I'm leaving now."),
                line(ant, "Hold!"),
                line(ant, "You must to stand on the other switch, it takes two to open the door."),
                line(pro, "I'm only here to carry the gear"),
                line(ant, "Just stand there to open the door. I will face the trials alone.")
        ));
        dialogs.put("7", Arrays.asList(
                line(pro, "Damn!"),
                line(pro, "Now I can't leave")
        ));
        dialogs.put("8", Arrays.asList(
                line(pro, "What does it say..."),
                line(pro, "\"Two must enter, but only one can gain power. Power comes from sacrifice.\"")
        ));
        dialogs.put("9", Arrays.asList(
                line(pro, "Stand on the switch, I can't leave."),
                line(ant, "Back off you filthy peasant, you will demand nothing of me."),
                line(ant, "You will remain here. I will complete the trials and return.")
        ));
        dialogs.put("10", Arrays.asList(
                line(ant, "You don't know what you're playing with, turn back now before you get hurt."),
                line(pro, "I'll go back if you go with me and let me out.")
        ));
        dialogs.put("11", Arrays.asList(
                line(ant, "You've gone far enough, I've sabotaged this puzzle. Don't die on the way back. HAHAHaha")
        ));
    }

    String currentDialog;
    Integer dialogIndex;
    private BitmapFont font;
    private float timer;
    private Texture dialogBox;
    private Color fontColorMain = new Color(202.0f / 256.0f, 253.0f  / 256.0f, 255.0f / 256.0f, 1);
    private Color fontColorSecondary = new Color(7.0f / 256.0f, 0.0f  / 256.0f, 7.0f / 256.0f, 1);
    private Map<String, Texture> portraits;

    public DialogContainer(Texture dialogBox, Texture proPortrait, Texture antPortrait) {
        currentDialog = null;
        dialogIndex = 0;
        font = loadFonts();
        timer = 0;
        this.dialogBox = dialogBox;
        portraits = new HashMap<>();
        portraits.put("ant", antPortrait);
        portraits.put("pro", proPortrait);
        this.portrait = new Sprite(portraits.get("pro"));
    }

    void render(SpriteBatch batch, Vector2 offset, DialogLine dialogLine) {
        Vector2 dialogPos = offset.cpy().add(64, 32);
        batch.draw(dialogBox, dialogPos.x, dialogPos.y);
        font.setColor(fontColorSecondary);
        font.draw(batch, dialogLine.getLine(), dialogPos.x + 10, dialogPos.y + 64);
        font.setColor(fontColorMain);
        font.draw(batch, dialogLine.getLine(), dialogPos.x + 10, dialogPos.y + 62);
        portrait.setRegion(portraits.get(dialogLine.owner));
        boolean isLeft = dialogLine.owner.equals("pro");
        portrait.setPosition(dialogPos.x + (isLeft ? 0 : 348), dialogPos.y + 80);
        portrait.draw(batch);
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
