package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.lovely.games.TheFirstGate.RANDOM_SOUND_ID_RANGE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Door implements Switchable {

    Vector2 pos;
    boolean isOpen;
    String switchId;
    Color color;
    private boolean originalIsOpen;
    SoundPlayer soundPlayer;
    int soundId;
    float animTimer;

    public Door(Vector2 pos, boolean isOpen, String switchId, SoundPlayer soundPlayer) {
        this.pos = pos;
        this.isOpen = isOpen;
        this.originalIsOpen = isOpen;
        this.switchId = switchId;
        this.color = new Color(random(0.8f, 0.9f), random(0.2f, 0.3f), random(0.8f, 0.9f), 1.0f);
        this.soundPlayer = soundPlayer;
        this.soundId = MathUtils.random(RANDOM_SOUND_ID_RANGE);
        animTimer = 0;
    }

    public void start() {
        this.isOpen = originalIsOpen;
        animTimer = 10;
    }

    public void update() {
        animTimer += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleMessage(String id) {
        if (switchId != null && switchId.equals(id)) {
            isOpen = !isOpen;
            animTimer = 0;
            soundPlayer.playSound(soundId, "sound/door.ogg", pos, false);
        }
    }
}
