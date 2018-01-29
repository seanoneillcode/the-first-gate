package com.lovely.games.scene;

import com.badlogic.gdx.math.Vector2;

public class SceneSource {

    public Vector2 pos;
    public String id;
    public Vector2 size;
    public boolean isDone;

    public SceneSource(Vector2 pos, String id, Vector2 size) {
        this.pos = pos;
        this.id = id;
        this.size = size;
        this.isDone = false;
    }
}
