package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Actor {

    Vector2 pos;
    String id;
    boolean isHidden;

    public Actor(Vector2 pos, String id) {
        this.pos = pos;
        this.id = id;
        this.isHidden = false;
    }
}
