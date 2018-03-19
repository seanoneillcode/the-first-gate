package com.lovely.games;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class SoundPlayer {

    AssetManager assetManager;
    Vector2 playerPos;



    SoundPlayer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void update(Vector2 playerPos) {
        this.playerPos = playerPos.cpy();
    }

    public void playSound(String name, boolean loop, float volume) {
        playSound(name, playerPos, loop, volume, 1.0f);
    }

    public long playSound(String name, boolean loop, float volume, float pitch) {
        return playSound(name, playerPos, loop, volume, pitch);
    }

    public void stopSound(String name) {
        Sound sound = assetManager.get(name);
        sound.stop();
    }

    public long playSound(String name, Vector2 pos, boolean loop, float volume, float pitch) {
        Sound sound = assetManager.get(name);
        if (loop) {
            return sound.loop(volume, pitch, 0);
        } else {
            return sound.play(volume, pitch, 0);
        }
    }

    public void updateVolume(long id, String name, Vector2 pos) {
        Sound sound = assetManager.get(name);
        float dst2 = pos.dst2(playerPos);
        sound.setVolume(id, dst2 / (32 * 4) * (32 * 4));
    }
}
