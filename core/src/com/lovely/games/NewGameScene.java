package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class NewGameScene {

    public static final float START_TIME_AMOUNT = 3.0f;
    public static final float MIDDLE_TIME_AMOUNT = 3.0f;
    public static final float END_TIME_AMOUNT = 2f;
    public static final float TOTAL_TIME = (END_TIME_AMOUNT + MIDDLE_TIME_AMOUNT + START_TIME_AMOUNT);

    Animation<TextureRegion> standing;
    Sprite sprite;

    float animTimer;
    private float alpha;
    boolean isDone;
    Vector2 pos;

    public NewGameScene(Animation<TextureRegion> standing) {
        this.standing = standing;
        sprite = new Sprite(standing.getKeyFrame(0));
        sprite.setScale(2.0f);
    }

    public void reset() {
        animTimer = 0;
        sprite.setPosition(0, 0);
        alpha = 0;
        isDone = false;
        pos = new Vector2(0, -40);
    }

    public void update(TheFirstGate theFirstGate) {
        if (isDone) {
            return;
        }
        animTimer = animTimer + Gdx.graphics.getDeltaTime();
        if (animTimer < START_TIME_AMOUNT) {
            alpha = animTimer / START_TIME_AMOUNT;
            pos.y = pos.y + (Gdx.graphics.getDeltaTime() * 10f);
        }
        if (animTimer >= START_TIME_AMOUNT + MIDDLE_TIME_AMOUNT) {
            alpha = END_TIME_AMOUNT - ((animTimer - START_TIME_AMOUNT - MIDDLE_TIME_AMOUNT) / END_TIME_AMOUNT);
            pos.y = pos.y + (Gdx.graphics.getDeltaTime() * 10f);
        }
        if (animTimer > TOTAL_TIME) {
            isDone = true;
            theFirstGate.isPlayingOpeningScene = false;
            theFirstGate.gotoState("new-game");
        }
    }

    public void render(SpriteBatch batch, Vector2 cameraPos) {
        if (!isDone) {
            TextureRegion region = standing.getKeyFrame(animTimer, true);
            sprite.setRegion(region);
            sprite.setPosition(cameraPos.x + pos.x, cameraPos.y + pos.y);
            sprite.setAlpha(alpha);
            sprite.draw(batch);
        }
    }

}
