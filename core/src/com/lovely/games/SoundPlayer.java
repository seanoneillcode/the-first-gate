package com.lovely.games;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SoundPlayer {

    public static final float VOLUME_RANGE = 240.0f;
    AssetManager assetManager;
    Vector2 playerPos;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.8f;


    SoundPlayer(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void update(Vector2 playerPos) {
        this.playerPos = playerPos.cpy();
    }

    public void playSound(String name, boolean loop, Vector2 pos) {
        playSound(name, pos, loop, 1.0f);
    }

    public void stopSound(String name) {
        Sound sound = assetManager.get(name);
        sound.stop();
    }

    public long playSound(String name, Vector2 pos, boolean loop, float pitch) {
        Sound sound = assetManager.get(name);
        float dist = pos.dst(playerPos);
        float volume;
        if (dist > VOLUME_RANGE) {
            volume = 0.1f;
        } else {
            volume = ((VOLUME_RANGE - dist) / VOLUME_RANGE) * soundVolume;
            System.out.println("dist " + dist);
            System.out.println("volume " + volume);
        }
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

    public float getSoundVolume() {
        return soundVolume;
    }

    public void increaseSoundVolume() {
        soundVolume = soundVolume + 0.01f;
        soundVolume = MathUtils.clamp(soundVolume, 0, 1.0f);
    }

    public void decreaseSoundVolume() {
        soundVolume = soundVolume - 0.01f;
        soundVolume = MathUtils.clamp(soundVolume, 0, 1.0f);
    }

    public void increaseMusicVolume() {
        musicVolume = musicVolume + 0.01f;
        musicVolume = MathUtils.clamp(musicVolume, 0, 1.0f);
    }

    public void decreaseMusicVolume() {
        musicVolume = musicVolume - 0.01f;
        musicVolume = MathUtils.clamp(musicVolume, 0, 1.0f);
    }

    public float getMusicVolume() {
        return musicVolume;
    }
}
