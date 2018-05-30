package com.lovely.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SoundPlayer {

    public static final float VOLUME_RANGE = 240.0f;
    AssetManager assetManager;
    Vector2 playerPos;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.8f;

    Map<Integer, PositionSound> sounds;
    boolean isPaused;

    SoundPlayer(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.sounds = new HashMap<>();
        this.isPaused = false;
    }

    public void startLevel() {
        sounds.clear();
    }

    public void pauseSounds() {
        isPaused = true;
        for (Integer id : sounds.keySet()) {
            sounds.get(id).sound.pause();
        }
    }

    public void resumeSounds() {
        isPaused = false;
        for (Integer id : sounds.keySet()) {
            sounds.get(id).sound.play();
        }
    }

    public void update(Vector2 playerPos) {
        if (isPaused) {
            return;
        }
        this.playerPos = playerPos.cpy();
        List<Integer> removes = new ArrayList<>();
        for (Integer id : sounds.keySet()) {
            if (!sounds.get(id).sound.isPlaying()) {
                removes.add(id);
            } else {
                sounds.get(id).sound.setVolume(getVolume(playerPos, sounds.get(id).pos));
            }
        }
        Iterator<Integer> iterator = removes.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            sounds.remove(i);
        }
    }

    public boolean isPlaying(int id) {
        if (sounds.containsKey(id)) {
            return sounds.get(id).sound.isPlaying();
        }
        return false;
    }

    public void stopSound(int id) {
        if (sounds.containsKey(id)) {
            Music sound = sounds.get(id).sound;
            sound.stop();
        }
    }

    public void playSound(int id, String name, Vector2 pos, boolean isLooping) {
        if (!sounds.containsKey(id)) {
            Music sound = assetManager.get(name);
            sounds.put(id, new PositionSound(sound, pos));
        }
        Music sound = sounds.get(id).sound;
        sound.setVolume(getVolume(playerPos, pos));
        sound.play();
        sound.setLooping(isLooping);
    }

    private float getVolume(Vector2 playerPos, Vector2 soundPos) {
        float dist = playerPos != null ? soundPos.dst(playerPos) : 0;
        float volume;
        if (dist > VOLUME_RANGE) {
            volume = 0.1f;
        } else {
            volume = ((VOLUME_RANGE - dist) / VOLUME_RANGE) * soundVolume;
        }
        return volume;
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
