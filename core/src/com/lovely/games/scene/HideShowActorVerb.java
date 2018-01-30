package com.lovely.games.scene;

import com.lovely.games.Stage;

public class HideShowActorVerb implements SceneVerb {

    boolean hide;
    boolean isDone;
    String actor;

    public HideShowActorVerb(boolean hide, String actor) {
        this.hide = hide;
        this.isDone = false;
        this.actor = actor;
    }

    @Override
    public void update(Stage stage) {
        if (!isDone && hide) {
            stage.hideActor(actor);
            isDone = true;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
