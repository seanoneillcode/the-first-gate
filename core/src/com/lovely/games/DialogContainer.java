package com.lovely.games;

import static com.lovely.games.DialogLine.line;
import static com.lovely.games.DialogLine.options;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class DialogContainer {

    Map<String, List<DialogElement>> dialogs = new HashMap<>();
    String pro = "pro";
    String ant = "ant";
    String info = "info";
    private Sprite portrait;

    {
        dialogs.put("1", Arrays.asList(
                line(pro, "I must push on with all haste, perhaps I can catch him")
        ));
        dialogs.put("2", Arrays.asList(
                line(info, "( press 'R' to restart a level )")
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
                line(ant, "You've gone far enough, I've sabotaged this trial to make sure")
        ));
        dialogs.put("12", Arrays.asList(
                line(ant, "Stop! damn it!"),
                line(ant, "It's mine, get out of the way."),
                line(pro, "No"),
                line(pro, "I completed the trials, despite your sabotage, and I did it first."),
                line(ant, "Listen here you little shit, are you forgetting who I am?"),
                line(ant, "I have more estates and money then you're shitty village."),
                line(ant, "I will ruin you before I kill you so you and no one else can forget."),
                line(pro, "Do it."),
                line(pro, "I'm getting what I earned.")
        ));
        dialogs.put("13", Arrays.asList(
                line(pro, "I can feel a strange power, stirring inside of me"),
                line(info, "(press 'spacebar' to cast a spell)")
        ));
        dialogs.put("14", Arrays.asList(
                line(ant, "Wait! don't leave me here."),
                line(ant, "Step on the switch so I can cross")
        ));
        dialogs.put("15", Arrays.asList(
                line(ant, "No!"),
                line(ant, "No! You bastard!"),
                line(ant, "I'll kill you!")
        ));
        dialogs.put("16", Arrays.asList(
                line(ant, "Thank you for your kindness.")
        ));
        dialogs.put("17", Arrays.asList(
                line(ant, "Fool, now I'll take what's mine.")
        ));
        dialogs.put("18", Arrays.asList(
                line(pro, "Now to take the stone")
        ));
        dialogs.put("19", Arrays.asList(
                line(ant, "What do you want?"),
                options(pro)
                        .opt("When will we arrive? It's been days of travel", "18")
                        .opt("Why do I have to carry everything...", "19")
                        .opt("Where are we going?", "20")
                        .build()
        ));
        dialogs.put("21", Arrays.asList(
                line(ant, "Only a day or two more. Over the nearest peak and through a valley."),
                line(pro, "We will run out of supplies for the return journey if we go too far"),
                line(ant, "I'm well aware..."),
                line(ant, "Do not fret, I have something planned. Now leave me be.")
        ));
        dialogs.put("22", Arrays.asList(
                line(ant, "Because I'm paying you to. You need the money and I can't be bothered to carry it myself")
        ));
        dialogs.put("23", Arrays.asList(
                line(ant, "I told you several times, a small castle belonging to my cousin."),
                line(pro, "I haven't heard of any castles out this far."),
                line(ant, "Are you calling me a liar?"),
                line(pro, "No I just..."),
                line(ant, "Hold your tongue if you want your pay.")
        ));
        dialogs.put("20", Arrays.asList(
                options(info)
                        .opt("sleep", "17")
                        .opt("stay up a while longer", null)
                        .build()
        ));
    }

    String currentDialog;
    Integer dialogIndex;
    private BitmapFont font;
    private float timer;
    private Texture dialogBottom, dialogTop, dialogLineImg;
    private Color fontColorMain = new Color(202.0f / 256.0f, 253.0f  / 256.0f, 255.0f / 256.0f, 1);
    private Color fontColorSecondary = new Color(7.0f / 256.0f, 0.0f  / 256.0f, 7.0f / 256.0f, 1);
    private Map<String, Texture> portraits;

    public DialogContainer(Texture dialogBottom, Texture dialogTop, Texture dialogLineImg, Texture proPortrait, Texture antPortrait) {
        currentDialog = null;
        dialogIndex = 0;
        font = loadFonts();
        timer = 0;
        this.dialogBottom = dialogBottom;
        this.dialogTop = dialogTop;
        this.dialogLineImg = dialogLineImg;
        portraits = new HashMap<>();
        portraits.put("ant", antPortrait);
        portraits.put("pro", proPortrait);
        this.portrait = new Sprite(portraits.get("pro"));
    }

    void render(SpriteBatch batch, Vector2 offset, DialogElement dialogLine) {
        Vector2 dialogPos = offset.cpy().add(64, 16);

        List<String> lines = dialogLine.getLines();
        float ypos = dialogLine.getTotalLines() * 32;
        batch.draw(dialogTop, dialogPos.x, dialogPos.y + ypos + 16 + 8);
        batch.draw(dialogBottom, dialogPos.x, dialogPos.y + 16);
        for (int i = 0; i < dialogLine.getTotalLines(); i++) {
            batch.draw(dialogLineImg, dialogPos.x, dialogPos.y + 16 + (i * 32) + 8);
        }

        if (portraits.containsKey(dialogLine.getOwner())) {
            portrait.setRegion(portraits.get(dialogLine.getOwner()));
            boolean isLeft = dialogLine.getOwner().equals("pro");
            portrait.setPosition(dialogPos.x + (isLeft ? 0 : 348), dialogPos.y + ypos + 32);
            portrait.draw(batch);
        }

        for (String line : lines) {
            font.setColor(fontColorSecondary);
            font.draw(batch, line, dialogPos.x + 10, dialogPos.y + 16 + 2 + ypos);
            font.setColor(fontColorMain);
            font.draw(batch, line, dialogPos.x + 10, dialogPos.y + 16 + ypos);
            ypos = ypos - 32;
        }
    }

    private BitmapFont loadFonts() {
        FileHandle handle = Gdx.files.internal("roboto.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        BitmapFont font = generator.generateFont(parameter);
        font.setUseIntegerPositions(false);
        font.setColor(fontColorMain);
        return font;
    }
}
