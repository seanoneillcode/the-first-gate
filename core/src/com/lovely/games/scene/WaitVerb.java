package com.lovely.games.scene;

import com.badlogic.gdx.Gdx;
import com.lovely.games.Stage;

public class WaitVerb implements SceneVerb {

    float timer;
    boolean isDone;

    public WaitVerb(float timer) {
        this.timer = timer;
        this.isDone = false;
    }

    @Override
    public void update(Stage stage) {
        if (!isDone) {
            timer = timer - Gdx.graphics.getDeltaTime();
        }
        if (timer < 0) {
            isDone = true;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
