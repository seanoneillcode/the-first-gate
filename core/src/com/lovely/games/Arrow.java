package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Arrow {

//    public static final float ARROW_SPD = TILE_SIZE * 2.0f;
    boolean isArrow;
    Vector2 pos;
    Vector2 dir;
    Color color;
    float speed;
    boolean isDead;

    public Arrow(boolean isArrow, Vector2 pos, Vector2 dir, float speed) {
        this.isArrow = isArrow;
        this.pos = pos;
        this.dir = dir;
        this.isDead = false;
        this.speed = speed;
        this.color = new Color(random(0.2f, 0.4f), random(0.7f, 1.0f) , random(0.5f, 0.8f), 1.0f);
    }

    public void update() {
        pos = pos.add(dir.cpy().scl(Gdx.graphics.getDeltaTime() * speed));
    }

    public Rectangle getRect() {
        float buffer = TILE_SIZE * 0.2f;
        float playerSize = TILE_SIZE - buffer - buffer;
        return new Rectangle(pos.x + buffer, pos.y + buffer, playerSize, playerSize);
    }
}
