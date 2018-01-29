package com.lovely.games;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class DialogLine {

    private static final float TIME_PER_CHAR = 20f;
    private static final int CHAR_PER_LINE = 36;

    float charTimer;
    int charIndex;

    String owner;
    String line;

    public DialogLine(String owner, String line) {
        this.line = parseLine(line);
        this.owner = owner;
        charTimer = 0;
        charIndex = 0;
    }

    private String parseLine(String line) {
        int lineCount = 0;
        StringBuilder sb = new StringBuilder();
        List<String> words = Arrays.asList(line.split(" "));
        for (String word : words) {
            if (word.length() + lineCount > CHAR_PER_LINE) {
                sb.append("\r\n");
                lineCount = 0;
            }
            lineCount = lineCount + word.length();
            sb.append(word);
            sb.append(" ");
        }
        return sb.toString();
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        charTimer = charTimer + (TIME_PER_CHAR * delta);
        charIndex = (int) charTimer;
        if (charIndex > line.length()) {
            charIndex = line.length();
        }
    }

    public String getLine() {
        return this.line.substring(0, charIndex);
    }

    public void reset() {
        charIndex = 0;
        charTimer = 0;
    }

    public void finish() {
        charIndex = line.length();
        charTimer = charIndex;
    }

    public boolean isFinished() {
        return charIndex >= line.length();
    }

    public static DialogLine line(String owner, String line) {
        return new DialogLine(owner, line);
    }
}
