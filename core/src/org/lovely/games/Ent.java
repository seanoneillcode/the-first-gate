package org.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Ent {

    private static final float JUMP_COOLDOWN = -0.5f;
    Vector2 pos;
    Vector2 size;
    Vector2 offset;
    Vector2 physics;
    public boolean needsGround;
    public EntState state;
    float fallTimer = 0;
    float delta = 0;
    float jumpTimer = 0;

    public Ent(Vector2 pos, Vector2 size, Vector2 offset, boolean needsGround) {
        this.pos = pos;
        this.size = size;
        this.offset = offset;
        this.needsGround = needsGround;
        this.state = EntState.ALIVE;
        this.physics = new Vector2();
    }

    public void fall() {
        state = Ent.EntState.FALLING;
        fallTimer = 2.0f;
        delta = 0;
    }

    public void jump(Vector2 movement) {
        if (state == EntState.ALIVE && jumpTimer < JUMP_COOLDOWN) {
            jumpTimer = 0.5f;
            state = EntState.JUMPING;
            delta = 0;
            physics = movement.cpy();
        }
    }

    enum EntState {
        DEAD,
        FALLING,
        ALIVE,
        JUMPING
    }

}
