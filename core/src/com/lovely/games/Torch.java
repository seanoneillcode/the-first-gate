package com.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Torch {

    Vector2 pos;
    Color color;
    boolean isFire;

    public Torch(Vector2 pos, Color color, boolean isFire) {
        this.pos = pos;
        this.color = color;
        this.isFire = isFire;
    }
}
