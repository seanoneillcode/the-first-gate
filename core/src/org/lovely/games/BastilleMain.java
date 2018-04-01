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

import static org.lovely.games.LoadingManager.PLAYER_IDLE;

public class BastilleMain extends ApplicationAdapter {

    SpriteBatch batch;
    Texture img;

    private CameraManager cameraManager;
    private InputManager inputManager;
    private float PLAYER_SPEED = 1.0f;
    private AssetManager assetManager;
    private LoadingManager loadingManager;
    private LevelManager levelManager;
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
		player = new Ent(new Vector2(), new Vector2(16, 16));
		levelManager.start();
	}

	@Override
	public void render () {
        inputManager.update(this);
        cameraManager.update(player.pos, inputManager.getInput());
        levelManager.update();
		Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraManager.camera.combined);
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
		batch.begin();
		levelManager.draw(batch, loadingManager);
        drawPlayer(player.pos);
		batch.end();
	}

	private void drawPlayer(Vector2 playerPos) {
        TextureRegion torchFrame = loadingManager.getAnim(PLAYER_IDLE).getKeyFrame(animationDelta, true);
        batch.draw(torchFrame, playerPos.x, playerPos.y);
    }

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

    public void movePlayer(Vector2 inputVector) {
        Vector2 change = inputVector.cpy().scl(PLAYER_SPEED);
        player.pos.add(change);
    }
}
