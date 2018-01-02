package com.lovely.games;

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
}
