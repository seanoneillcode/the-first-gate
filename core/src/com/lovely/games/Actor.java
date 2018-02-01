package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Actor {

    Vector2 pos;
    String id;
    boolean isHidden;
    Vector2 originalPos;

    public Actor(Vector2 pos, String id) {
        this.pos = pos;
        this.id = id;
        this.isHidden = false;
        this.originalPos = pos.cpy();
    }

    public void start() {
        this.pos = originalPos.cpy();
        this.isHidden = false;
    }
}
