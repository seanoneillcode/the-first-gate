package com.lovely.games.scene;

import static com.lovely.games.TheFirstGate.RANDOM_SOUND_ID_RANGE;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lovely.games.Stage;

public class PlaySoundVerb implements SceneVerb {

    boolean isDone;
    String name;
    int id;
    Vector2 pos;

    public PlaySoundVerb(String name, Vector2 pos) {
        this.isDone = false;
        this.name = name;
        this.id = MathUtils.random(RANDOM_SOUND_ID_RANGE);
        this.pos = pos;
    }

    @Override
    public void update(Stage stage) {
        if (!isDone) {
            isDone = true;
            stage.playSound(id, name, pos);
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

    @Override
    public void start() {
        isDone = false;

    }

    @Override
    public void skip() {

    }

    @Override
    public String getOutcome() {
        return null;
    }
}
