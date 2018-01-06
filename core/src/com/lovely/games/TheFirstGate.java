package com.lovely.games;

import static com.lovely.games.Level.TILE_SIZE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TheFirstGate extends ApplicationAdapter {
    private static final float PLAYER_SPEED = 32f * 4.0f;
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
    private List<Arrow> arrows;
    private Texture arrowImage;
    private Texture platformImg;
    private Platform currentPlatform;

	@Override
	public void create () {
        assetManager = new AssetManager();
        FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.load("tower-01.tmx", TiledMap.class);
        assetManager.load("tower-02.tmx", TiledMap.class);
        assetManager.load("tower-arrow-01.tmx", TiledMap.class);
        assetManager.load("tower-arrow-02.tmx", TiledMap.class);
        assetManager.load("tower-arrow-03.tmx", TiledMap.class);
        assetManager.load("tower-arrow-04.tmx", TiledMap.class);
        assetManager.load("tower-platform-01.tmx", TiledMap.class);
        assetManager.load("tower-platform-02.tmx", TiledMap.class);
        assetManager.load("tower-platform-03.tmx", TiledMap.class);
        assetManager.load("tower-platform-04.tmx", TiledMap.class);
        assetManager.load("arrow.png", Texture.class);
        assetManager.load("platform.png", Texture.class);
        assetManager.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 320);

		batch = new SpriteBatch();
		img = new Texture("wizard.png");
		arrowImage = assetManager.get("arrow.png");
        platformImg = assetManager.get("platform.png");

        levels = new ArrayList<>();
        levels.add(Level.loadLevel(assetManager, "tower-01.tmx")); // 01
        levels.add(Level.loadLevel(assetManager, "tower-02.tmx"));
        levels.add(Level.loadLevel(assetManager, "tower-arrow-01.tmx")); // 05
        levels.add(Level.loadLevel(assetManager, "tower-arrow-02.tmx")); // 07
        levels.add(Level.loadLevel(assetManager, "tower-arrow-03.tmx")); // 09
        levels.add(Level.loadLevel(assetManager, "tower-arrow-04.tmx")); // 09
        levels.add(Level.loadLevel(assetManager, "tower-platform-01.tmx")); // 13
        levels.add(Level.loadLevel(assetManager, "tower-platform-02.tmx")); // 15
        levels.add(Level.loadLevel(assetManager, "tower-platform-03.tmx")); // 17
        levels.add(Level.loadLevel(assetManager, "tower-platform-04.tmx")); // 19

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
        currentPlatform = null;
        loadLevel(level);
        currentLevel = level;
        Vector2 startPos = level.getConnectionPosition(startConnection);
        playerPos = startPos;
        isMoving = false;
        inputVector = new Vector2();
        moveVector = new Vector2();
        arrows = new ArrayList<>();
        movementValue = 0;
        lastConnection = startConnection;
        for (ArrowSource arrowSource : currentLevel.getArrowSources()) {
            arrowSource.start();
        }
        for (Platform platform : currentLevel.getPlatforms()) {
            platform.start();
        }
    }

	@Override
	public void render () {
	    getInput();
	    update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
		batch.begin();
        for (Platform platform : currentLevel.getPlatforms()) {
            batch.draw(platformImg, platform.pos.x, platform.pos.y);
        }
		for (Arrow arrow : arrows) {
		    arrow.draw(batch);
        }
        batch.draw(img, playerPos.x, playerPos.y + 8);
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
            if (currentPlatform != null) {
                playerPos.add(currentPlatform.getMovement());
            }
        }
        if (!isMoving) {
            Platform platform = currentLevel.getPlatform(playerPos);
            if (platform != null) {
                currentPlatform = platform;
            } else {
                if (currentPlatform != null) {

                }
                currentPlatform = null;
            }
            if (currentPlatform == null && currentLevel.isDeath(playerPos.cpy().add(16,16))) {
                newConnectionTo = lastConnection;
                startLevel(currentLevel, lastConnection);
            }
            playerPos.x = MathUtils.round(playerPos.x / TILE_SIZE) * TILE_SIZE;
            playerPos.y = MathUtils.round(playerPos.y / TILE_SIZE) * TILE_SIZE;

        }

        if (currentPlatform != null && !isMoving) {
            playerPos = currentPlatform.pos.cpy();
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

        for (ArrowSource arrowSource : currentLevel.getArrowSources()) {
            arrowSource.update(this);
        }

        Iterator<Arrow> arrowIterator = arrows.iterator();
        while(arrowIterator.hasNext()) {
            Arrow arrow = arrowIterator.next();
            arrow.update(this);
            if (currentLevel.isWall(arrow.pos) || currentLevel.isOutOfBounds(arrow.pos)) {
                arrowIterator.remove();
            }
            if (getPlayerRect().overlaps(arrow.getRect())) {
                newConnectionTo = lastConnection;
                startLevel(currentLevel, lastConnection);
            }
        }

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.update();
        }
    }

    private Rectangle getPlayerRect() {
	    float buffer = TILE_SIZE * 0.2f;
	    float playerSize = TILE_SIZE - buffer - buffer;
	    return new Rectangle(playerPos.x + buffer, playerPos.y + buffer, playerSize, playerSize);
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
            if (!currentLevel.isWall(moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(8,8))) {
                isMoving = true;
                movementValue = 32f / PLAYER_SPEED;
            }
        }
        inputVector = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    void addArrow(Vector2 pos, Vector2 dir) {
	    System.out.println("adding arrow");
        arrows.add(new Arrow(arrowImage, pos, dir));
    }
}
