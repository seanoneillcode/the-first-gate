package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Platform {

    public static final float SPEED = 32f * 2.0f;

    Vector2 pos;
    Vector2 start;
    Vector2 end;
    Vector2 destination;
    float offset;
    float timer;

    public Platform(Vector2 start, Vector2 end, float offset) {
        this.pos = start.cpy();
        this.start = start;
        this.end = end;
        this.destination = end;
        this.offset = offset;
        this.timer = 0;
    }

    void start() {
        timer = -offset;
        pos = start.cpy();
        destination = end;
    }

    public void update() {
        if (timer < 0) {
            timer = timer + Gdx.graphics.getDeltaTime();
        } else {
            pos.add(getMovement());

            if (destination.dst2(pos) < 14) {
                if (destination.equals(end)) {
                    destination = start;
                } else {
                    destination = end;
                }
            }
        }
    }

    public Vector2 getMovement() {
        Vector2 dir = destination.cpy().sub(pos).nor();
        return dir.scl(SPEED * Gdx.graphics.getDeltaTime());
    }
}
