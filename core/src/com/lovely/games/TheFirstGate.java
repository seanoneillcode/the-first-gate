package com.lovely.games;

import static com.lovely.games.Level.TILE_SIZE;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class TheFirstGate extends ApplicationAdapter {
    private static final float PLAYER_SPEED = 32f * 6.0f;
    private SpriteBatch batch;
    private Texture img;
    private OrthogonalTiledMapRenderer mapRenderer;
    private AssetManager assetManager;
    private OrthographicCamera camera;

    private Level currentLevel;
    private Vector2 playerPos;
    private boolean isMoving;
    private Vector2 moveVector;
    private Vector2 inputVector;
    private float movementValue;
    private String lastConnection;
    private List<Level> levels;
    private String newConnectionTo;

	@Override
	public void create () {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("tower-01.tmx", TiledMap.class);
        assetManager.load("tower-02.tmx", TiledMap.class);
        assetManager.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 320);

		batch = new SpriteBatch();
		img = new Texture("wizard.png");

        levels = new ArrayList<Level>();
        levels.add(Level.loadLevel(assetManager, "tower-01.tmx"));
        levels.add(Level.loadLevel(assetManager, "tower-02.tmx"));

        newConnectionTo = "01";

        // special
        startLevel(levels.get(0), "01");
	}

	private void loadLevel(Level level) {
        TiledMap map = assetManager.get(level.name);
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        mapRenderer.setView(camera);
    }

    private void startLevel(Level level, String startConnection) {
        loadLevel(level);
        currentLevel = level;
        Vector2 startPos = level.getConnectionPosition(startConnection);
        playerPos = startPos;
        isMoving = false;
        inputVector = new Vector2();
        moveVector = new Vector2();
        movementValue = 0;
        lastConnection = startConnection;
    }

	@Override
	public void render () {
	    getInput();
	    update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
		batch.begin();
		batch.draw(img, playerPos.x, playerPos.y + 12);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		assetManager.dispose();
	}

	private void update() {
        if (isMoving) {
            float movementDelta = Gdx.graphics.getDeltaTime();
            movementValue = movementValue - movementDelta;
            if (movementValue < 0) {
                isMoving = false;
                movementDelta = movementDelta + movementValue;
            }
            Vector2 movement = moveVector.cpy().scl(movementDelta * PLAYER_SPEED);
            playerPos.add(movement);
        }
        if (currentLevel.isDeath(playerPos.cpy().add(8,8))) {
            newConnectionTo = lastConnection;
            startLevel(currentLevel, lastConnection);
        }
        Connection connection = currentLevel.getConnection(playerPos.cpy().add(8,8));
        if (connection == null) {
            newConnectionTo = null;
        }
        if (connection != null && !connection.name.equals(newConnectionTo)) {
            if (connection.to != null) {
                for (Level level : levels) {
                    if (level.hasConnection(connection.to)) {
                        startLevel(level, connection.to);
                        newConnectionTo = connection.to;
                        break;
                    }
                }
            }
        }

    }

	private void getInput() {
        boolean isLeftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean isRightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean isUpPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean isDownPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        if (isLeftPressed) {
            inputVector.x = inputVector.x - 1;
        }
        if (isRightPressed) {

            inputVector.x = inputVector.x + 1;
        }
        if (isUpPressed) {
            inputVector.y = inputVector.y + 1;
        }
        if (isDownPressed) {
            inputVector.y = inputVector.y - 1;
        }

        if (!isMoving && !inputVector.isZero()) {
            moveVector = inputVector.cpy();
            if (currentLevel.canMove(moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(8,8))) {
                isMoving = true;
                movementValue = 32f / PLAYER_SPEED;
            }
        }
        inputVector = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
