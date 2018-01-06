package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Platform {

    private static final float SPEED = 32f * 2.0f;

    Vector2 pos;
    Vector2 start;
    Vector2 end;
    Vector2 destination;

    public Platform(Vector2 start, Vector2 end) {
        this.pos = start.cpy();
        this.start = start;
        this.end = end;
        this.destination = end;
    }

    public void update() {
        Vector2 dir = destination.cpy().sub(pos).nor();
        pos.add(dir.scl(SPEED * Gdx.graphics.getDeltaTime()));

        if (destination.dst2(pos) < 14) {
            if (destination.equals(end)) {
                destination = start;
            } else {
                destination = end;
            }
        }
    }
}
