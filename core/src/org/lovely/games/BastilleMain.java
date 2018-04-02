package org.lovely.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static org.lovely.games.LevelManager.TILE_SIZE;
import static org.lovely.games.LoadingManager.*;

public class BastilleMain extends ApplicationAdapter {

    SpriteBatch batch;

    private CameraManager cameraManager;
    private InputManager inputManager;
    private float PLAYER_SPEED = 1.5f;
    private AssetManager assetManager;
    private LoadingManager loadingManager;
    private LevelManager levelManager;
    private EffectManager effectManager;
    private EntManager entManager;
    private float animationDelta = 0f;
    Color background = new Color(0 / 256f, 149 / 256f, 233 / 256f, 1);
    Ent player;
    private List<Cloud> cloudShadows;

    @Override
	public void create () {
		batch = new SpriteBatch();
        assetManager = new AssetManager();
        FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        loadingManager = new LoadingManager(assetManager);
        loadingManager.load();
        levelManager = new LevelManager();
		cameraManager = new CameraManager();
		effectManager = new EffectManager();
		inputManager = new InputManager();
		entManager = new EntManager();
		levelManager.start();
		Vector2 startPos = levelManager.getStartPos();
        player = entManager.addEnt(startPos, new Vector2(8, 8), new Vector2(0, 0), true);
	}

	@Override
	public void render () {
        inputManager.update(this);
        cameraManager.update(player.pos, inputManager.getInput());
        levelManager.update();
        entManager.update(levelManager, this, effectManager);
        effectManager.update();
		Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraManager.camera.combined);
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
		batch.begin();
        drawLevel(levelManager);
        drawEffects(effectManager);
        drawEnts(entManager);
        drawCloudShadows();
		batch.end();
	}

    private void drawEffects(EffectManager effectManager) {
        Sprite effectSprite = new Sprite();
        for (Effect effect : effectManager.effects) {
            TextureRegion frame = loadingManager.getAnim(effect.image).getKeyFrame(effect.anim, true);
            effectSprite.setRegion(frame);
            effectSprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());
            effectSprite.setPosition(effect.pos.x, effect.pos.y);
            effectSprite.draw(batch);
        }
    }

    private void drawEnts(EntManager entManager) {
        Sprite entSprite = new Sprite();
        entSprite.setScale(2);
        for (Ent ent : entManager.ents) {
            entSprite.setSize(ent.size.x, ent.size.y);
            float actualy = ent.pos.y + ent.z;
            entSprite.setPosition(ent.pos.x, actualy);

            if (ent.state != Ent.EntState.DEAD) {
                TextureRegion frame = null;
                if (ent.state == Ent.EntState.FALLING) {
                    frame = loadingManager.getAnim(PLAYER_FALL).getKeyFrame(ent.delta, false);
                }
                if (ent.state == Ent.EntState.JUMPING) {
                    frame = loadingManager.getAnim(PLAYER_JUMP).getKeyFrame(ent.delta, true);
                }
                if (ent.state == Ent.EntState.ALIVE) {
                    if (inputManager.isMoving()) {
                        frame = loadingManager.getAnim(PLAYER_RUN).getKeyFrame(ent.delta, true);
                    } else {
                        frame = loadingManager.getAnim(PLAYER_IDLE).getKeyFrame(ent.delta, true);
                    }
                }
                entSprite.setRegion(frame);
                if (!inputManager.isRight) {
                    entSprite.flip(true, false);
                }
                if (ent.state != Ent.EntState.DEAD && ent.state != Ent.EntState.FALLING) {
                    batch.draw(loadingManager.getAnim(PLAYER_SHADOW).getKeyFrame(ent.delta, false), ent.pos.x, ent.pos.y - 4);
                }
                entSprite.draw(batch);
            }
        }
    }

    private void drawCloudShadows() {
        Sprite cloudSprite = new Sprite();
        cloudSprite.setSize(128, 128);
        for (Cloud cloud : cloudShadows) {
            cloudSprite.setPosition(cloud.pos.x, cloud.pos.y);
            TextureRegion frame = loadingManager.getAnim(cloud.img).getKeyFrame(animationDelta, true);
            cloudSprite.setRegion(frame);
            cloudSprite.draw(batch);
        }
    }

    private void drawLevel(LevelManager levelManager) {
        cloudShadows = new ArrayList<>();
        Sprite cloudSprite = new Sprite();
        cloudSprite.setSize(128, 128);
        for (Cloud cloud : levelManager.clouds) {
            if (cloud.img == CLOUD_3) {
                cloudShadows.add(cloud);
                continue;
            }
            cloudSprite.setPosition(cloud.pos.x, cloud.pos.y);
            TextureRegion frame = loadingManager.getAnim(cloud.img).getKeyFrame(animationDelta, true);
            cloudSprite.setRegion(frame);
            cloudSprite.setScale(cloud.scale);
            cloudSprite.draw(batch);
        }

        Sprite tileSprite = new Sprite();
        tileSprite.setSize(TILE_SIZE, TILE_SIZE);
        for (Tile tile : levelManager.tiles) {
            TextureRegion frame = loadingManager.getAnim(tile.image).getKeyFrame(animationDelta, true);
            tileSprite.setPosition(tile.pos.x, tile.pos.y);
            tileSprite.setRegion(frame);
            tileSprite.setColor(tile.color);
            tileSprite.draw(batch);
        }
    }

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}

    public void movePlayer(Vector2 inputVector) {
        if (player.state == Ent.EntState.ALIVE) {
            Vector2 change = inputVector.cpy().scl(PLAYER_SPEED);
            player.pos.add(change);
        }
        if (player.state == Ent.EntState.JUMPING) {
            Vector2 change = inputVector.cpy().scl(PLAYER_SPEED).scl(0.8f);
            player.pos.add(change);
        }
    }

    public void jumpPlayer() {
        player.jump();
    }
}
