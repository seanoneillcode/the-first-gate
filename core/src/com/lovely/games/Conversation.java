package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Conversation {

    private List<DialogElement> dialogs;
    private int dialogIndex;

    public Conversation(List<DialogElement> dialogs) {
        this.dialogs = dialogs;
        dialogIndex = 0;
    }

    public void reset() {
        dialogIndex = 0;
        for (DialogElement dialog : dialogs) {
            dialog.reset();
        }
    }

    public void update() {
        getCurrentDialog().update();
    }

    public boolean isFinished() {
        return dialogIndex == dialogs.size() - 1 && getCurrentDialog().isFinished();
    }

    public DialogElement getCurrentDialog() {
        return dialogs.get(dialogIndex);
    }

    public void handleInput(Vector2 inputVector) {
        if (!getCurrentDialog().isFinished()) {
            getCurrentDialog().handleInput(inputVector);
        } else {
            dialogIndex++;
            if (dialogIndex > dialogs.size() - 1) {
                dialogIndex = dialogs.size() - 1;
            }
        }
    }
}
