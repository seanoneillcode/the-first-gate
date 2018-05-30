package com.lovely.games;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

public class PositionSound {

    Music sound;
    Vector2 pos;

    public PositionSound(Music sound, Vector2 pos) {
        this.sound = sound;
        this.pos = pos;
    }
}
