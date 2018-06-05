package com.lovely.games;

import static com.lovely.games.TheFirstGate.DEFAULT_SOUND_LEVEL;
import static com.lovely.games.TheFirstGate.RANDOM_SOUND_ID_RANGE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SoundPlayer {

    public static final float VOLUME_RANGE = 240.0f;
    AssetManager assetManager;
    Vector2 playerPos;
    private float soundVolume;
    private float musicVolume;

    Map<Integer, PositionSound> sounds;
    boolean isPaused;

    SoundPlayer(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.sounds = new HashMap<>();
        this.isPaused = false;
        this.soundVolume = DEFAULT_SOUND_LEVEL;
        this.musicVolume = DEFAULT_SOUND_LEVEL;
    }

    public void startLevel() {
        sounds.forEach((integer, positionSound) -> {
            positionSound.sound.stop();
        });
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
                PositionSound positionSound = sounds.get(id);
                if (!positionSound.isMusic) {
                    positionSound.sound.setVolume(getVolume(playerPos, positionSound.pos));
                } else {
                    positionSound.sound.setVolume(getMusicVolume());
                }
            }
        }
        Iterator<Integer> iterator = removes.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            PositionSound ps = sounds.remove(i);
            ps.sound.dispose();
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

    public void playSound(String name) {
        int id = MathUtils.random(RANDOM_SOUND_ID_RANGE);
        Music sound = Gdx.audio.newMusic(Gdx.files.internal(name));
        sounds.put(id, new PositionSound(sound, playerPos, false));
        sound.setVolume(getVolume(playerPos, playerPos));
        sound.play();
    }


    public void playSound(String name, Vector2 pos) {
        int id = MathUtils.random(RANDOM_SOUND_ID_RANGE);
        Music sound = Gdx.audio.newMusic(Gdx.files.internal(name));
        sounds.put(id, new PositionSound(sound, pos, false));
        sound.setVolume(getVolume(playerPos, pos));
        sound.play();
    }

    public void playMusic(int id, String name, boolean isLooping) {
        if (!sounds.containsKey(id)) {
            Music sound = Gdx.audio.newMusic(Gdx.files.internal(name));
            sounds.put(id, new PositionSound(sound, new Vector2(), true));
        }
        Music sound = sounds.get(id).sound;
        if (sound.isPlaying()) {
            return;
        }
        sound.setVolume(getMusicVolume());
        sound.play();
        sound.setLooping(isLooping);
    }

    public void playSound(int id, String name, Vector2 pos, boolean isLooping) {
        if (!sounds.containsKey(id)) {
            Music sound = Gdx.audio.newMusic(Gdx.files.internal(name));
            sounds.put(id, new PositionSound(sound, pos, false));
        }
        Music sound = sounds.get(id).sound;
        if (sound.isPlaying()) {
            return;
        }
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

    public void dispose() {
        for (Integer id : sounds.keySet()) {
            sounds.get(id).sound.dispose();
        }
    }

    public void setSoundVolume(float volume) {
        soundVolume = volume;
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;
    }

}
