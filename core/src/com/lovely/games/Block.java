package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

class Block {

    static final float TILE_SPEED = 32.0f * 4.0f;

    Vector2 pos;
    Vector2 dir;
    Vector2 startPos;
    boolean isMoving;
    float movementValue;

    Block(Vector2 pos) {
        this.startPos = pos.cpy();
        this.pos = pos;
        this.isMoving = false;
        this.dir = new Vector2();
        this.movementValue = 0;
    }

    void move(Vector2 dir) {
        this.dir = dir.cpy();
        isMoving = true;
        movementValue = 32f / TILE_SPEED;
    }

    void start() {
        this.pos = startPos.cpy();
    }

    void update() {
        if (isMoving) {
            float movementDelta = Gdx.graphics.getDeltaTime();
            movementValue = movementValue - movementDelta;
            if (movementValue < 0) {
                isMoving = false;
                movementDelta = movementDelta + movementValue;
            }
            Vector2 movement = dir.cpy().scl(movementDelta * TILE_SPEED);
            pos.add(movement);
        } else {

        }
    }
}
