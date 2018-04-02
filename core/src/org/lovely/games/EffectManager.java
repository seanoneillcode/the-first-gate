package org.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    List<Effect> effects;

    EffectManager() {
        effects = new ArrayList<>();
    }

    void update() {
        for (Effect effect : effects) {
            effect.timer = effect.timer - Gdx.graphics.getDeltaTime();
            effect.anim = effect.anim + Gdx.graphics.getDeltaTime();
        }
        effects.removeIf(effect -> effect.timer < 0);
    }

    public void addEffect(Vector2 pos, String image, float timer) {
        effects.add(new Effect(pos, timer, image));
    }

    public void start() {
        effects.clear();
    }
}
