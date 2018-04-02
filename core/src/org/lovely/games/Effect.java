package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Effect {
    Vector2 pos;
    float timer;
    String image;
    float anim;

    public Effect(Vector2 pos, float timer, String image) {
        this.pos = pos;
        this.timer = timer;
        this.image = image;
        this.anim = 0;
    }
}
