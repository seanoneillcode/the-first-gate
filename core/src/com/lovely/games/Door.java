package com.lovely.games;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Door implements Switchable {

    Vector2 pos;
    boolean isOpen;
    String switchId;
    Color color;

    public Door(Vector2 pos, boolean isOpen, String switchId) {
        this.pos = pos;
        this.isOpen = isOpen;
        this.switchId = switchId;
        this.color = new Color(random(0.2f, 0.4f), random(0.2f, 0.4f), random(0.8f, 1.0f), 1.0f);
    }

    @Override
    public void handleMessage(String id) {
        System.out.println("got message");
        if (switchId != null && switchId.equals(id)) {

            isOpen = !isOpen;
            System.out.println("door isopen " + isOpen);
        }
    }
}
