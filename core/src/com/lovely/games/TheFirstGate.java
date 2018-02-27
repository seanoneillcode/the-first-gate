package com.lovely.games;

import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.lovely.games.scene.*;

public class TheFirstGate extends ApplicationAdapter implements Stage {

    public static final float TILE_SIZE = 32f;

    private static final float HALF_TILE_SIZE = 16f;
    private static final float QUARTER_TILE_SIZE = 8f;
    private static final float PLAYER_SPEED = TILE_SIZE * 4.0f;
    private static final float CAMERA_MARGIN = 0.5f;
    private static final float CAMERA_CATCHUP_SPEED = 2.0f;
    private static final int VIEWPORT_WIDTH = 600;
    private static final int VIEWPORT_HEIGHT = 480;
    private static final float CAST_ARROW_COOLDOWN = 2.0f;

    private SpriteBatch batch;
    private SpriteBatch bufferBatch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private AssetManager assetManager;
    private OrthographicCamera camera;

    private Level currentLevel;
    private Vector2 playerPos, playerDir;
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
    private Sprite lightHole;
    private Animation<TextureRegion> walkanim;
    private Animation<TextureRegion> lightAnim, playerLightAnim, arrowAnim, torchAnim, campfireAnim;
    private float animationDelta = 0;
    private DialogContainer dialogContainer;
    private Conversation conversation;
    private boolean dialogLock = false;
    private Sprite playerLight, arrowSprite, levelLight;
    private boolean isLevelDirty = false;
    private FrameBuffer buffer;
    private OrthographicCamera cam;
    private Vector2 cameraTargetPos;
    private SceneContainer sceneContainer;
    private List<Scene> currentScenes;
    private DialogVerb activeDialogVerb;
    private boolean moveLock, snaplock;
    private Map<String, Texture> actorImages;
    private boolean skipLock;
    private boolean castLock;
    private String currentSpell;
    private float castCooldown = 0;
    private boolean fighting;
    private float fightLevel;
    private ShapeRenderer shapeRenderer;
    private Vector2 antFightJitter, proFightJitter;
    private String fightInputNeeded;
    private List<String> directions;
    private float fightInputScale;
    private Sprite fightDirectionArrow;
    private Sprite windSprite;
    private boolean fightInputLock;

    private FightVerb fightVerb;
    private float posterAlpha;
    private String posterImageName;
    private Sprite posterSprite;
    private boolean staticLevel;
    private String fightName;
    private float screenFade;
    private Sprite fadeSprite;
    private float gamma;
    private Color fadeColor;
    private Animation<TextureRegion> windAnim;
    private Animation<TextureRegion> windHorizontalAnim;

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
        assetManager.load("levels/tower-arrow-06.tmx", TiledMap.class);
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
        assetManager.load("levels/tower-broken-level.tmx", TiledMap.class);
        assetManager.load("levels/tower-bridge-1.tmx", TiledMap.class);
        assetManager.load("levels/tower-prize-fight.tmx", TiledMap.class);
        assetManager.load("levels/tower-ant-revenge.tmx", TiledMap.class);
        assetManager.load("levels/camp-fire.tmx", TiledMap.class);
        assetManager.load("levels/wind-1.tmx", TiledMap.class);
        assetManager.load("levels/wind-2.tmx", TiledMap.class);
        assetManager.load("levels/bullet-01.tmx", TiledMap.class);
        assetManager.load("levels/bullet-02.tmx", TiledMap.class);
        assetManager.load("levels/bullet-03.tmx", TiledMap.class);
        assetManager.load("levels/bullet-04.tmx", TiledMap.class);
        assetManager.load("levels/bullet-05.tmx", TiledMap.class);
        assetManager.load("levels/bullet-06.tmx", TiledMap.class);
        assetManager.load("levels/bullet-07.tmx", TiledMap.class);
        assetManager.load("levels/maze-1.tmx", TiledMap.class);

        assetManager.load("arrow.png", Texture.class);
        assetManager.load("platform.png", Texture.class);
        assetManager.load("block.png", Texture.class);
        assetManager.load("pressure.png", Texture.class);
        assetManager.load("ground-block.png", Texture.class);
        assetManager.load("door.png", Texture.class);
        assetManager.load("open-door.png", Texture.class);
        assetManager.load("wizard-sheet.png", Texture.class);
        assetManager.load("dialog-bottom.png", Texture.class);
        assetManager.load("dialog-top.png", Texture.class);
        assetManager.load("dialog-line.png", Texture.class);
        assetManager.load("light-hole.png", Texture.class);
        assetManager.load("light-magic.png", Texture.class);
        assetManager.load("player-light.png", Texture.class);
        assetManager.load("arrow-sheet.png", Texture.class);
        assetManager.load("level-light.png", Texture.class);
        assetManager.load("torch-sheet.png", Texture.class);
        assetManager.load("portraits/portrait-1.png", Texture.class);
        assetManager.load("portraits/red-01.png", Texture.class);
        assetManager.load("wizard.png", Texture.class);
        assetManager.load("ant-test.png", Texture.class);
        assetManager.load("fade-image.png", Texture.class);
        assetManager.load("dialog-pointer.png", Texture.class);

        assetManager.load("wind.png", Texture.class);
        assetManager.load("wind-horizontal.png", Texture.class);
        assetManager.load("fight-indicator.png", Texture.class);
        assetManager.load("fight-pro-avatar.png", Texture.class);
        assetManager.load("fight-ant-avatar.png", Texture.class);
        assetManager.load("direction-arrow.png", Texture.class);
        assetManager.load("poster-prize.png", Texture.class);
        assetManager.load("poster-help-ant.png", Texture.class);
        assetManager.load("poster-fuck-ant.png", Texture.class);
        assetManager.load("campfire.png", Texture.class);
        assetManager.load("option-pointer.png", Texture.class);
        assetManager.load("portraits/real-test.png", Texture.class);
        assetManager.load("portraits/portrait-pro.png", Texture.class);
        assetManager.load("portraits/portrait-pro-listening.png", Texture.class);
        assetManager.load("portraits/portrait-pro-angry.png", Texture.class);
        assetManager.load("portraits/portrait-pro-happy.png", Texture.class);
        assetManager.load("portraits/portrait-pro-worried.png", Texture.class);
        assetManager.load("portraits/portrait-ant-talking.png", Texture.class);
        assetManager.load("portraits/portrait-ant-listening.png", Texture.class);
        assetManager.load("portraits/portrait-ant-angry.png", Texture.class);
        assetManager.load("portraits/portrait-ant-happy.png", Texture.class);
        assetManager.load("portraits/portrait-ant-worried.png", Texture.class);

        assetManager.finishLoading();

        dialogContainer = new DialogContainer(assetManager);

        fadeColor = Color.BLACK;

        directions = Arrays.asList("left", "right", "up", "down");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();
        fighting = false;

		arrowImage = assetManager.get("arrow.png");
        platformImg = assetManager.get("platform.png");
        blockImage = assetManager.get("block.png");
        pressureImage = assetManager.get("pressure.png");
        groundBlockImage = assetManager.get("ground-block.png");
        doorImage = assetManager.get("door.png");
        openDoorImage = assetManager.get("open-door.png");
        lightHole = new Sprite((Texture) assetManager.get("light-hole.png"));
        lightHole.setScale(6.0f);
        playerLight = new Sprite((Texture) assetManager.get("player-light.png"));
        playerLight.setScale(1.0f, 4.0f);
        levelLight = new Sprite((Texture) assetManager.get("level-light.png"));
        fightDirectionArrow = new Sprite((Texture) assetManager.get("direction-arrow.png"));
        fadeSprite = new Sprite((Texture) assetManager.get("fade-image.png"));
        fadeSprite.setScale(4.0f);

        buffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, false);
        cam = new OrthographicCamera(buffer.getWidth(), buffer.getHeight());
        cam.position.set(buffer.getWidth() / 2, buffer.getWidth() / 2, 0);
        cam.update();

        posterSprite = new Sprite((Texture) assetManager.get("poster-prize.png"));
        posterSprite.setBounds(0,0,VIEWPORT_WIDTH,VIEWPORT_HEIGHT);

        shapeRenderer = new ShapeRenderer();

        screenFade = 0f;

        levels = new ArrayList<>();
        levels.add(Level.loadLevel(assetManager, "levels/tower-01.tmx")); // 01
        levels.add(Level.loadLevel(assetManager, "levels/tower-02.tmx"));
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-01.tmx")); // 05
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-02.tmx")); // 07
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-03.tmx")); // 09
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-04.tmx")); // 11
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
        levels.add(Level.loadLevel(assetManager, "levels/tower-broken-level.tmx")); // 51 // 23
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-06.tmx")); // 53 // 24
        levels.add(Level.loadLevel(assetManager, "levels/tower-bridge-1.tmx")); // 55 // 25
        levels.add(Level.loadLevel(assetManager, "levels/tower-prize-fight.tmx")); // 57 // 26
        levels.add(Level.loadLevel(assetManager, "levels/tower-ant-revenge.tmx")); // 59 // 27
        levels.add(Level.loadLevel(assetManager, "levels/camp-fire.tmx")); // start // 28
        levels.add(Level.loadLevel(assetManager, "levels/wind-1.tmx")); // 61 // 29
        levels.add(Level.loadLevel(assetManager, "levels/wind-2.tmx")); // 63 // 30
        levels.add(Level.loadLevel(assetManager, "levels/bullet-01.tmx")); // 65 // 31
        levels.add(Level.loadLevel(assetManager, "levels/bullet-02.tmx")); // 67 // 32
        levels.add(Level.loadLevel(assetManager, "levels/bullet-03.tmx")); // 69 // 33
        levels.add(Level.loadLevel(assetManager, "levels/bullet-04.tmx")); // 71 // 34
        levels.add(Level.loadLevel(assetManager, "levels/bullet-05.tmx")); // 73 // 35
        levels.add(Level.loadLevel(assetManager, "levels/bullet-06.tmx")); // 75 // 36
        levels.add(Level.loadLevel(assetManager, "levels/bullet-07.tmx")); // 77 // 37
        levels.add(Level.loadLevel(assetManager, "levels/maze-1.tmx")); // 79 // 38

        gamma = 0.2f;

        walkanim = loadAnimation(assetManager.get("wizard-sheet.png"), 4, 0.5f);
        lightAnim = loadAnimation(assetManager.get("light-magic.png"), 4, 0.6f);
        playerLightAnim = loadAnimation(assetManager.get("player-light.png"), 4, 0.5f);
        arrowAnim = loadAnimation(assetManager.get("arrow-sheet.png"), 8, 0.05f);
        torchAnim = loadAnimation(assetManager.get("torch-sheet.png"), 2, 0.5f);
        campfireAnim = loadAnimation(assetManager.get("campfire.png"), 8, 0.1f);
        windAnim = loadAnimation(assetManager.get("wind.png"), 8, 0.1f);
        windHorizontalAnim = loadAnimation(assetManager.get("wind-horizontal.png"), 8, 0.1f);
        arrowSprite = new Sprite();
        arrowSprite.setBounds(0,0,32,32);
        actorImages = new HashMap<>();
        actorImages.put("ant", assetManager.get("wizard.png"));
        currentScenes = new ArrayList<>();

        newConnectionTo = "01";
        moveLock = false;

        sceneContainer = new SceneContainer();

        // special
        startLevel(levels.get(38), "79");
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
        for (Actor actor : currentLevel.actors) {
            actor.start();
        }
        for (SceneSource sceneSource : currentLevel.scenes) {
            sceneSource.start();
            sceneContainer.scenes.get(sceneSource.id).start();
        }
        isLevelDirty = true;
        moveLock = false;
        cameraTargetPos = null;
        playerDir = new Vector2(1,0);
        if (levels.indexOf(currentLevel) >= 27) {
            currentSpell = "arrow";
        }
        fightLevel = 0;
        if (level.name.equals("levels/camp-fire.tmx")) {
            staticLevel = true;
        } else {
            staticLevel = false;
        }
    }

    private Vector3 getCameraPosition() {
        Vector2 pos = cameraTargetPos == null ? playerPos : cameraTargetPos;
        if (staticLevel) {
            pos = new Vector2(280, 240);
        }
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
            cameraPosition.x = MathUtils.clamp(cameraPosition.x, -cameraTrailLimit + pos.x, cameraTrailLimit + pos.x);
            cameraPosition.y = MathUtils.clamp(cameraPosition.y, -cameraTrailLimit + pos.y, cameraTrailLimit + pos.y);
        }
        return cameraPosition;
    }

    private void renderLightMasks() {
        buffer.begin();
        Gdx.gl.glClearColor(gamma, gamma, gamma, 1.0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bufferBatch.setProjectionMatrix(camera.combined);
        bufferBatch.begin();

        bufferBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);

        Vector2 offset = new Vector2((playerPos.x), (playerPos.y) );
        TextureRegion playerRegion = playerLightAnim.getKeyFrame(animationDelta, true);
        if (!staticLevel) {
            playerLight.setRegion(playerRegion);
            playerLight.setColor(1.0f, 0.8f, 0.5f, 1.0f);
            playerLight.setPosition( offset.x - 60, offset.y);
            playerLight.draw(bufferBatch);
        }

        TextureRegion tr = lightAnim.getKeyFrame(animationDelta, true);
        TextureRegion slow = lightAnim.getKeyFrame(animationDelta * 2.0f, true);
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
            if (torch.isFire) {
                lightHole.setColor(torch.color);
                lightHole.setRegion(slow);
                lightHole.setPosition((torch.pos.x), (torch.pos.y));
                lightHole.draw(bufferBatch);
            } else {
                lightHole.setColor(torch.color);
                lightHole.setRegion(tr);
                lightHole.setPosition((torch.pos.x), (torch.pos.y));
                lightHole.draw(bufferBatch);
            }
        }
        if (!staticLevel) {
            for (Actor actor : currentLevel.actors) {
                if (!actor.isHidden) {
                    playerLight.setRegion(playerRegion);
                    playerLight.setColor(1.0f, 0.8f, 0.5f, 1.0f);
                    playerLight.setPosition(actor.pos.x - 60, actor.pos.y);
                    playerLight.draw(bufferBatch);
                }
            }
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
            for (Block block : currentLevel.blocks) {
                if (block.isGround) {
                    batch.draw(groundBlockImage, block.pos.x, block.pos.y);
                }
                if (block.pos.y > playerPos.y && !block.isGround) {
                    batch.draw(blockImage, block.pos.x, block.pos.y);
                }
            }
            for (Arrow arrow : arrows) {
                TextureRegion currentFrame = arrowAnim.getKeyFrame(animationDelta, true);
                arrowSprite.setPosition(arrow.pos.x, arrow.pos.y + 8);
                arrowSprite.setRegion(currentFrame);
                arrowSprite.draw(batch);
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
                if (!actor.isHidden && actor.pos.y >= playerPos.y) {
                    Texture actorImage = actorImages.get(actor.id);
                    batch.draw(actorImage, actor.pos.x, actor.pos.y + 12);
                }
            }
            for (Torch torch : currentLevel.torches) {
                if (torch.pos.y >= playerPos.y) {
                    if (torch.isFire) {
                        TextureRegion torchFrame = campfireAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x - 20, torch.pos.y - 10);
                    } else {
                        TextureRegion torchFrame = torchAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x, torch.pos.y);
                    }
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
                if (torch.pos.y < playerPos.y) {
                    if (torch.isFire) {
                        TextureRegion torchFrame = campfireAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x - 20, torch.pos.y - 10);
                    } else {
                        TextureRegion torchFrame = torchAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x, torch.pos.y);
                    }
                }
            }
            for (Wind wind : currentLevel.winds) {
                float offset = 1.0f;
                for (Vector2 drawPos : wind.drawPositions) {
                    if (wind.isHorizontal()) {
                        TextureRegion windFrame = windHorizontalAnim.getKeyFrame(animationDelta + offset, true);
                        batch.draw(windFrame, drawPos.x , drawPos.y );
                    } else {
                        TextureRegion windFrame = windAnim.getKeyFrame(animationDelta + offset, true);
                        batch.draw(windFrame, drawPos.x , drawPos.y );
                    }
                    offset = offset + 0.4f;
                }
            }

            for (Actor actor : currentLevel.actors) {
                if (!actor.isHidden && actor.pos.y < playerPos.y) {
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
                dialogContainer.render(batch, new Vector2(camera.position.x - (VIEWPORT_WIDTH / 2.0f), camera.position.y - (VIEWPORT_HEIGHT / 2.0f)), conversation);
            } else {
                dialogContainer.reset();
            }

            if (fighting) {
                batch.draw((Texture) assetManager.get("fight-pro-avatar.png"), proFightJitter.x + camera.position.x - (VIEWPORT_WIDTH / 2.0f) , proFightJitter.y + camera.position.y - 160);
                batch.draw((Texture) assetManager.get("fight-ant-avatar.png"), camera.position.x + (VIEWPORT_WIDTH / 2.0f) - 300 + antFightJitter.x, antFightJitter.y + camera.position.y - 160);

                batch.end();
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(new Color(1.0f, 0.2f, 0.2f, 1.0f));
                shapeRenderer.rect(camera.position.x - (VIEWPORT_WIDTH / 2.0f) + 48, camera.position.y - 200, fightLevel * 5, 32);
                shapeRenderer.setColor(new Color(0.2f, 0.2f, 1.0f, 1.0f));
                shapeRenderer.rect(camera.position.x - (VIEWPORT_WIDTH / 2.0f) + 48 + (fightLevel * 5), camera.position.y - 200, (100 - fightLevel) * 5, 32);
                shapeRenderer.end();
                batch.begin();

                batch.draw((Texture) assetManager.get("fight-indicator.png"), camera.position.x - (VIEWPORT_WIDTH / 2.0f) + (fightLevel * 5) , camera.position.y - 216);

                fightDirectionArrow.setScale(fightInputScale);
                float rotation = 0;
                if (fightInputNeeded.equals("left")) {
                    rotation = 180;
                }
                if (fightInputNeeded.equals("up")) {
                    rotation = 90;
                }
                if (fightInputNeeded.equals("down")) {
                    rotation = 270;
                }
                fightDirectionArrow.setRotation(rotation);
                fightDirectionArrow.setPosition(camera.position.x - 32, camera.position.y - 32);
                fightDirectionArrow.draw(batch);
            }
            if (posterImageName != null) {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                posterSprite.setTexture(assetManager.get(posterImageName));
                posterSprite.setAlpha(posterAlpha);
                posterSprite.setPosition(camera.position.x - (VIEWPORT_WIDTH / 2.0f), camera.position.y - (VIEWPORT_HEIGHT / 2.0f));
                posterSprite.draw(batch);
            }
            if (screenFade > 0) {
                fadeSprite.setColor(fadeColor);
                fadeSprite.setAlpha(screenFade);
                fadeSprite.setPosition(camera.position.x - 76, camera.position.y - 62);
                fadeSprite.draw(batch);
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
        Wind wind = currentLevel.getWind(playerPos.cpy().add(4,4));
        if (wind != null) {
            Vector2 dir = wind.getDir();
            inputVector = dir.cpy();
        }
        for (Wind winds : currentLevel.winds) {
            winds.update();
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
            if (wind == null) {
                playerPos.x = MathUtils.round(playerPos.x / TILE_SIZE) * TILE_SIZE;
                playerPos.y = MathUtils.round(playerPos.y / TILE_SIZE) * TILE_SIZE;
            }
        }

        if (fighting) {
            fightLevel = fightLevel - (fightName.equals("camp") ? 0.4f : 0.2f);
            if (fightLevel < 0 || fightLevel > 100) {
                fightLevel = 0;
                fighting = false;
                fightVerb.isDone = true;
            }
            int jitter = 10;
            antFightJitter.x = antFightJitter.x + MathUtils.random(-jitter, jitter);
            antFightJitter.y = antFightJitter.y + MathUtils.random(-jitter, jitter);
            proFightJitter.x = proFightJitter.x + MathUtils.random(-jitter, jitter);
            proFightJitter.y = proFightJitter.y + MathUtils.random(-jitter, jitter);
            if (fightInputScale > 1.0f ) {
                fightInputScale = fightInputScale - (Gdx.graphics.getDeltaTime() * 2);
            }
        }

        if (currentPlatform != null && !isMoving) {
            playerPos = currentPlatform.pos.cpy();
        }


        Connection connection = currentLevel.getConnection(playerPos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE));
        if (connection == null) {
            newConnectionTo = null;
        }
        if (connection != null && !connection.name.equals(newConnectionTo)) {
            if (connection.to != null && !connection.to.isEmpty()) {
                goToConnection(connection.to);
            }
        }

        DialogSource dialogSource = currentLevel.getDialogSource(playerPos);
        if (dialogSource != null) {
            startDialog(dialogSource.id, null);
            dialogSource.done = true;
        }
        if (conversation != null) {
            conversation.update();
        }
        checkForSceneSources(playerPos);
        currentLevel.resetSceneSources(playerPos);
        List<Scene> newScenes = new ArrayList<>();
        Iterator<Scene> sceneIterator = currentScenes.iterator();
        while (sceneIterator.hasNext()) {
            Scene scene = sceneIterator.next();
            scene.update(this);
            if (scene.isDone()) {
                String outcome = scene.getOutcome();
                if (outcome != null) {
                    if (sceneContainer.scenes.containsKey(outcome)) {
                        newScenes.add(sceneContainer.scenes.get(outcome));
                    }
                }
                scene.start();
                sceneIterator.remove();
            }
        }
        currentScenes.addAll(newScenes);
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
            Vector2 nextTilePos = arrow.dir.cpy().scl(TILE_SIZE).add(arrow.pos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
            Block block = currentLevel.getBlock(nextTilePos, true);
            if (block!= null) {
                Vector2 nextTileAgain = arrow.dir.cpy().scl(TILE_SIZE * 2.0f).add(arrow.pos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                if (currentLevel.isTileBlocked(nextTileAgain)) {
                    arrowIterator.remove();
                } else {
                    block.move(arrow.dir);
                    arrowIterator.remove();
                }
            }
            if (getPlayerRect().overlaps(arrow.getRect())) {
                restartLevel();
            }
        }

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.update();
        }

        if (castCooldown > 0) {
            castCooldown = castCooldown - Gdx.graphics.getDeltaTime();
        }

        boolean blocksDirty = false;
        for (Block block : currentLevel.blocks) {
            block.update();
            if (!block.isMoving && !block.isGround) {
                Platform platform = currentLevel.getPlatform(block.pos);
                if (platform == null) {
                    block.pos.x = MathUtils.round(block.pos.x / TILE_SIZE) * TILE_SIZE;
                    block.pos.y = MathUtils.round(block.pos.y / TILE_SIZE) * TILE_SIZE;
                    if (currentLevel.isDeath(block.pos.cpy().add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE))) {
                        boolean alreadyGround = false;
                        for (Block otherBlock : currentLevel.blocks) {
                            if (otherBlock.isGround) {
                                if (block.pos.dst2(otherBlock.pos) < 64) {
                                    alreadyGround = true;
                                }
                            }
                        }
                        if (!alreadyGround) {
                            blocksDirty = true;
                            block.isGround = true;
                        }
                    }
                } else {
                    block.pos = platform.pos.cpy();
                }
            }
        }
        if (blocksDirty) {
            currentLevel.blocks.sort((o1, o2) -> o1.isGround == o2.isGround ? 0 : (o1.isGround ? -1 : 1));
        }
    }

    private void checkForSceneSources(Vector2 pos) {
        List<SceneSource> sceneSources = currentLevel.getSceneSources(pos);
        for (SceneSource sceneSource : sceneSources) {
            if (sceneContainer.scenes.containsKey(sceneSource.id) &&
                    !currentScenes.contains(sceneContainer.scenes.get(sceneSource.id))) {
                Scene scene = sceneContainer.scenes.get(sceneSource.id);
                currentScenes.add(scene);
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
                if (!conversation.isFinished()) {
                    conversation.handleInput(inputVector);
                }
                if (conversation.isFinished()) {
                    String chosenOption = conversation.getCurrentDialog().getChosenOption();
                    if (activeDialogVerb != null) {
                        activeDialogVerb.finish(chosenOption);
                        activeDialogVerb = null;
                        moveLock = true;
                        skipLock = true;
                    }
                    conversation = null;
                }
            }
        } else {
            if (!fighting) {
                boolean sceneBlock = !currentScenes.isEmpty() && currentScenes.stream().anyMatch(Scene::isBlocking);
                if (!moveLock && !sceneBlock && !isMoving && !inputVector.isZero()) {
                    boolean blocked = false;
                    moveVector = inputVector.cpy();
                    Vector2 nextTilePos = moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                    if (currentLevel.isTileBlocked(nextTilePos)) {
                        Block block = currentLevel.getBlock(nextTilePos, true);
                        if (block == null) {
                            checkForSceneSources(nextTilePos);
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
                        playerDir = inputVector.cpy();
                        movementValue = TILE_SIZE / PLAYER_SPEED;
                    }
                }
                if (!inputVector.isZero() && !skipLock) {
                    skipLock = true;
                    for (Scene scene : currentScenes) {
                        scene.skip();
                    }
                }
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    if (!sceneBlock && !castLock) {
                        castCurrentSpell();
                    }
                    castLock = true;
                } else {
                    castLock = false;
                }
            } else {
                int fightLevelAmount = 4;
                if (fightInputNeeded != null && !fightInputLock) {
                    switch (fightInputNeeded) {
                        case "left":
                            if (inputVector.x == -1 && inputVector.y == 0) {
                                fightInputNeeded = getRandomFightInput();
                                fightLevel = fightLevel + fightLevelAmount;
                                fightInputLock = true;
                            }
                            break;
                        case "right":
                            if (inputVector.x == 1 && inputVector.y == 0) {
                                fightInputNeeded = getRandomFightInput();
                                fightLevel = fightLevel + fightLevelAmount;
                                fightInputLock = true;
                            }
                            break;
                        case "up":
                            if (inputVector.x == 0 && inputVector.y == 1) {
                                fightInputNeeded = getRandomFightInput();
                                fightLevel = fightLevel + fightLevelAmount;
                                fightInputLock = true;
                            }
                            break;
                        case "down":
                            if (inputVector.x == 0 && inputVector.y == -1) {
                                fightInputNeeded = getRandomFightInput();
                                fightLevel = fightLevel + fightLevelAmount;
                                fightInputLock = true;
                            }
                            break;
                    }

                }
            }
        }
        if ((inputVector.x != 0 || inputVector.y != 0)) {
            dialogLock = true;
        } else {
            dialogLock = false;
            moveLock = false;
            skipLock = false;
            fightInputLock = false;
        }
        inputVector = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            gamma = gamma + 0.01f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            gamma = gamma - 0.01f;
        }
        gamma = MathUtils.clamp(gamma, 0, 1.0f);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restartLevel();
        }
    }

    private void castCurrentSpell() {
        if (castCooldown > 0) {
            return;
        }
        if (currentSpell != null && currentSpell.equals("arrow")) {
            Vector2 nextTilePos = playerDir.cpy().scl(TILE_SIZE).add(playerPos);
            addArrow(nextTilePos, playerDir);
            castCooldown = CAST_ARROW_COOLDOWN;
        }
    }

    private String getRandomFightInput() {
        fightInputScale = 2.0f;
        if (MathUtils.random(0,5) == 0 || fightInputNeeded == null) {
            return directions.get(MathUtils.random(0,3));
        }
        return fightInputNeeded;
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
        cameraTargetPos = null;
    }

    public void hideActor(String id, boolean isHide) {
        for (Actor levelActor : currentLevel.actors) {
            if (levelActor.id.equals(id)) {
                levelActor.isHidden = isHide;
            }
        }
    }

    public Trunk getTrunk() {
	    return currentLevel.trunk;
    }

    void addArrow(Vector2 pos, Vector2 dir) {
        arrows.add(new Arrow(arrowImage, pos, dir));
    }

    public void startFight(String fightName, FightVerb fightVerb) {
        fighting = true;
        fightLevel = 50;
        antFightJitter = new Vector2();
        proFightJitter = new Vector2();
        fightInputNeeded = getRandomFightInput();
        fightInputScale = 1.0f;
        this.fightVerb = fightVerb;
        this.fightName = fightName;
    }

    public void showPoster(float alpha, String poster) {
        posterAlpha = alpha;
        posterImageName = poster;
    }

    public void fadeScreen(float amount, Color color) {
        screenFade = amount;
        fadeColor = color;
    }

    @Override
    public void goToConnection(String target) {
        for (Level level : levels) {
            if (level.hasConnection(target)) {
                startLevel(level, target);
                newConnectionTo = target;
                break;
            }
        }
    }
}
