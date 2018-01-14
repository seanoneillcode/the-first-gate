package com.lovely.games;

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
import com.badlogic.gdx.math.Vector3;

public class TheFirstGate extends ApplicationAdapter {

    static final float TILE_SIZE = 32f;

    private static final float HALF_TILE_SIZE = 16f;
    private static final float QUARTER_TILE_SIZE = 8f;
    private static final float PLAYER_SPEED = TILE_SIZE * 4.0f;
    private static final float CAMERA_MARGIN = 0.5f;
    private static final float CAMERA_CATCHUP_SPEED = 2.0f;
    public static final int VIEWPORT_WIDTH = 480;
    public static final int VIEWPORT_HEIGHT = 320;

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
    private Texture blockImage;
    private Texture groundBlockImage;
    private Texture pressureImage;
    private Platform currentPlatform;
    private Texture doorImage;
    private Texture openDoorImage;

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
        assetManager.load("tower-block-01.tmx", TiledMap.class);
        assetManager.load("tower-block-02.tmx", TiledMap.class);
        assetManager.load("tower-block-03.tmx", TiledMap.class);
        assetManager.load("tower-block-04.tmx", TiledMap.class);
        assetManager.load("tower-switch-01.tmx", TiledMap.class);
        assetManager.load("tower-switch-02.tmx", TiledMap.class);
        assetManager.load("tower-switch-03.tmx", TiledMap.class);
        assetManager.load("tower-arrow-05.tmx", TiledMap.class);
        assetManager.load("tower-switch-04.tmx", TiledMap.class);
        assetManager.load("tower-switch-05.tmx", TiledMap.class);

        assetManager.load("arrow.png", Texture.class);
        assetManager.load("platform.png", Texture.class);
        assetManager.load("block.png", Texture.class);
        assetManager.load("pressure.png", Texture.class);
        assetManager.load("ground-block.png", Texture.class);
        assetManager.load("door.png", Texture.class);
        assetManager.load("open-door.png", Texture.class);

        assetManager.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		batch = new SpriteBatch();

		img = new Texture("wizard.png");
		arrowImage = assetManager.get("arrow.png");
        platformImg = assetManager.get("platform.png");
        blockImage = assetManager.get("block.png");
        pressureImage = assetManager.get("pressure.png");
        groundBlockImage = assetManager.get("ground-block.png");
        doorImage = assetManager.get("door.png");
        openDoorImage = assetManager.get("open-door.png");

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
        levels.add(Level.loadLevel(assetManager, "tower-block-01.tmx")); // 21 // 10
        levels.add(Level.loadLevel(assetManager, "tower-block-02.tmx")); // 23
        levels.add(Level.loadLevel(assetManager, "tower-block-03.tmx")); // 25
        levels.add(Level.loadLevel(assetManager, "tower-block-04.tmx")); // 27
        levels.add(Level.loadLevel(assetManager, "tower-switch-01.tmx")); // 29
        levels.add(Level.loadLevel(assetManager, "tower-switch-02.tmx")); // 31
        levels.add(Level.loadLevel(assetManager, "tower-switch-03.tmx")); // 33
        levels.add(Level.loadLevel(assetManager, "tower-arrow-05.tmx")); // 35
        levels.add(Level.loadLevel(assetManager, "tower-switch-04.tmx")); // 37
        levels.add(Level.loadLevel(assetManager, "tower-switch-05.tmx")); // 39

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
        playerPos = level.getConnectionPosition(startConnection);
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
        for (Block block : currentLevel.blocks) {
            block.start();
        }
        for (PressureTile pressureTile : currentLevel.pressureTiles) {
            pressureTile.start();
        }
    }

    private Vector3 getCameraPosition() {
        Vector2 pos = playerPos.cpy();
        Vector3 target = new Vector3(pos.x, pos.y, 0);
        final float speed = CAMERA_CATCHUP_SPEED * Gdx.graphics.getDeltaTime();
        float ispeed = 1.0f - speed;
        Vector3 cameraPosition = camera.position.cpy();
        cameraPosition.scl(ispeed);
        target.scl(speed);
        cameraPosition.add(target);
        if (Math.abs(cameraPosition.x - pos.x) < CAMERA_MARGIN) {
            cameraPosition.x = pos.x;
        }
        if (Math.abs(cameraPosition.y - pos.y) < CAMERA_MARGIN) {
            cameraPosition.y = pos.y;
        }
        return cameraPosition;
    }

	@Override
	public void render () {
        camera.position.set(getCameraPosition());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
	    getInput();
	    update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
		batch.begin();

        for (PressureTile pressureTile : currentLevel.pressureTiles) {
            batch.draw(pressureImage, pressureTile.pos.x, pressureTile.pos.y);
        }
        for (Platform platform : currentLevel.getPlatforms()) {
            batch.draw(platformImg, platform.pos.x, platform.pos.y);
        }
		for (Arrow arrow : arrows) {
		    arrow.draw(batch);
        }
        for (Block block : currentLevel.blocks) {
            if (block.isGround) {
                batch.draw(groundBlockImage, block.pos.x, block.pos.y);
            }
            if (block.pos.y > playerPos.y && !block.isGround) {
                batch.draw(blockImage, block.pos.x, block.pos.y);
            }
        }
        for (Door door : currentLevel.doors) {
            if (door.isOpen && door.pos.y >= playerPos.y) {
                batch.draw(openDoorImage, door.pos.x, door.pos.y);
            }
            if (!door.isOpen) {
                batch.draw(doorImage, door.pos.x, door.pos.y);
            }
        }
        batch.draw(img, playerPos.x, playerPos.y + QUARTER_TILE_SIZE);
        for (Block block : currentLevel.blocks) {
            if (block.pos.y <= playerPos.y && !block.isGround) {
                batch.draw(blockImage, block.pos.x, block.pos.y);
            }
        }
        for (Door door : currentLevel.doors) {
            if (door.pos.y < playerPos.y && door.isOpen) {
                batch.draw(openDoorImage, door.pos.x, door.pos.y);
            }
        }
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
        for (PressureTile pressureTile : currentLevel.pressureTiles) {
            boolean handled = false;
            if (playerPos.dst2(pressureTile.pos) < 64) {
                pressureTile.handleAction();
                handled = true;
            }
            for (Block block : currentLevel.blocks) {
                if (block.pos.dst2(pressureTile.pos) < 64) {
                    pressureTile.handleAction();
                    handled = true;
                }
            }
            if (!handled) {
                pressureTile.handlePressureOff();
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
            if (currentPlatform == null && currentLevel.isDeath(playerPos.cpy().add(HALF_TILE_SIZE,HALF_TILE_SIZE))) {
                Block block = currentLevel.getBlock(playerPos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE), false);
                if (!(block != null && block.isGround)) {
                    restartLevel();
                }
            }
            playerPos.x = MathUtils.round(playerPos.x / TILE_SIZE) * TILE_SIZE;
            playerPos.y = MathUtils.round(playerPos.y / TILE_SIZE) * TILE_SIZE;

        }

        if (currentPlatform != null && !isMoving) {
            playerPos = currentPlatform.pos.cpy();
        }

        Connection connection = currentLevel.getConnection(playerPos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE));
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
                restartLevel();
            }
        }

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.update();
        }

        for (Block block : currentLevel.blocks) {
            block.update();
            if (!block.isMoving && !block.isGround) {
                block.pos.x = MathUtils.round(block.pos.x / TILE_SIZE) * TILE_SIZE;
                block.pos.y = MathUtils.round(block.pos.y / TILE_SIZE) * TILE_SIZE;
                if (currentLevel.isDeath(block.pos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE))) {
                    boolean alreadyGround = false;
                    for (Block otherBlock : currentLevel.blocks) {
                        if (otherBlock.isGround) {
                            if (block.pos.dst2(otherBlock.pos) < 64) {
                                alreadyGround = true;
                            }
                        }
                    }
                    if (!alreadyGround) {
                        block.isGround = true;
                    }
                }
            }
        }
    }

    private void restartLevel() {
        newConnectionTo = lastConnection;
        startLevel(currentLevel, lastConnection);
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
            boolean blocked = false;
            moveVector = inputVector.cpy();
            Vector2 nextTilePos = moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE);
            if (currentLevel.isTileBlocked(nextTilePos)) {
                Block block = currentLevel.getBlock(nextTilePos, true);
                if (block == null) {
                    blocked = true;
                } else {
                    Vector2 nextTileAgain = moveVector.cpy().scl(TILE_SIZE * 2.0f).add(playerPos).add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE);
                    if (currentLevel.isTileBlocked(nextTileAgain)) {
                        blocked = true;
                    } else {
                        block.move(moveVector);
                    }
                }
            }
            if (!blocked) {
                isMoving = true;
                movementValue = TILE_SIZE / PLAYER_SPEED;
            }
        }
        inputVector = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restartLevel();
        }
    }

    void addArrow(Vector2 pos, Vector2 dir) {
        arrows.add(new Arrow(arrowImage, pos, dir));
    }
}
