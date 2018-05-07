package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class StonePrizeScene {

    Sprite player;
    Sprite light;
    Sprite stone;

    float timer;
    private Vector2 playerPos, stonePos;
    private float lightAlpha;
    private boolean isIncrease;
    private float amount;

    StonePrizeScene(AssetManager assetManager) {
        player = new Sprite((Texture)assetManager.get("posters/stone-0.png"));
        player.setScale(2.0f);
        stone = new Sprite((Texture)assetManager.get("posters/stone-2.png"));
        stone.setScale(2.0f);
        light = new Sprite((Texture)assetManager.get("posters/stone-1.png"));
        light.setScale(2.0f);
    }

    void reset() {
        timer = 0;
        System.out.println("resetting scene");
        playerPos = new Vector2(-134f, -24f);
        stonePos = new Vector2(96f, -24f);
        lightAlpha = 0;
        amount = 0.02f;
        isIncrease = true;
    }

    void update(Stage stage) {
        timer = timer + Gdx.graphics.getDeltaTime();
        if (timer < 1.0f) {
            playerPos.x = playerPos.x + 2.2f;
            playerPos.y = playerPos.y + 0.3f;

            stonePos.x = stonePos.x - 1.6f;
            stonePos.y = stonePos.y + 0.3f;
        }
        if (timer > 1f ) {
            if (isIncrease) {
                lightAlpha = lightAlpha + amount;
                if (lightAlpha > 1.0f) {
                    lightAlpha = 1.0f;
                    isIncrease = false;
                    amount = amount + 0.01f;
                }
            } else {
                lightAlpha = lightAlpha - amount;
                if (lightAlpha < 0f) {
                    lightAlpha = 0f;
                    isIncrease = true;
                    amount = amount + 0.01f;
                }
            }
        }
        lightAlpha = MathUtils.clamp(lightAlpha, 0, 1.0f);
    }

    void render(SpriteBatch batch, Vector2 pos) {
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        player.setTexture(assetManager.get(posterImageName));
//        player.setAlpha(posterAlpha);
        player.setPosition(pos.x + playerPos.x, pos.y + playerPos.y);
        player.draw(batch);
        light.setPosition(pos.x + stonePos.x - 150,pos.y + stonePos.y);
        light.setAlpha(lightAlpha);
        light.draw(batch);
        stone.setPosition(pos.x + stonePos.x,pos.y + stonePos.y);
        stone.draw(batch);
    }
}
