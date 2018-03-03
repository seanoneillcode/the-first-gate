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

    public static final float ARROW_SPD = TILE_SIZE * 2.0f;
    Texture img;
    Vector2 pos;
    Vector2 dir;
    Color color;
    float speed;

    public Arrow(Texture img, Vector2 pos, Vector2 dir, float speed) {
        this.img = img;
        this.pos = pos;
        this.dir = dir;
        this.speed = speed;
        this.color = new Color(random(0.2f, 0.4f), random(0.7f, 1.0f) , random(0.5f, 0.8f), 1.0f);
    }

    public void update(TheFirstGate theFirstGate) {
        pos = pos.add(dir.cpy().scl(Gdx.graphics.getDeltaTime() * speed));
    }

    public void draw(SpriteBatch batch) {
        batch.draw(img, pos.x, pos.y);
    }

    public Rectangle getRect() {
        float buffer = TILE_SIZE * 0.2f;
        float playerSize = TILE_SIZE - buffer - buffer;
        return new Rectangle(pos.x + buffer, pos.y + buffer, playerSize, playerSize);
    }
}
