package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Platform implements Switchable {

    public static final float SPEED = TILE_SIZE * 2.0f;

    Vector2 pos;
    Vector2 start;
    Vector2 end;
    Vector2 destination;
    float offset;
    float timer;
    boolean isActive;
    String switchId;
    boolean initialIsActive;
    Color color;

    public Platform(Vector2 start, Vector2 end, float offset, boolean isActive, String switchId) {
        this.pos = start.cpy();
        this.start = start;
        this.end = end;
        this.destination = end;
        this.offset = offset;
        this.timer = 0;
        this.isActive = isActive;
        this.initialIsActive = isActive;
        this.switchId = switchId;
        this.color = new Color(random(0.8f, 1.0f), random(0.2f, 0.4f), random(0.8f, 1.0f), 1.0f);
    }

    void start() {
        timer = -offset;
        pos = start.cpy();
        destination = end;
        isActive = initialIsActive;
    }

    public void update() {
        if (isActive) {
            if (timer < 0) {
                timer = timer + Gdx.graphics.getDeltaTime();
            } else {
                pos.add(getMovement());

                if (destination.dst2(pos) < 1) {
                    if (destination.equals(end)) {
                        destination = start;
                    } else {
                        destination = end;
                    }
                }
            }
        }
    }

    public Vector2 getMovement() {
        Vector2 dir = new Vector2();
        if (isActive) {
            dir = destination.cpy().sub(pos).nor();
        }
        return dir.scl(SPEED * Gdx.graphics.getDeltaTime());
    }

    public void handleMessage(String id) {
        if (this.switchId != null && this.switchId.equals(id)) {
            isActive = !isActive;
        }
    }
}
