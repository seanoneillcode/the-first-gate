package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

class Block {

    static final float TILE_SPEED = TILE_SIZE * 4.0f;

    Vector2 pos;
    Vector2 dir;
    Vector2 startPos;
    boolean isMoving;
    float movementValue;
    boolean isGround;
    Color color;

    Block(Vector2 pos) {
        this.startPos = pos.cpy();
        this.pos = pos;
        this.isMoving = false;
        this.dir = new Vector2();
        this.movementValue = 0;
        this.isGround = false;
        this.color = new Color(random(0.8f, 1.0f), random(0.1f, 0.2f), random(0.1f, 0.2f), 1.0f);
    }

    void move(Vector2 dir) {
        if (isGround) {
            return;
        }
        this.dir = dir.cpy();
        isMoving = true;
        movementValue = TILE_SIZE / TILE_SPEED;
    }

    void start() {
        this.pos = startPos.cpy();
        this.isGround = false;
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
