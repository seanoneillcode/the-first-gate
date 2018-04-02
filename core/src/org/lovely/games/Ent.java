package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Ent {

    private static final float JUMP_TOTAL_TIME = 1.0f;
    private static final float JUMP_HALF_TIME = 0.5f;
    Vector2 pos;
    Vector2 size;
    Vector2 offset;
    Vector2 physics;
    public boolean needsGround;
    public EntState state;
    float fallTimer = 0;
    float delta = 0;
    float jumpTimer = 0;
    float z;
    float impulse = 0;

    public Ent(Vector2 pos, Vector2 size, Vector2 offset, boolean needsGround) {
        this.pos = pos;
        this.size = size;
        this.offset = offset;
        this.needsGround = needsGround;
        this.state = EntState.ALIVE;
        this.physics = new Vector2();
        this.z = 0;
    }

    public void fall() {
        state = Ent.EntState.FALLING;
        fallTimer = 2.0f;
        delta = 0;
    }

    public void jump() {
        // first jump
        if (state == EntState.ALIVE) {
            jumpTimer = JUMP_TOTAL_TIME;
            state = EntState.JUMPING;
            delta = 0;
            impulse = 2.0f;
        }
        // continue to jump
        if (state == EntState.JUMPING && jumpTimer > JUMP_HALF_TIME) {
            impulse = impulse + 0.05f;
        }
    }

    enum EntState {
        DEAD,
        FALLING,
        ALIVE,
        JUMPING
    }

}
