package com.lovely.games;

import static com.lovely.games.DialogLine.line;
import static com.lovely.games.DialogLine.options;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DialogContainer {

    public static final float NORMAL_FACE_SCALE = 0.6f;
    public static final float SMALL_FACE_SCALE = 0.54f;
    public static final float SCALE_INCREASE_AMOUNT = 0.01f;
    private final Sprite rightDialogPointer;
    Map<String, List<DialogElement>> dialogs = new HashMap<>();
    String pro = "pro";
    String ant = "ant";
    String info = "info";
    private Sprite leftPortrait, rightPortrait;
    private String lastAntMood;

    {
        dialogs.put("1", Arrays.asList(
                line(pro, "Perhaps I can catch him")
        ));
        dialogs.put("2", Arrays.asList(
                line(info, "( press 'R' to restart a level )")
        ));
        dialogs.put("3", Arrays.asList(
                line(pro, "I've never seen a flying ball of fire before. Magic!", "happy"),
                line(pro, "I better not let it touch me though", "worried")
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
                line(ant, "You must to stand on the other floor switch, both floor switches need to be active"),
                line(pro, "I'm only here to carry the gear", "angry"),
                line(ant, "Just stand on the switch, then you can go.")
        ));
        dialogs.put("7", Arrays.asList(
                line(pro, "Damn!", "angry"),
                line(pro, "Now I can't leave", "angry")
        ));
        dialogs.put("8", Arrays.asList(
                line(pro, "What does it say..."),
                line(info, "\"Power requires sacrifice.\"")
        ));
        dialogs.put("9", Arrays.asList(
                line(pro, "Stand on the switch, I can't leave.", "worried"),
                line(ant, "Back off you filthy peasant, you will demand nothing of me.", "angry"),
                line(pro, "What!?", "angry"),
                line(ant, "You will remain here. I will return later.")
        ));
        dialogs.put("10", Arrays.asList(
                line(ant, "You don't know what you're playing with, turn back now before you get hurt.", "worried"),
                line(pro, "I'll go back if you go with me and let me out.")
        ));
        dialogs.put("11", Arrays.asList(
                line(pro, "I can feel the power of this stone!", "happy"),
                line(pro, "My blood hurts though", "worried"),
                line(ant, "You will learn to live with the pain in time.", "talk"),
                line(ant, "The next tests will teach you how to use the stone.", "talk"),
                line(ant, "You must also think about the power you hold. Will you use it to kill? To take what is not yours?", "talk"),
                line(pro, "I... I don't know...", "worried"),
                line(ant, "You must know these things!", "angry"),
                line(ant, "This is great power, a force to create or destroy. A power over others.", "angry")
        ));
        dialogs.put("12", Arrays.asList(
                line(pro, "Those tests are bloody hard!", "angry"),
                line(ant, "Then you should be happy", "happy"),
                line(ant, "A Great reward demands a great challenge", "talk"),
                line(pro, "Reward? I hope it is gold.", "happy"),
                line(ant, "Why?", "talk"),
                line(pro, "I am a landless peasant. I have nothing", "worried"),
                line(ant, "There is no gold here.", "talk"),
                line(pro, "What? Then what is the reward!?", "angry"),
                line(ant, "Power. See for yourself. Take the stone.", "talk")
        ));
        dialogs.put("13", Arrays.asList(
                line(pro, "My bones feel like they're going to burst", "worried"),
                line(info, "(press 'spacebar' to cast a spell)")
        ));
        dialogs.put("14", Arrays.asList(
                line(ant, "Yes?"),
                options(pro)
                        .opt("Is this the Caen?", "18")
                        .opt("Who are you?", "19")
                        .opt("What should I do?", "20")
                        .build()
        ));
        dialogs.put("15", Arrays.asList(
                line(ant, "No!", "angry"),
                line(ant, "No! You bastard!", "angry"),
                line(ant, "I'll kill you!", "angry")
        ));
        dialogs.put("16", Arrays.asList(
                line(ant, "Thank you for your kindness.", "happy"),
                line(ant, "Fool, now I'll take what's mine.", "happy")
        ));
        dialogs.put("18", Arrays.asList(
                line(pro, "There's something here...")
        ));
        dialogs.put("19", Arrays.asList(
                line(ant, "Yes?"),
                options(pro)
                        .opt("When will we arrive?", "18")
                        .opt("Why do I have to carry everything...", "19")
                        .opt("Where are we going?", "20")
                        .mood("worried")
                        .build()
        ));
        dialogs.put("21", Arrays.asList(
                line(ant, "Yes. This is the Caen."),
                line(ant, "The Caen is a series or rooms that require careful thinking and skillful action to pass through."),
                line(ant, "Make no mistake though, this is not a simple test."),
                line(ant, "They are dangerous. Any hesitation or incorrect action will kill you.")
        ));
        dialogs.put("22", Arrays.asList(
                line(ant, "I am the caretaker of the Caen."),
                line(ant, "I clean the rooms"),
                line(pro, "That's nice", "happy"),
                line(ant, "It's mostly removing bodies and cleaning blood"),
                line(pro, "Oh... that's less nice", "worried"),
                line(ant, "Well I quite enjoy it", "happy")

        ));
        dialogs.put("23", Arrays.asList(
                line(ant, "That's for you figure out."),
                line(ant, "Have a look around."),
                line(pro, "Thanks..."),
                line(ant, "Well the goal is the exit at the top of this room"),
                line(ant, "But the tests are taken in a linear fashion"),
                line(ant, "You can start at the bottom right room if you're eager")
        ));
//        dialogs.put("24", Arrays.asList(
//                line(pro, "What next?"),
//                line(ant, "You can rest here for a while. The Caen is not easy."),
//                line(pro, "And after that?"),
//                line(ant, "As always, that is up to you. You may leave with the stone."),
//                line(pro, "So ... there's no gold?"),
//                line(ant, "There is no gold."),
//                line(pro, "I cannot eat the stone.", "angry"),
//                line(ant, "Why not sell it?"),
//                line(pro, "Sell it? This would turn a man into criminal, a noble into a tyrant.", "angry"),
//                line(pro, "It is a curse", "angry"),
//                line(ant, "Yes, now you understand.", "happy"),
//                line(ant, "I am happy that you understand the nature of power. Listen to me.", "talk"),
//                line(ant, "I can take you to Bryn, the hearth of Mages. There you can learn to control the power, to use it ethically."),
//                line(pro, "You would turn me, a peasant, into a Mage?", "worried"),
//                line(ant, "Yes, anyone who survives the Caen can enter Bryn."),
//                line(ant, "It is another set of trials though. Not of block and switches but of people. Not in rooms but in the mind."),
//                line(ant, "Surviving Bryn is much harder than the Caen."),
//                line(pro, "But is the reward equal to the risk?"),
//                line(ant, "well... a Mage will not go hungry..."),
//                line(ant, "Take the night to rest and think on it.")
//        ));
        dialogs.put("24", Arrays.asList(
                line(ant, "I suggest you rest"),
                line(pro, "And then?"),
                line(ant, "I suggest you come with me to Bryn, to the college of Mages"),
                line(pro, "For another trial I suppose? One that puts me in jail or worse"),
                line(ant, "Actually, I would like you to join us. "),
                line(ant, "We will provide everything you need. Food, shelter..."),
                line(ant, "...education"),
                line(pro, "I don't know"),
                line(ant, "I was wrong."),
                line(ant, "You have shown me this."),
                line(ant, "Think on it. Perhaps you can show the rest of us what really matters"),
                line(pro, "Determination?"),
                line(ant, "Yes, determination and more...")
        ));
        dialogs.put("saveWarning", Arrays.asList(
                line(info, "There is already a save game. Starting a new game will overwrite the save game."),
                line(info, "Do you wish to start a new game?"),
                options(info)
                        .opt("yes", "new-game")
                        .opt("no", "menu")
                        .build()
        ));
//        dialogs.put("21", Arrays.asList(
//                line(ant, "Only a day or two more. Over the nearest peak and through a valley."),
//                line(pro, "We will run out of supplies for the return journey if we go too far.", "worried"),
//                line(ant, "I have something planned, don't worry.", "happy")
//        ));
//        dialogs.put("22", Arrays.asList(
//                line(ant, "Because I'm paying you to. You need the money and I can't be bothered to carry it myself"),
//                line(pro, "That's true", "happy")
//        ));
//        dialogs.put("23", Arrays.asList(
//                line(ant, "I told you several times, a small castle belonging to my cousin."),
//                line(pro, "But I haven't heard of any castles out this far.", "worried"),
//                line(ant, "That's no surprise with your education, or lack thereof. There are many wonderful things outside of your village."),
//                line(pro, "No I just...", "worried"),
//                line(ant, "Trust me, my uncle will put us up and provide food and drink plenty.", "happy")
//        ));
        dialogs.put("20", Arrays.asList(
                options(info)
                        .opt("sleep", "17")
                        .opt("stay up a little longer", null)
                        .build()
        ));
        dialogs.put("25", Arrays.asList(
                line(ant, "You're doing great!", "happy"),
                line(ant, "...almost there"),
                line(pro, "Almost?"),
                line(ant, "The next one is a bit tricky, but it's worth it.", "talk"),
                line(pro, "Any hints or tips?"),
                line(ant, "No", "happy"),
                line(ant, "Good luck!", "happy")
        ));
        dialogs.put("26", Arrays.asList(
                line(ant, "Who are you? Why are you here!?"),
                line(pro, "I am here to pass through the first gate", "worried"),
                line(ant, "You are joking?", "happy"),
                line(ant, "You are missing a leg, you are clearly a beggar", "talk"),
                line(pro, "I have travelled far and suffered much just to take these trials", "worried"),
                line(ant, "Begone you fool, this place is for the most able men and women.", "angry"),
                line(ant, "You must be of sound mind and body.", "talk"),
                line(ant, "Begone", "talk")
        ));
        dialogs.put("27", Arrays.asList(
                line(pro, "We will see", "angry")
        ));
        dialogs.put("28", Arrays.asList(
                line(ant, "I told you to leave"),
                line(pro, "Leave? Not without taking the trials", "talk"),
                line(ant, "These trials are not for wretched folk like you", "talk"),
                line(pro, "You know nothing about me", "angry"),
                line(ant, "Are you of noble blood?", "happy"),
                line(ant, "...do you posses a fine education?", "happy"),
                line(pro, "No...", "worried"),
                line(ant, "Then be gone", "angry"),
                line(pro, "I will not be gone! I have the most important quality", "angry"),
                line(pro, "Determination", "angry")
        ));
        dialogs.put("29", Arrays.asList(
                line(ant, "I see you. If you try that again I will burn you to a cinder", "angry")
        ));
        dialogs.put("30", Arrays.asList(
                line(pro, "I must be quick! If the old man catches me I'm dead...", "worried")
        ));
        dialogs.put("31", Arrays.asList(
                line(ant, "Well done, you've completed the trials"),
                line(ant, "However..."),
                line(ant, "I cannot let you leave."),
                line(pro, "What? Why not?"),
                line(ant, "You have cheated I am sure, there is no other way you could get this far", "angry"),
                line(ant, "I will stop this now, only those who actually take the trials may pass.", "angry")
        ));
        dialogs.put("32", Arrays.asList(
                line(pro, "You are undone"),
                line(pro, "By my hand?"),
                line(ant, "..."),
                line(ant, "Yes"),
                line(ant, "I am very sorry, you are more the man than I"),
                line(pro, "What now? I can leave?"),
                line(ant, "There is a resting place ahead. Go there now and I will meet you momentarily.")

        ));
        dialogs.put("33", Arrays.asList(
                line(pro, "What does this say..."),
                line(pro, "'RULES OF THE CAEN'"),
                line(pro, "'1. All rooms have a solution'"),
                line(pro, "'2. All rooms have one entrance and one exit'"),
                line(pro, "'3. You must have fun'")

        ));
    }

    String currentDialog;
    Integer dialogIndex;
    private BitmapFont font;
    private float timer;
    private Texture dialogBottom, dialogTop, dialogLineImg;
    private Color fontColorMain = new Color(6f / 256.0f, 27f  / 256.0f, 46f / 256.0f, 1);
    private Color fontColorHighlighted = fontColorMain;//new Color(110f / 256.0f, 36f / 256.0f, 61f / 256.0f, 1);
    private Color fontColorSecondary = new Color(7.0f / 256.0f, 0.0f  / 256.0f, 7.0f / 256.0f, 1);
    private Map<String, Texture> portraits;
    private Sprite dialogPointer, optionPointer;
    private Set<String> actors;
    private String lastMood;
    private float leftScaleTarget, rightScaleTarget, leftScaleAmount, rightScaleAmount;

    String lastChar;

    public DialogContainer(AssetManager assetManager) {
        currentDialog = null;
        dialogIndex = 0;
        font = loadFonts("fonts/decent.fnt");
        timer = 0;
        this.dialogBottom = assetManager.get("dialog-bottom.png");
        this.dialogTop = assetManager.get("dialog-top.png");
        this.dialogLineImg = assetManager.get("dialog-line.png");
        this.dialogPointer = new Sprite((Texture) assetManager.get("dialog-pointer.png"));
        this.rightDialogPointer = new Sprite((Texture) assetManager.get("dialog-pointer.png"));
        this.optionPointer = new Sprite((Texture) assetManager.get("option-pointer.png"));
        rightDialogPointer.flip(true, false);
        portraits = new HashMap<>();
        portraits.put("pro-talk", assetManager.get("portraits/portrait-pro.png"));
        portraits.put("pro-listening", assetManager.get("portraits/portrait-pro-listening.png"));
        portraits.put("pro-angry", assetManager.get("portraits/portrait-pro-angry.png"));
        portraits.put("pro-happy", assetManager.get("portraits/portrait-pro-happy.png"));
        portraits.put("pro-worried", assetManager.get("portraits/portrait-pro-worried.png"));
        portraits.put("ant-talk", assetManager.get("portraits/portrait-ant-talking.png"));
        portraits.put("ant-listening", assetManager.get("portraits/portrait-ant-listening.png"));
        portraits.put("ant-angry", assetManager.get("portraits/portrait-ant-angry.png"));
        portraits.put("ant-happy", assetManager.get("portraits/portrait-ant-happy.png"));
        portraits.put("ant-worried", assetManager.get("portraits/portrait-ant-angry.png"));
        this.leftPortrait = new Sprite(portraits.get("pro-talk"));
        this.rightPortrait = new Sprite(portraits.get("ant-talk"));
        this.rightPortrait.flip(true, false);
        leftScaleTarget = NORMAL_FACE_SCALE;
        rightScaleTarget = NORMAL_FACE_SCALE;
        leftScaleAmount = NORMAL_FACE_SCALE;
        rightScaleAmount = NORMAL_FACE_SCALE;
        leftPortrait.setScale(leftScaleTarget);
        rightPortrait.setScale(rightScaleTarget);
        lastMood = "pro-listening";
        lastAntMood = "ant-listening";
    }

    void render(SpriteBatch batch, Vector2 offset, Conversation conversation, SoundPlayer soundPlayer) {
        Vector2 dialogPos = offset.cpy().add(0, 16);
        DialogElement dialogLine = conversation.getCurrentDialog();
        actors = new HashSet<>(conversation.getActors());
        float portraitHeight = 170;
        boolean isLeft = dialogLine.getOwner().equals("pro");
        if (isLeft) {
            if (dialogLine.getMood() != null) {
                leftPortrait.setTexture(portraits.get("pro-" + dialogLine.getMood()));
                lastMood = "pro-" + dialogLine.getMood();
            } else {
                leftPortrait.setTexture(portraits.get("pro-talk"));
                lastMood = "pro-listening";
            }
            dialogPointer.setPosition(dialogPos.x + 150, dialogPos.y + portraitHeight - 40);
            rightPortrait.setTexture(portraits.get(lastAntMood));
            leftScaleTarget = NORMAL_FACE_SCALE;
            rightScaleTarget = SMALL_FACE_SCALE;
        } else {
            if (dialogLine.getMood() != null) {
                rightPortrait.setTexture(portraits.get("ant-" + dialogLine.getMood()));
                lastAntMood = "ant-" + dialogLine.getMood();
            } else {
                rightPortrait.setTexture(portraits.get("ant-talk"));
                lastAntMood = "ant-listening";
            }
            leftPortrait.setTexture(portraits.get(lastMood));
            rightDialogPointer.setPosition(dialogPos.x + 302, dialogPos.y + portraitHeight - 40);
            rightScaleTarget = NORMAL_FACE_SCALE;
            leftScaleTarget = SMALL_FACE_SCALE;
        }
        if (leftScaleAmount < leftScaleTarget) {
            leftScaleAmount += SCALE_INCREASE_AMOUNT;
        }
        if (leftScaleAmount > leftScaleTarget) {
            leftScaleAmount -= SCALE_INCREASE_AMOUNT;
        }
        if (rightScaleAmount < rightScaleTarget) {
            rightScaleAmount += SCALE_INCREASE_AMOUNT;
        }
        if (rightScaleAmount > rightScaleTarget) {
            rightScaleAmount -= SCALE_INCREASE_AMOUNT;
        }
        leftPortrait.setScale(leftScaleAmount);
        rightPortrait.setScale(rightScaleAmount);
        if (actors.contains("pro")) {
            leftPortrait.setPosition(dialogPos.x - 20, dialogPos.y + 4 - 100 + 40);
            leftPortrait.draw(batch);
        }
        if (actors.contains("ant")) {
            rightPortrait.setPosition(dialogPos.x + 302, dialogPos.y + 4 - 170 + 40);
            rightPortrait.draw(batch);
        }
        if (dialogLine.getOwner().equals("info")) {
            dialogPos.x = dialogPos.x + 40;
        }

        List<String> lines = dialogLine.getLines();
        float ypos = dialogLine.getTotalLines() * 32;

        float startHeight = portraitHeight + 16;
        if (!isLeft) {
            dialogPos.x = dialogPos.x + 120;
        }
        batch.draw(dialogBottom, dialogPos.x, dialogPos.y + startHeight);
        batch.draw(dialogTop, dialogPos.x, dialogPos.y + 8 + ypos + startHeight);
        for (int i = 0; i < dialogLine.getTotalLines(); i++) {
            batch.draw(dialogLineImg, dialogPos.x, dialogPos.y + 8 + (i * 32) + startHeight);
        }

        if (!dialogLine.getOwner().equals("info")) {
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
//                font.setColor(fontColorSecondary);
//                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y - 1 + ypos + startHeight - 2);
                font.setColor(fontColorHighlighted);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y + ypos + startHeight - 2);
            } else {
//                font.setColor(fontColorSecondary);
//                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y - 1 + ypos + startHeight - 2);
                font.setColor(fontColorMain);
                font.draw(batch, line, dialogPos.x + 10 + offsetx, dialogPos.y + ypos + startHeight - 2);
            }

            ypos = ypos - 32;
        }

        String lastLine = lines.get(lines.size() - 1);
        if (!lastLine.isEmpty()) {
            String thisChar = lastLine.substring(lastLine.length() - 1);
            if (!thisChar.isEmpty() && (lastChar == null || !thisChar.equals(lastChar))) {
                if (!thisChar.equals(" ")) {
                    if (isLeft) {
                        soundPlayer.playSound("sound/talk-high-beep.ogg");
                    } else {
                        soundPlayer.playSound("sound/talk-shift.ogg");
                    }
                    lastChar = thisChar;
                }
            }
        }

    }

    private BitmapFont loadFonts(String fontString) {
        font = new BitmapFont(Gdx.files.internal(fontString),false);
        font.setUseIntegerPositions(false);
        font.setColor(fontColorMain);
        return font;
    }

    public void reset() {
        lastMood = "pro-listening";
    }
}
