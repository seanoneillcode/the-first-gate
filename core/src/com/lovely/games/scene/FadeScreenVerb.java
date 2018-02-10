package com.lovely.games.scene;

import com.badlogic.gdx.Gdx;
import com.lovely.games.Stage;

public class FadeScreenVerb implements SceneVerb {

    private boolean isDone;
    private boolean inDirection;
    private float amount;
    private float time;
    private float originalAmount;

    public FadeScreenVerb(boolean inDirection, float time) {
        this.inDirection = inDirection;
        this.time = time;
        this.amount = inDirection ? time : 0;
        this.originalAmount = amount;
        this.isDone = false;
    }

    @Override
    public void update(Stage stage) {
        if (!isDone) {
            if (inDirection) {
                amount = amount - Gdx.graphics.getDeltaTime();
                if (amount < 0) {
                    isDone = true;
                    amount = 0;
                }
            } else {
                amount = amount + Gdx.graphics.getDeltaTime();
                if (amount > time) {
                    isDone = true;
                    amount = time;
                }
            }

        }
        stage.fadeScreen(amount / time);
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
        amount = originalAmount;
    }

    @Override
    public void skip() {

    }
}