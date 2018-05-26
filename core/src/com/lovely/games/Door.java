package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Door implements Switchable {

    Vector2 pos;
    boolean isOpen;
    String switchId;
    Color color;
    private boolean originalIsOpen;
    SoundPlayer soundPlayer;

    public Door(Vector2 pos, boolean isOpen, String switchId, SoundPlayer soundPlayer) {
        this.pos = pos;
        this.isOpen = isOpen;
        this.originalIsOpen = isOpen;
        this.switchId = switchId;
        this.color = new Color(random(0.2f, 0.4f), random(0.8f, 1.0f), random(0.3f, 0.4f), 1.0f);
        this.soundPlayer = soundPlayer;
    }

    public void start() {
        this.isOpen = originalIsOpen;
    }

    @Override
    public void handleMessage(String id) {
        if (switchId != null && switchId.equals(id)) {
            isOpen = !isOpen;
            soundPlayer.playSound("sound/block-0.ogg", false, 1.0f);
        }
    }
}
