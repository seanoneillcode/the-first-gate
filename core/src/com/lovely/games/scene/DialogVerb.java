package com.lovely.games.scene;

import com.lovely.games.Stage;

public class DialogVerb implements SceneVerb {

    boolean isDone;
    boolean conversationStarted;
    String id;

    public DialogVerb(String id) {
        this.isDone = false;
        this.conversationStarted = false;
        this.id = id;
    }

    @Override
    public void update(Stage stage) {
        if (!conversationStarted) {
            conversationStarted = true;
            stage.startDialog(id, this);
        }
    }

    public void finish() {
        isDone = true;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
