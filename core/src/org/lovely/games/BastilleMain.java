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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static org.lovely.games.LoadingManager.PLAYER_FALL;
import static org.lovely.games.LoadingManager.PLAYER_IDLE;
import static org.lovely.games.LoadingManager.PLAYER_RUN;

public class BastilleMain extends ApplicationAdapter {

    SpriteBatch batch;

    private CameraManager cameraManager;
    private InputManager inputManager;
    private float PLAYER_SPEED = 1.5f;
    private AssetManager assetManager;
    private LoadingManager loadingManager;
    private LevelManager levelManager;
    private EntManager entManager;
    private float animationDelta = 0f;
    Color background = new Color(65 / 256f, 115 / 256f, 145 / 256f, 1);
    Ent player;

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
		inputManager = new InputManager();
		entManager = new EntManager();
		levelManager.start();
		Vector2 startPos = levelManager.tiles.get(levelManager.tiles.size() - 1).pos.cpy();
        player = entManager.addEnt(startPos, new Vector2(8, 8), new Vector2(4, 4), true);
	}

	@Override
	public void render () {
        inputManager.update(this);
        cameraManager.update(player.pos, inputManager.getInput());
        levelManager.update();
        entManager.update(levelManager);
		Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraManager.camera.combined);
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
		batch.begin();
        drawLevel(levelManager);
        drawEnts(entManager);
		batch.end();
	}

    private void drawEnts(EntManager entManager) {
        for (Ent ent : entManager.ents) {
            if (ent.state == Ent.EntState.FALLING) {
                TextureRegion torchFrame = loadingManager.getAnim(PLAYER_FALL).getKeyFrame(ent.delta, false);
                batch.draw(torchFrame, ent.pos.x, ent.pos.y);
            }
            if (ent.state == Ent.EntState.ALIVE) {
                if (inputManager.isMoving()) {
                    TextureRegion torchFrame = loadingManager.getAnim(PLAYER_RUN).getKeyFrame(ent.delta, true);
                    batch.draw(torchFrame, ent.pos.x, ent.pos.y);
                } else {
                    TextureRegion torchFrame = loadingManager.getAnim(PLAYER_IDLE).getKeyFrame(ent.delta, true);
                    batch.draw(torchFrame, ent.pos.x, ent.pos.y);
                }
            }
        }
    }

    private void drawLevel(LevelManager levelManager) {
        for (Tile tile : levelManager.tiles) {
            TextureRegion frame = loadingManager.getAnim(tile.image).getKeyFrame(animationDelta, true);
            batch.draw(frame, tile.pos.x, tile.pos.y);
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
    }
}
