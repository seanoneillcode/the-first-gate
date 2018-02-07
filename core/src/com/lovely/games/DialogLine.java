package com.lovely.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class DialogLine implements DialogElement {

    private static final float TIME_PER_CHAR = 20f;
    private static final int CHAR_PER_LINE = 36;

    float charTimer;
    int charIndex;

    String owner;
    int lineIndex;
    List<String> lines;
    boolean isDone;

    public DialogLine(String owner, String line) {
        this.lines = parseLine(line);
        lineIndex = 0;
        this.owner = owner;
        charTimer = 0;
        charIndex = 0;
        isDone = false;
    }

    private List<String> parseLine(String line) {
        List<String> lines = new ArrayList<>();
        int charPerLineCount = 0;
        StringBuilder sb = new StringBuilder();
        List<String> words = Arrays.asList(line.split(" "));
        for (String word : words) {
            if (word.length() + charPerLineCount > CHAR_PER_LINE) {
                lines.add(sb.toString());
                charPerLineCount = 0;
                sb = new StringBuilder();
            }
            charPerLineCount += word.length();
            sb.append(word).append(" ");
        }
        lines.add(sb.toString());
        return lines;
    }

    public void update() {
        if (!isDone) {
            float delta = Gdx.graphics.getDeltaTime();
            charTimer = charTimer + (TIME_PER_CHAR * delta);
            charIndex = (int) charTimer;
            if (charIndex > lines.get(lineIndex).length()) {
                charIndex = 0;
                charTimer = 0;
                lineIndex += 1;
                if (lineIndex == lines.size()) {
                    lineIndex -= 1;
                    isDone = true;
                }
            }
        }
    }



    public List<String> getLines() {
        if (isDone) {
            return lines;
        }
        List<String> returnLines = new ArrayList<>();
        for (int i = 0 ; i < lineIndex; i++) {
            returnLines.add(lines.get(i));
        }
        returnLines.add(lines.get(lineIndex).substring(0,charIndex));
        return returnLines;
    }

    public void reset() {
        charIndex = 0;
        charTimer = 0;
    }

    public void handleInput(Vector2 inputVector) {
        isDone = true;
    }

    @Override
    public int getTotalLines() {
        return lines.size();
    }

    @Override
    public String getChosenOption() {
        return null;
    }

    public boolean isFinished() {
        return isDone;
    }

    public String getOwner() {
        return owner;
    }

    public static DialogLine line(String owner, String line) {
        return new DialogLine(owner, line);
    }

    public static DialogOption options(String owner, String... options) {
        return new DialogOption(owner, Arrays.asList(options));
    }
}
