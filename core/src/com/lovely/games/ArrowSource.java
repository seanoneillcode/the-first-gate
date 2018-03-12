package com.lovely.games;

import static com.lovely.games.TheFirstGate.ARROW_SPEED;
import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ArrowSource implements Switchable {

    Vector2 pos;
    Vector2 dir;
    float offset;
    float delay;
    float timer;
    boolean isActive;
    boolean originalIsActive;
    String switchId;

    public ArrowSource(Vector2 pos, Vector2 dir, float offset, float delay, boolean isActive, String switchId) {
        this.pos = pos;
        this.dir = dir;
        this.offset = offset;
        this.delay = delay;
        this.timer = 0;
        this.isActive = isActive;
        this.originalIsActive = isActive;
        this.switchId = switchId;
    }

    public void start() {
        this.timer = delay - offset;
        this.isActive = originalIsActive;
    }

    public void update(TheFirstGate theFirstGate) {
        if (isActive) {
            timer = timer + Gdx.graphics.getDeltaTime();
            if (timer > delay) {
                timer = 0;
                Vector2 startPos = pos.cpy().add(dir.cpy().scl(TILE_SIZE));
                theFirstGate.addArrow(startPos, dir, ARROW_SPEED);
            }
        }
    }

    @Override
    public void handleMessage(String id) {
        if (this.switchId != null && this.switchId.equals(id)) {
            isActive = !isActive;
        }
    }
}
