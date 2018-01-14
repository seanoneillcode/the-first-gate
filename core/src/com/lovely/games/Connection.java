package com.lovely.games;


import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.math.Vector2;

public class Connection {

    String name;
    String to;
    Vector2 pos;

    public Connection(String name, String to, Vector2 pos) {
        this.name = name;
        this.to = to;
        this.pos = pos;
    }

    public boolean contains(Vector2 other) {
        return other.x > pos.x && other.x < (pos.x + TILE_SIZE)
                && other.y > pos.y && other.y < (pos.y + TILE_SIZE);
    }
}
