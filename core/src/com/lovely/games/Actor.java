package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Actor {

    Vector2 pos;
    String id;
    boolean isHidden;
    Vector2 originalPos;
    boolean originalIsHide;

    public Actor(Vector2 pos, String id, boolean isHide) {
        this.pos = pos;
        this.id = id;
        this.originalIsHide = isHide;
        this.isHidden = isHide;
        this.originalPos = pos.cpy();
    }

    public void start() {
        this.pos = originalPos.cpy();
        this.isHidden = originalIsHide;
    }
}
