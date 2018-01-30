package com.lovely.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.lovely.games.scene.DialogVerb;
import com.lovely.games.scene.Scene;
import com.lovely.games.scene.SceneContainer;
import com.lovely.games.scene.SceneSource;

public class TheFirstGate extends ApplicationAdapter implements Stage {

    public static final float TILE_SIZE = 32f;

    private static final float HALF_TILE_SIZE = 16f;
    private static final float QUARTER_TILE_SIZE = 8f;
    private static final float PLAYER_SPEED = TILE_SIZE * 4.0f;
    private static final float CAMERA_MARGIN = 0.5f;
    private static final float CAMERA_CATCHUP_SPEED = 2.0f;
    public static final int VIEWPORT_WIDTH = 600;
    public static final int VIEWPORT_HEIGHT = 480;

    private SpriteBatch batch;
    private SpriteBatch bufferBatch;
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
    private Sprite mask;
    private Sprite lightHole, portrait;
    private Animation<TextureRegion> walkanim;
    private Animation<TextureRegion> lightAnim, playerLightAnim, arrowAnim, torchAnim;
    float animationDelta = 0;
    DialogContainer dialogContainer;
    Conversation conversation;
    boolean dialogLock = false;
    Sprite playerLight, arrowSprite, levelLight;
    boolean isLevelDirty = false;
    Texture bufferLight;
    FrameBuffer buffer;
    OrthographicCamera cam;
    Vector2 cameraTargetPos;
    SceneContainer sceneContainer;
    Scene currentScene;
    DialogVerb activeDialogVerb;
    boolean moveLock, snaplock;
    Map<String, Texture> actorImages;

	@Override
	public void create () {
        assetManager = new AssetManager();
        FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.load("levels/tower-01.tmx", TiledMap.class);
        assetManager.load("levels/tower-02.tmx", TiledMap.class);
        assetManager.load("levels/tower-arrow-01.tmx", TiledMap.class);
        assetManager.load("levels/tower-arrow-02.tmx", TiledMap.class);
        assetManager.load("levels/tower-arrow-03.tmx", TiledMap.class);
        assetManager.load("levels/tower-arrow-04.tmx", TiledMap.class);
        assetManager.load("levels/tower-platform-01.tmx", TiledMap.class);
        assetManager.load("levels/tower-platform-02.tmx", TiledMap.class);
        assetManager.load("levels/tower-platform-03.tmx", TiledMap.class);
        assetManager.load("levels/tower-platform-04.tmx", TiledMap.class);
        assetManager.load("levels/tower-block-01.tmx", TiledMap.class);
        assetManager.load("levels/tower-block-02.tmx", TiledMap.class);
        assetManager.load("levels/tower-block-03.tmx", TiledMap.class);
        assetManager.load("levels/tower-block-04.tmx", TiledMap.class);
        assetManager.load("levels/tower-switch-01.tmx", TiledMap.class);
        assetManager.load("levels/tower-switch-02.tmx", TiledMap.class);
        assetManager.load("levels/tower-switch-03.tmx", TiledMap.class);
        assetManager.load("levels/tower-arrow-05.tmx", TiledMap.class);
        assetManager.load("levels/tower-switch-04.tmx", TiledMap.class);
        assetManager.load("levels/tower-switch-05.tmx", TiledMap.class);
        assetManager.load("levels/start-room.tmx", TiledMap.class);
        assetManager.load("levels/end-room.tmx", TiledMap.class);
        assetManager.load("levels/scene-test.tmx", TiledMap.class);

        assetManager.load("arrow.png", Texture.class);
        assetManager.load("platform.png", Texture.class);
        assetManager.load("block.png", Texture.class);
        assetManager.load("pressure.png", Texture.class);
        assetManager.load("ground-block.png", Texture.class);
        assetManager.load("door.png", Texture.class);
        assetManager.load("open-door.png", Texture.class);
        assetManager.load("wizard-sheet.png", Texture.class);
        assetManager.load("dialog-box.png", Texture.class);
        assetManager.load("light-hole.png", Texture.class);
        assetManager.load("light-magic.png", Texture.class);
        assetManager.load("player-light.png", Texture.class);
        assetManager.load("arrow-sheet.png", Texture.class);
        assetManager.load("level-light.png", Texture.class);
        assetManager.load("torch-sheet.png", Texture.class);
        assetManager.load("portrait-1.png", Texture.class);
        assetManager.load("wizard.png", Texture.class);
        assetManager.finishLoading();

        dialogContainer = new DialogContainer(assetManager.get("dialog-box.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();

		arrowImage = assetManager.get("arrow.png");
        platformImg = assetManager.get("platform.png");
        blockImage = assetManager.get("block.png");
        pressureImage = assetManager.get("pressure.png");
        groundBlockImage = assetManager.get("ground-block.png");
        doorImage = assetManager.get("door.png");
        openDoorImage = assetManager.get("open-door.png");
        lightHole = new Sprite((Texture) assetManager.get("light-hole.png"));
        bufferLight = assetManager.get("light-hole.png");
        lightHole.setScale(6.0f);
        playerLight = new Sprite((Texture) assetManager.get("player-light.png"));
        playerLight.setScale(1.0f, 4.0f);
        levelLight = new Sprite((Texture) assetManager.get("level-light.png"));
        portrait = new Sprite((Texture) assetManager.get("portrait-1.png"));


        buffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, false);
        cam = new OrthographicCamera(buffer.getWidth(), buffer.getHeight());
        cam.position.set(buffer.getWidth() / 2, buffer.getWidth() / 2, 0);
        cam.update();

        levels = new ArrayList<>();
        levels.add(Level.loadLevel(assetManager, "levels/tower-01.tmx")); // 01
        levels.add(Level.loadLevel(assetManager, "levels/tower-02.tmx"));
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-01.tmx")); // 05
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-02.tmx")); // 07
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-03.tmx")); // 09
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-04.tmx")); // 09
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-01.tmx")); // 13
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-02.tmx")); // 15
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-03.tmx")); // 17
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-04.tmx")); // 19
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-01.tmx")); // 21 // 10
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-02.tmx")); // 23
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-03.tmx")); // 25
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-04.tmx")); // 27
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-01.tmx")); // 29
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-02.tmx")); // 31
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-03.tmx")); // 33
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-05.tmx")); // 35
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-04.tmx")); // 37
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-05.tmx")); // 39
        levels.add(Level.loadLevel(assetManager, "levels/start-room.tmx")); // 1 // 20
        levels.add(Level.loadLevel(assetManager, "levels/end-room.tmx")); // 41 // 21
        levels.add(Level.loadLevel(assetManager, "levels/scene-test.tmx")); // 1 // 22

        walkanim = loadAnimation(assetManager.get("wizard-sheet.png"), 4, 0.5f);
        lightAnim = loadAnimation(assetManager.get("light-magic.png"), 4, 0.6f);
        playerLightAnim = loadAnimation(assetManager.get("player-light.png"), 4, 0.5f);
        arrowAnim = loadAnimation(assetManager.get("arrow-sheet.png"), 8, 0.05f);
        torchAnim = loadAnimation(assetManager.get("torch-sheet.png"), 2, 0.5f);
        arrowSprite = new Sprite();
        arrowSprite.setBounds(0,0,32,32);
        actorImages = new HashMap<>();
        actorImages.put("ant", assetManager.get("wizard.png"));



        newConnectionTo = "01";
        moveLock = false;

        sceneContainer = new SceneContainer();

        // special
        startLevel(levels.get(22), "start");
	}

    private Animation<TextureRegion> loadAnimation(Texture sheet, int numberOfFrames, float frameDelay) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / numberOfFrames, sheet.getHeight());
        Array<TextureRegion> frames = new Array<>(numberOfFrames);
        for (int i = 0; i < numberOfFrames; i++) {
            frames.add(tmp[0][i]);
        }
        return new Animation<>(frameDelay, frames);
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
        isLevelDirty = true;
        moveLock = false;
        cameraTargetPos = playerPos;
    }

    private Vector3 getCameraPosition() {
        Vector2 pos = cameraTargetPos;
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
        if (snaplock && pos.dst2(new Vector2(cameraPosition.x, cameraPosition.y)) < 10000) {
            snaplock = false;
        }
        if (!snaplock) {
            float cameraTrailLimit = 100.0f;
            cameraPosition.x = MathUtils.clamp(cameraPosition.x, -cameraTrailLimit + cameraTargetPos.x, cameraTrailLimit + cameraTargetPos.x);
            cameraPosition.y = MathUtils.clamp(cameraPosition.y, -cameraTrailLimit + cameraTargetPos.y, cameraTrailLimit + cameraTargetPos.y);
        }
        return cameraPosition;
    }

    private void renderLightMasks() {
        buffer.begin();
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.15f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bufferBatch.setProjectionMatrix(camera.combined);
        bufferBatch.begin();

        bufferBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);

        Vector2 offset = new Vector2((playerPos.x), (playerPos.y) );
        TextureRegion playerRegion = playerLightAnim.getKeyFrame(animationDelta, true);
        playerLight.setRegion(playerRegion);
        playerLight.setColor(1.0f, 0.8f, 0.5f, 1.0f);
        playerLight.setPosition( offset.x - 60, offset.y);
        playerLight.draw(bufferBatch);


        TextureRegion tr = lightAnim.getKeyFrame(animationDelta, true);
        for (Arrow arrow : arrows) {

            lightHole.setColor(arrow.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((arrow.pos.x), (arrow.pos.y));
            lightHole.draw(bufferBatch);
        }

        for (PressureTile tile : currentLevel.pressureTiles) {
            lightHole.setColor(tile.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((tile.pos.x), (tile.pos.y));
            lightHole.draw(bufferBatch);
        }
        for (Platform platform : currentLevel.platforms) {
            lightHole.setColor(platform.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((platform.pos.x), (platform.pos.y));
            lightHole.draw(bufferBatch);
        }
        for (Block block : currentLevel.blocks) {
            lightHole.setColor(block.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((block.pos.x), (block.pos.y));
            lightHole.draw(bufferBatch);
        }
        for (Door door : currentLevel.doors) {
            lightHole.setColor(door.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((door.pos.x), (door.pos.y));
            lightHole.draw(bufferBatch);
        }
        for (LevelLight light : currentLevel.lights) {
            levelLight.setColor(light.color);
            levelLight.setBounds(light.pos.x, light.pos.y, light.size.x, light.size.y);
            levelLight.draw(bufferBatch);
        }
        for (Torch torch : currentLevel.torches) {
            lightHole.setColor(torch.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((torch.pos.x), (torch.pos.y));
            lightHole.draw(bufferBatch);
        }
        for (Actor actor : currentLevel.actors) {
            playerLight.setRegion(playerRegion);
            playerLight.setColor(1.0f, 0.8f, 0.5f, 1.0f);
            playerLight.setPosition( actor.pos.x - 60, actor.pos.y);
            playerLight.draw(bufferBatch);
        }

        bufferBatch.end();
        buffer.end();
    }

	@Override
	public void render () {
        camera.position.set(getCameraPosition());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
	    getInput();
	    update();
	    animationDelta = animationDelta + Gdx.graphics.getDeltaTime();

	    renderLightMasks();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isLevelDirty) {
            mapRenderer.render();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            for (PressureTile pressureTile : currentLevel.pressureTiles) {
                batch.draw(pressureImage, pressureTile.pos.x, pressureTile.pos.y);
            }
            for (Platform platform : currentLevel.getPlatforms()) {
                batch.draw(platformImg, platform.pos.x, platform.pos.y);
            }
            for (Arrow arrow : arrows) {
                TextureRegion currentFrame = arrowAnim.getKeyFrame(animationDelta, true);
                arrowSprite.setPosition(arrow.pos.x, arrow.pos.y);
                arrowSprite.setRegion(currentFrame);
                arrowSprite.draw(batch);
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
            for (Actor actor : currentLevel.actors) {
                if (actor.pos.y <= playerPos.y) {
                    Texture actorImage = actorImages.get(actor.id);
                    batch.draw(actorImage, actor.pos.x, actor.pos.y + 12);
                }
            }
            TextureRegion currentFrame = walkanim.getKeyFrame(animationDelta, true);
            batch.draw(currentFrame, playerPos.x, playerPos.y + QUARTER_TILE_SIZE);

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
            for (Torch torch : currentLevel.torches) {
                TextureRegion torchFrame = torchAnim.getKeyFrame(animationDelta, true);
                batch.draw(torchFrame, torch.pos.x, torch.pos.y);
            }
            for (Actor actor : currentLevel.actors) {
                if (actor.pos.y > playerPos.y) {
                    Texture actorImage = actorImages.get(actor.id);
                    batch.draw(actorImage, actor.pos.x, actor.pos.y + 12);
                }
            }

            batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_COLOR);
            TextureRegion tr = new TextureRegion(buffer.getColorBufferTexture());
            tr.flip(false,true);

            batch.draw(tr, camera.position.x - (VIEWPORT_WIDTH / 2.0f), camera.position.y - (VIEWPORT_HEIGHT / 2.0f));
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            if (conversation != null) {
                DialogLine currentDialog = conversation.getCurrentDialog();
                dialogContainer.render(batch, new Vector2(camera.position.x + 48, camera.position.y), currentDialog);
                portrait.setPosition(camera.position.x - (VIEWPORT_WIDTH / 2.0f), camera.position.y + 64 - (VIEWPORT_HEIGHT / 2.0f));
                portrait.draw(batch);
            }
            batch.end();

        }
        isLevelDirty = false;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
		bufferBatch.dispose();
		buffer.dispose();
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

        DialogSource dialogSource = currentLevel.getDialogSource(playerPos);
        if (dialogSource != null) {
            System.out.println("hit dialog " + dialogSource.id);
            startDialog(dialogSource.id, null);
            dialogSource.done = true;
        }
        if (conversation != null) {
            conversation.update();
        }
        SceneSource sceneSource = currentLevel.getSceneSource(playerPos);
        if (sceneSource != null && sceneContainer.scenes.containsKey(sceneSource.id)) {
            currentScene = sceneContainer.scenes.get(sceneSource.id);
        }
        if (currentScene != null) {
            currentScene.update(this);
            if (currentScene.isDone()) {
                currentScene = null;
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


        if (conversation != null) {
            if ((inputVector.x != 0 || inputVector.y != 0) && !dialogLock) {
                if (conversation.isFinished()) {
                    if (activeDialogVerb != null) {
                        activeDialogVerb.finish();
                        activeDialogVerb = null;
                        moveLock = true;
                    }
                    conversation = null;
                } else {
                    conversation.handleInput();
                }
            }
        } else {
            if (!moveLock && currentScene == null && !isMoving && !inputVector.isZero()) {
                boolean blocked = false;
                moveVector = inputVector.cpy();
                Vector2 nextTilePos = moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                if (currentLevel.isTileBlocked(nextTilePos)) {
                    Block block = currentLevel.getBlock(nextTilePos, true);
                    if (block == null) {
                        blocked = true;
                    } else {
                        Vector2 nextTileAgain = moveVector.cpy().scl(TILE_SIZE * 2.0f).add(playerPos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
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
        }
        if ((inputVector.x != 0 || inputVector.y != 0)) {
            dialogLock = true;
        } else {
            dialogLock = false;
            moveLock = false;
        }
        inputVector = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restartLevel();
        }
    }

    public void startDialog(String id, DialogVerb dialogVerb) {
        conversation = new Conversation(dialogContainer.dialogs.get(id));
        activeDialogVerb = dialogVerb;
        conversation.reset();
        dialogLock = true;
    }

    public void setActorPos(String actor, Vector2 pos) {
        for (Actor levelActor : currentLevel.actors) {
            if (levelActor.id.equals(actor)) {
                levelActor.pos = pos.cpy();
            }
        }
    }

    public void moveActor(String actor, Vector2 value) {
        for (Actor levelActor : currentLevel.actors) {
            if (levelActor.id.equals(actor)) {
                levelActor.pos.add(value);
            }
        }
    }

    public void moveCamera(Vector2 pos) {
        cameraTargetPos = pos.cpy();
        snaplock = true;
    }

    public void resetCamera() {
        snaplock = true;
        cameraTargetPos = playerPos;
    }

    public Trunk getTrunk() {
	    return currentLevel.trunk;
    }

    void addArrow(Vector2 pos, Vector2 dir) {
        arrows.add(new Arrow(arrowImage, pos, dir));
    }
}
