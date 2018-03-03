package com.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import static com.lovely.games.TheFirstGate.TILE_SIZE;

public class Enemy {

    Vector2 pos;
    Vector2 dir;
    Color color;

    public Enemy(Vector2 pos, String dir) {
        this.pos = pos;
        this.dir = getDir(dir);
        this.color = new Color(0.4f, 0.8f, 0.7f, 1.0f);
    }

    private Vector2 getDir(String dir) {
        switch (dir) {
            case "left":
                return new Vector2(-1, 0);
            case "right":
                return new Vector2(1, 0);
            case "up":
                return new Vector2(0 ,1);
            case "down":
                return new Vector2(0,-1);
        }
        return new Vector2();
    }

    public void update(Vector2 playerPos, TheFirstGate theFirstGate) {
        boolean colliding = false;
        Vector2 checkPos = pos.cpy();
        for (int i = 0; i < 10; i++) {
            checkPos.add(dir.cpy().scl(TILE_SIZE));
            if (contains(checkPos, playerPos)) {
                colliding = true;
                break;
            }        }
        if (colliding) {
            theFirstGate.addLava(pos.cpy(), dir.cpy());
        }
    }

    private boolean contains(Vector2 thisPos, Vector2 other) {
        return other.x > thisPos.x && other.x < (thisPos.x + TILE_SIZE)
                && other.y > thisPos.y && other.y < (thisPos.y + TILE_SIZE);
    }
}
