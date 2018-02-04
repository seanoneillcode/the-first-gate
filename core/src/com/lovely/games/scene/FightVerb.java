package com.lovely.games.scene;

import com.lovely.games.Stage;

public class FightVerb implements SceneVerb {

    public boolean isDone;
    public String name;
    boolean fightStarted;

    public FightVerb(String name) {
        this.isDone = false;
        this.name = name;
        this.fightStarted = false;
    }

    @Override
    public void update(Stage stage) {
        if (!fightStarted) {
            fightStarted = true;
            stage.startFight(name, this);
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
        fightStarted = false;
    }

    @Override
    public void skip() {

    }
}
