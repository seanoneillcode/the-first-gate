package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

public class Door implements Switchable {

    Vector2 pos;
    boolean isOpen;
    String switchId;

    public Door(Vector2 pos, boolean isOpen, String switchId) {
        this.pos = pos;
        this.isOpen = isOpen;
        this.switchId = switchId;
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
