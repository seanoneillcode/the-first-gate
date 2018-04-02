package org.lovely.games;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class LoadingManager {

    public static final String PLAYER_IDLE = "player-idle.png";
    public static final String PLAYER_SHADOW = "player-shadow.png";
    public static final String PLAYER_FALL = "player-fall.png";
    public static final String PLAYER_JUMP = "player-jump.png";
    public static final String PLAYER_RUN = "player-run.png";
    public static final String GRASS_TILE = "grass-tile.png";
    public static final String BOTTOM_TILE = "bottom-tile.png";
    public static final String CLOUD_0 = "cloud-0.png";
    public static final String CLOUD_1 = "cloud-1.png";
    public static final String CLOUD_2 = "cloud-2.png";
    public static final String CLOUD_3 = "cloud-3.png";
    public static final String GOAL = "goal.png";
    public static final String LAND_EFFECT = "land-effect.png";
    private Map<String, Animation<TextureRegion>> anims;
    AssetManager assetManager;

    LoadingManager(AssetManager assetManager) {
        anims = new HashMap<>();
        this.assetManager = assetManager;
    }

    public void load() {
        assetManager.load(PLAYER_IDLE, Texture.class);
        assetManager.load(PLAYER_FALL, Texture.class);
        assetManager.load(PLAYER_RUN, Texture.class);
        assetManager.load(PLAYER_JUMP, Texture.class);
        assetManager.load(GRASS_TILE, Texture.class);
        assetManager.load(BOTTOM_TILE, Texture.class);
        assetManager.load(PLAYER_SHADOW, Texture.class);
        assetManager.load(LAND_EFFECT, Texture.class);
        assetManager.load(CLOUD_0, Texture.class);
        assetManager.load(CLOUD_1, Texture.class);
        assetManager.load(CLOUD_2, Texture.class);
        assetManager.load(CLOUD_3, Texture.class);
        assetManager.load(GOAL, Texture.class);

        assetManager.finishLoading();

        anims.put(PLAYER_IDLE, loadAnimation(PLAYER_IDLE, 4, 0.25f));
        anims.put(PLAYER_FALL, loadAnimation(PLAYER_FALL, 6, 0.1f));
        anims.put(PLAYER_RUN, loadAnimation(PLAYER_RUN, 4, 0.15f));
        anims.put(PLAYER_JUMP, loadAnimation(PLAYER_JUMP, 2, 0.25f));
        anims.put(GOAL, loadAnimation(GOAL, 2, 0.25f));
        anims.put(GRASS_TILE, loadAnimation(GRASS_TILE, 4, 0.5f));
        anims.put(BOTTOM_TILE, loadAnimation(BOTTOM_TILE, 1, 1f));
        anims.put(PLAYER_SHADOW, loadAnimation(PLAYER_SHADOW, 1, 1f));
        anims.put(CLOUD_0, loadAnimation(CLOUD_0, 1, 1f));
        anims.put(CLOUD_1, loadAnimation(CLOUD_1, 1, 1f));
        anims.put(CLOUD_2, loadAnimation(CLOUD_2, 1, 1f));
        anims.put(CLOUD_3, loadAnimation(CLOUD_3, 1, 1f));
        anims.put(LAND_EFFECT, loadAnimation(LAND_EFFECT, 6, 0.05f));
    }

    public Animation<TextureRegion> getAnim(String name) {
        return anims.get(name);
    }

    private Animation<TextureRegion> loadAnimation(String fileName, int numberOfFrames, float frameDelay) {
        Texture sheet = assetManager.get(fileName);
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / numberOfFrames, sheet.getHeight());
        Array<TextureRegion> frames = new Array<>(numberOfFrames);
        for (int i = 0; i < numberOfFrames; i++) {
            frames.add(tmp[0][i]);
        }
        return new Animation<>(frameDelay, frames);
    }
}
