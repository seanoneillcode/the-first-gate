package org.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tile {

    Vector2 pos;
    Vector2 size;
    String image;
    boolean isGround;
    Color color;

    public Tile(Vector2 pos, Vector2 size, String image, boolean isGround, Color color) {
        this.pos = pos;
        this.image = image;
        this.isGround = isGround;
        this.size = size;
        this.color = color;
    }
}
