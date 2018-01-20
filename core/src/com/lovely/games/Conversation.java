package com.lovely.games;

import java.util.List;

public class Conversation {

    private List<DialogLine> dialogs;
    private int dialogIndex;

    public Conversation(List<DialogLine> dialogs) {
        this.dialogs = dialogs;
        dialogIndex = 0;
    }

    public void reset() {
        dialogIndex = 0;
        for (DialogLine dialog : dialogs) {
            dialog.reset();
        }
    }

    public void update() {
        getCurrentDialog().update();
    }

    public boolean isFinished() {
        return dialogIndex == dialogs.size() - 1 && getCurrentDialog().isFinished();
    }

    public DialogLine getCurrentDialog() {
        return dialogs.get(dialogIndex);
    }

    public void handleInput() {
        if (!getCurrentDialog().isFinished()) {
            getCurrentDialog().finish();
        } else {
            dialogIndex++;
            if (dialogIndex > dialogs.size() - 1) {
                dialogIndex = dialogs.size() - 1;
            }
        }
    }
}
