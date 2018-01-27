package com.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class LevelLight {

    Vector2 pos;
    Vector2 size;
    Color color;

    public LevelLight(Vector2 pos, Vector2 size, Color color) {
        this.pos = pos;
        this.size = size;
        this.color = color;
    }
}
