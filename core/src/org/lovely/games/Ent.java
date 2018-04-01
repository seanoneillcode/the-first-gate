package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Ent {

    Vector2 pos;
    Vector2 size;
    Vector2 offset;
    public boolean needsGround;
    public EntState state;
    float fallTimer = 0;
    float delta = 0;

    public Ent(Vector2 pos, Vector2 size, Vector2 offset, boolean needsGround) {
        this.pos = pos;
        this.size = size;
        this.offset = offset;
        this.needsGround = needsGround;
        this.state = EntState.ALIVE;
    }

    public void fall() {
        state = Ent.EntState.FALLING;
        fallTimer = 2.0f;
        delta = 0;
    }

    enum EntState {
        DEAD,
        FALLING,
        ALIVE
    }

}
