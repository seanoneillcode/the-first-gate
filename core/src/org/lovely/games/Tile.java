package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Tile {

    Vector2 pos;
    String image;

    public Tile(Vector2 pos, String image) {
        this.pos = pos;
        this.image = image;
    }
}
