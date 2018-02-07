package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class DialogOption implements DialogElement {

    private String owner;
    private List<String> options;
    private int optionIndex;
    private String line;
    private boolean isDone;

    public DialogOption(String owner, List<String> options) {
        this.owner = owner;
        this.options = options;
        optionIndex = 0;
        isDone = false;
        this.line = options.stream().reduce("", (p, c) -> p + c + "\r\n");
    }

    @Override
    public void reset() {
        optionIndex = 0;
        isDone = false;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return isDone;
    }

    @Override
    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            if (i != optionIndex) {
                lines.add("  " + options.get(i));
            } else {
                lines.add("> " + options.get(i));
            }
        }
        return lines;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void handleInput(Vector2 inputVector) {
        optionIndex = optionIndex + (int) inputVector.y;
        if (optionIndex < 0) {
            optionIndex = options.size() - 1;
        }
        if (optionIndex == options.size()) {
            optionIndex = 0;
        }
        if (inputVector.x == 1) {
            isDone = true;
        }
    }

    @Override
    public int getTotalLines() {
        return options.size();
    }

    @Override
    public String getChosenOption() {
        return options.get(optionIndex);
    }
}
