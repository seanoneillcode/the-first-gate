package com.lovely.games;

import static com.lovely.games.Level.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ArrowSource {

    Vector2 pos;
    Vector2 dir;
    float offset;
    float delay;
    float timer;

    public ArrowSource(Vector2 pos, Vector2 dir, float offset, float delay) {
        this.pos = pos;
        this.dir = dir;
        this.offset = offset;
        this.delay = delay;
        this.timer = 0;
    }

    public void start() {
        this.timer = delay - offset;
    }

    public void update(TheFirstGate theFirstGate) {
        timer = timer + Gdx.graphics.getDeltaTime();
        if (timer > delay) {
            timer = 0;
            Vector2 startPos = pos.cpy().add(dir.cpy().scl(TILE_SIZE));
            theFirstGate.addArrow(startPos, dir);
        }
    }
}
