package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Particle {

    Vector2 pos;
    String image;
    float lifeTimer;
    float totalLife;
    Vector2 mov;
    Color targetColor;
    Color color;
    float colorStep;

    public void update() {
        lifeTimer = lifeTimer + Gdx.graphics.getDeltaTime();
        pos.add(mov);
        color = color.lerp(targetColor, colorStep);
    }

    public boolean isAlive() {
        return lifeTimer < totalLife;
    }
}
