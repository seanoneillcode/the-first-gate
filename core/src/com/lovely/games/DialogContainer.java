package com.lovely.games;

import static com.lovely.games.DialogLine.line;
import static com.lovely.games.DialogLine.options;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DialogContainer {

    private final Sprite rightDialogPointer;
    Map<String, List<DialogElement>> dialogs = new HashMap<>();
    String pro = "pro";
    String ant = "ant";
    String info = "info";
    private Sprite leftPortrait, rightPortrait;
    private Color greyColor = new Color(0.3f, 0.3f, 0.3f, 1.0f);
    private Color white = new Color(1, 1, 1, 1.0f);

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
                        .opt("When will we arrive?", "18")
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
    private Color fontColorMain = new Color(172.0f / 256.0f, 203.0f  / 256.0f, 255.0f / 256.0f, 1);
    private Color fontColorHighlighted = new Color(200.0f / 256.0f, 200.0f  / 256.0f, 160.0f / 256.0f, 1);
    private Color fontColorSecondary = new Color(7.0f / 256.0f, 0.0f  / 256.0f, 7.0f / 256.0f, 1);
    private Map<String, Texture> portraits;
    private Sprite dialogPointer, optionPointer;
    private Set<String> actors;

    public DialogContainer(AssetManager assetManager) {
        currentDialog = null;
        dialogIndex = 0;
        font = loadFonts();
        timer = 0;
        this.dialogBottom = assetManager.get("dialog-bottom.png");
        this.dialogTop = assetManager.get("dialog-top.png");
        this.dialogLineImg = assetManager.get("dialog-line.png");
        this.dialogPointer = new Sprite((Texture) assetManager.get("dialog-pointer.png"));
        this.rightDialogPointer = new Sprite((Texture) assetManager.get("dialog-pointer.png"));
        this.optionPointer = new Sprite((Texture) assetManager.get("option-pointer.png"));
        rightDialogPointer.flip(true, false);
        portraits = new HashMap<>();
        portraits.put("pro", assetManager.get("portraits/portrait-1.png"));
        portraits.put("ant", assetManager.get("portraits/red-01.png"));
        this.leftPortrait = new Sprite(portraits.get("pro"));
        this.rightPortrait = new Sprite(portraits.get("ant"));
    }

    void render(SpriteBatch batch, Vector2 offset, Conversation conversation) {
        Vector2 dialogPos = offset.cpy().add(64, 16);
        DialogElement dialogLine = conversation.getCurrentDialog();
        actors = new HashSet<>(conversation.getActors());
        if (portraits.containsKey(dialogLine.getOwner())) {
            boolean isLeft = dialogLine.getOwner().equals("pro");
            if (isLeft) {
                rightPortrait.setColor(greyColor);
                leftPortrait.setColor(white);
                rightPortrait.setScale(0.85f);
                leftPortrait.setScale(1);
                dialogPointer.setPosition(dialogPos.x + 96, dialogPos.y + 128 - 10);
            } else {
                rightPortrait.setScale(1);
                leftPortrait.setScale(0.85f);
                rightPortrait.setColor(white);
                leftPortrait.setColor(greyColor);
                rightDialogPointer.setPosition(dialogPos.x + 302, dialogPos.y + 128 - 10);
            }
            if (actors.contains("pro")) {
                leftPortrait.setPosition(dialogPos.x, dialogPos.y + 4);
                leftPortrait.draw(batch);
            }
            if (actors.contains("ant")) {
                rightPortrait.setPosition(dialogPos.x + 348, dialogPos.y + 4);
                rightPortrait.draw(batch);
            }
        }

        List<String> lines = dialogLine.getLines();
        float ypos = dialogLine.getTotalLines() * 32;

        float startHeight = 128 + 16;

        batch.draw(dialogBottom, dialogPos.x, dialogPos.y + startHeight);
        batch.draw(dialogTop, dialogPos.x, dialogPos.y + 8 + ypos + startHeight);
        for (int i = 0; i < dialogLine.getTotalLines(); i++) {
            batch.draw(dialogLineImg, dialogPos.x, dialogPos.y + 8 + (i * 32) + startHeight);
        }

        if (portraits.containsKey(dialogLine.getOwner())) {
            boolean isLeft = dialogLine.getOwner().equals("pro");
            if (isLeft) {
                dialogPointer.draw(batch);
            } else {
                rightDialogPointer.draw(batch);
            }
        }

        for (String line : lines) {
            String chosenOption = dialogLine.getCurrentOption();
            float offsetx = dialogLine instanceof DialogOption ? 16 : 0;
            if (chosenOption != null && line.equals(chosenOption)) {
                optionPointer.setPosition(dialogPos.x + 6, dialogPos.y - 2 + ypos + startHeight - 14);
                optionPointer.draw(batch);
                font.setColor(fontColorSecondary);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y - 2 + ypos + startHeight - 2);
                font.setColor(fontColorHighlighted);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y + ypos + startHeight - 2);
            } else {
                font.setColor(fontColorSecondary);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y - 2 + ypos + startHeight - 2);
                font.setColor(fontColorMain);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y + ypos + startHeight - 2);
            }

            ypos = ypos - 32;
        }
    }

    private BitmapFont loadFonts() {
        font = new BitmapFont(Gdx.files.internal("consolas.fnt"),false);
        font.setUseIntegerPositions(false);
        font.setColor(fontColorMain);
        return font;
    }
}
