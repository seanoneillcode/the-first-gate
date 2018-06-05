package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

class PressureTile {

    Vector2 pos;
    Trunk trunk;
    private boolean handledAction;
    String switchId;
    private boolean isSwitch;
    Color color;
    float animTimer;
    boolean isPressure;

    PressureTile(Vector2 pos, String switchId, boolean isSwitch) {
        this.pos = pos;
        this.handledAction = false;
        this.switchId = switchId;
        this.trunk = null;
        this.isSwitch = isSwitch;
        this.color = new Color(random(0.8f, 1.0f), random(0.2f, 0.4f), random(0.3f, 0.5f), 1.0f);
        animTimer = 0;
        isPressure = false;
    }

    void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    void start() {
        this.handledAction = false;
        isPressure = false;
        animTimer = 10;
    }

    void update() {
        animTimer += Gdx.graphics.getDeltaTime();
    }

    void handlePressureOff(SoundPlayer soundPlayer) {
        if (handledAction) {
            if (!isSwitch) {
                if (trunk != null) {
                    trunk.broadcast(switchId);
                }
            }
            animTimer = 0;
            isPressure = false;
        }
        this.handledAction = false;
    }

    void handleAction(SoundPlayer soundPlayer) {
        if (!handledAction && trunk != null) {
            trunk.broadcast(switchId);
            handledAction = true;
            isPressure = true;
            soundPlayer.playSound("sound/thunk.ogg", pos);
            animTimer = 0;
        }
    }
}
