package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Cloud {
    Vector2 pos;
    String img;
    Vector2 mov;
    float scale;

    public Cloud(Vector2 pos, String img, Vector2 mov, float scale) {
        this.pos = pos;
        this.img = img;
        this.mov = mov;
        this.scale = scale;
    }
}
