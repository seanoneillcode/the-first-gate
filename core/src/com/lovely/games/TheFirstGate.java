package com.lovely.games;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
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
    public static final float ARROW_SPEED = TILE_SIZE * 2.0f;

    private static final float HALF_TILE_SIZE = 16f;
    private static final float QUARTER_TILE_SIZE = 8f;
    private static final float PLAYER_SPEED = TILE_SIZE * 4.0f;
    private static final float CAMERA_MARGIN = 0.5f;
    private static final float CAMERA_CATCHUP_SPEED = 2.0f;
    private static final int VIEWPORT_WIDTH = 600;
    private static final int VIEWPORT_HEIGHT = 480;
    private static final float CAST_ARROW_COOLDOWN = 0.6f;
    private static final float PLAYER_DEATH_TIME = 1.0f;
    private static final float PLAYER_SHOOTING_TIME = 0.3f;
    private static final float PLAYER_ARROW_SPEED = TILE_SIZE * 4.0f;

    private float lazerSoundTimer = 0;
    private float stepTimer = 0;

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
    private Connection lastConnection;
    private List<Level> levels;
    private List<Arrow> arrows;
    private Texture arrowImage;
    private Texture platformImg;
    private Texture blockImage;
    private Texture groundBlockImage;
    private Texture pressureImage;
    private Platform currentPlatform;
    private Texture doorImage;
    private Texture lazerImage, horizontalLazerImage;
    private Texture openDoorImage;
    private Sprite lightHole;
    private Animation<TextureRegion> walkRight, idleAnim;
    private Animation<TextureRegion> lightAnim, playerLightAnim, arrowAnim, torchAnim, campfireAnim;
    private float animationDelta = 0;
    private float walkAnimDelta = 0;
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
    private Sprite playerSprite;
    private boolean fightInputLock;
    private Sprite enemySprite;

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
    private boolean levelChangeLock = false;
    private boolean isWalkOne = true;
    private boolean wasMoving;
    private boolean playerFacingLeft = false;
    private Animation<TextureRegion> fireDeath;
    private Animation<TextureRegion> fallDeath;
    private Animation<TextureRegion> pushBlock;
    private boolean playerIsPushing = false;
    private boolean playerWasPushing = false;
    private float playerDeathTimer = 0;
    private boolean playerIsDead = false;
    private boolean isFallDeath = false;
    private Animation<TextureRegion> playerShoot;
    private boolean isPlayerShooting = false;
    private float playerShootingTimer = 0;
    private Sprite titleSprite;
    private boolean isTitleMenu = false;
    private int titleSelectionIndex = 0;
    private List<String> titleOptions = Arrays.asList("quit", "options", "new", "continue");
    private Sprite titleSelectionSprite;
    private boolean titleLock = false;
    private Animation<TextureRegion> arrowExplodeAnim;
    private List<Explosion> explosions;
    private Animation<TextureRegion> antWalk;
    private Animation<TextureRegion> antIdle;
    private Sprite antSprite;
    private SoundPlayer soundPlayer;
    private int lastLevel = -1;
    private String lastConnectionNumber = "";
    private BitmapFont font;
    private Color fontColorSelectedMain = new Color(69 / 256.0f, 128 / 256.0f, 213 / 256.0f, 1);
    private Color fontColorMain = new Color(10 / 256.0f, 64 / 256.0f, 97 / 256.0f, 1);
    private Color fontGreyedOut = new Color(55 / 256.0f, 55 / 256.0f, 55 / 256.0f, 1);
    private boolean hasContinue = false;
    private boolean isViewDirty = false;
    private ScreenFader screenFader;

    @Override
	public void create () {
        assetManager = new AssetManager();
        FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.setLoader(Sound.class, new SoundLoader(fileHandleResolver));
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
        assetManager.load("levels/enemy-1.tmx", TiledMap.class);
        assetManager.load("levels/enemy-2.tmx", TiledMap.class);
        assetManager.load("levels/enemy-3.tmx", TiledMap.class);
        assetManager.load("levels/enemy-4.tmx", TiledMap.class);
        assetManager.load("levels/enemy-5.tmx", TiledMap.class);
        assetManager.load("levels/enemy-6.tmx", TiledMap.class);
        assetManager.load("levels/enemy-8.tmx", TiledMap.class);
        assetManager.load("levels/enemy-9.tmx", TiledMap.class);
        assetManager.load("levels/block-test.tmx", TiledMap.class);
        assetManager.load("levels/crossy-road-1.tmx", TiledMap.class);
        assetManager.load("levels/crossy-road-2.tmx", TiledMap.class);

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
        assetManager.load("enemy.png", Texture.class);
        assetManager.load("enemy-ground.png", Texture.class);
        assetManager.load("lava-ball.png", Texture.class);
        assetManager.load("lazer.png", Texture.class);
        assetManager.load("lazer-horizontal.png", Texture.class);
        assetManager.load("char-style-4.png", Texture.class);
        assetManager.load("char-style-3.png", Texture.class);
        assetManager.load("pro-simple-fall-death.png", Texture.class);
        assetManager.load("pro-simple-fire-death.png", Texture.class);
        assetManager.load("pro-simple-push.png", Texture.class);
        assetManager.load("pro-simple-shoot.png", Texture.class);
        assetManager.load("pro-simple-idle.png", Texture.class);
        assetManager.load("pro-simple-walk.png", Texture.class);
        assetManager.load("pro-walk-down.png", Texture.class);
        assetManager.load("arrow-explode.png", Texture.class);
        assetManager.load("ant-idle.png", Texture.class);
        assetManager.load("ant-walk.png", Texture.class);

        assetManager.load("caen-title.png", Texture.class);
        assetManager.load("wind.png", Texture.class);
        assetManager.load("wind-horizontal.png", Texture.class);
        assetManager.load("fight-indicator.png", Texture.class);
        assetManager.load("fight-pro-avatar.png", Texture.class);
        assetManager.load("fight-ant-avatar.png", Texture.class);
        assetManager.load("direction-arrow.png", Texture.class);
        assetManager.load("poster-prize.png", Texture.class);
        assetManager.load("poster-help-ant.png", Texture.class);
        assetManager.load("ending-poster.png", Texture.class);
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

        assetManager.load("sound/step-2.ogg", Sound.class);
        assetManager.load("sound/block-0.ogg", Sound.class);
        assetManager.load("sound/chirp-1.ogg", Sound.class);
        assetManager.load("sound/cricket-2.ogg", Sound.class);
        assetManager.load("sound/fall-0.ogg", Sound.class);
        assetManager.load("sound/flame-0.ogg", Sound.class);
        assetManager.load("sound/blast-1.ogg", Sound.class);
        assetManager.load("sound/block-3.ogg", Sound.class);
        assetManager.load("sound/lazer-4.ogg", Sound.class);

        assetManager.finishLoading();

        dialogContainer = new DialogContainer(assetManager);

        fadeColor = Color.BLACK;
        inputVector = new Vector2();

        directions = Arrays.asList("left", "right", "up", "down");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();
        fighting = false;

        explosions = new ArrayList<>();

        soundPlayer = new SoundPlayer(assetManager);

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
        enemySprite = new Sprite((Texture) assetManager.get("enemy.png"));
        playerSprite = new Sprite();
        playerSprite.setSize(32,32);
        antSprite = new Sprite();
        antSprite.setSize(32,32);

        titleSprite = new Sprite((Texture) assetManager.get("caen-title.png"));
        titleSprite.setScale(2);

        lazerImage = assetManager.get("lazer.png");
        horizontalLazerImage = assetManager.get("lazer-horizontal.png");

        buffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, false);
        cam = new OrthographicCamera(buffer.getWidth(), buffer.getHeight());
        cam.position.set(buffer.getWidth() / 2, buffer.getWidth() / 2, 0);
        cam.update();

        posterSprite = new Sprite((Texture) assetManager.get("poster-prize.png"));
        posterSprite.setBounds(0,0,VIEWPORT_WIDTH,VIEWPORT_HEIGHT);

        shapeRenderer = new ShapeRenderer();

        screenFader = new ScreenFader();
        screenFade = 0f;

        levels = new ArrayList<>();
        levels.add(Level.loadLevel(assetManager, "levels/tower-01.tmx", soundPlayer)); // 01
        levels.add(Level.loadLevel(assetManager, "levels/tower-02.tmx", soundPlayer));
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-01.tmx", soundPlayer)); // 05
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-02.tmx", soundPlayer)); // 07
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-03.tmx", soundPlayer)); // 09
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-04.tmx", soundPlayer)); // 11
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-01.tmx", soundPlayer)); // 13
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-02.tmx", soundPlayer)); // 15
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-03.tmx", soundPlayer)); // 17
        levels.add(Level.loadLevel(assetManager, "levels/tower-platform-04.tmx", soundPlayer)); // 19
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-01.tmx", soundPlayer)); // 21 // 10
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-02.tmx", soundPlayer)); // 23
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-03.tmx", soundPlayer)); // 25
        levels.add(Level.loadLevel(assetManager, "levels/tower-block-04.tmx", soundPlayer)); // 27
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-01.tmx", soundPlayer)); // 29
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-02.tmx", soundPlayer)); // 31
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-03.tmx", soundPlayer)); // 33
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-05.tmx", soundPlayer)); // 35
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-04.tmx", soundPlayer)); // 37
        levels.add(Level.loadLevel(assetManager, "levels/tower-switch-05.tmx", soundPlayer)); // 39
        levels.add(Level.loadLevel(assetManager, "levels/start-room.tmx", soundPlayer)); // 1 // 20
        levels.add(Level.loadLevel(assetManager, "levels/end-room.tmx", soundPlayer)); // 41 // 21
        levels.add(Level.loadLevel(assetManager, "levels/scene-test.tmx", soundPlayer)); // 1 // 22
        levels.add(Level.loadLevel(assetManager, "levels/tower-broken-level.tmx", soundPlayer)); // 51 // 23
        levels.add(Level.loadLevel(assetManager, "levels/tower-arrow-06.tmx", soundPlayer)); // 53 // 24
        levels.add(Level.loadLevel(assetManager, "levels/tower-bridge-1.tmx", soundPlayer)); // 55 // 25
        levels.add(Level.loadLevel(assetManager, "levels/tower-prize-fight.tmx", soundPlayer)); // 57 // 26
        levels.add(Level.loadLevel(assetManager, "levels/tower-ant-revenge.tmx", soundPlayer)); // 59 // 27
        levels.add(Level.loadLevel(assetManager, "levels/camp-fire.tmx", soundPlayer)); // start // 28
        levels.add(Level.loadLevel(assetManager, "levels/wind-1.tmx", soundPlayer)); // 61 // 29
        levels.add(Level.loadLevel(assetManager, "levels/wind-2.tmx", soundPlayer)); // 63 // 30
        levels.add(Level.loadLevel(assetManager, "levels/bullet-01.tmx", soundPlayer)); // 65 // 31
        levels.add(Level.loadLevel(assetManager, "levels/bullet-02.tmx", soundPlayer)); // 67 // 32
        levels.add(Level.loadLevel(assetManager, "levels/bullet-03.tmx", soundPlayer)); // 69 // 33
        levels.add(Level.loadLevel(assetManager, "levels/bullet-04.tmx", soundPlayer)); // 71 // 34
        levels.add(Level.loadLevel(assetManager, "levels/bullet-05.tmx", soundPlayer)); // 73 // 35
        levels.add(Level.loadLevel(assetManager, "levels/bullet-06.tmx", soundPlayer)); // 75 // 36
        levels.add(Level.loadLevel(assetManager, "levels/bullet-07.tmx", soundPlayer)); // 77 // 37
        levels.add(Level.loadLevel(assetManager, "levels/maze-1.tmx", soundPlayer)); // 79 // 38
        levels.add(Level.loadLevel(assetManager, "levels/enemy-1.tmx", soundPlayer)); // 39
        levels.add(Level.loadLevel(assetManager, "levels/enemy-2.tmx", soundPlayer)); // 40
        levels.add(Level.loadLevel(assetManager, "levels/enemy-3.tmx", soundPlayer)); // 41
        levels.add(Level.loadLevel(assetManager, "levels/enemy-4.tmx", soundPlayer)); // 42
        levels.add(Level.loadLevel(assetManager, "levels/enemy-5.tmx", soundPlayer)); // 43
        levels.add(Level.loadLevel(assetManager, "levels/enemy-6.tmx", soundPlayer)); // 44
        levels.add(Level.loadLevel(assetManager, "levels/enemy-8.tmx", soundPlayer)); // 45
        levels.add(Level.loadLevel(assetManager, "levels/enemy-9.tmx", soundPlayer)); // 46
        levels.add(Level.loadLevel(assetManager, "levels/block-test.tmx", soundPlayer)); // 47
        levels.add(Level.loadLevel(assetManager, "levels/crossy-road-1.tmx", soundPlayer)); // 48
        levels.add(Level.loadLevel(assetManager, "levels/crossy-road-2.tmx", soundPlayer)); // 49
        gamma = 0.2f;

        antWalk = loadAnimation(assetManager.get("ant-walk.png"), 4, 0.165f);
        antIdle = loadAnimation(assetManager.get("ant-idle.png"), 2, 0.5f);
        walkRight = loadAnimation(assetManager.get("pro-simple-walk.png"), 4, 0.165f);
        fireDeath = loadAnimation(assetManager.get("pro-simple-fire-death.png"), 8, 0.085f);
        fallDeath = loadAnimation(assetManager.get("pro-simple-fall-death.png"), 5, 0.08f);
        playerShoot = loadAnimation(assetManager.get("pro-simple-shoot.png"), 6, 0.1f);
        pushBlock = loadAnimation(assetManager.get("pro-simple-push.png"), 4, 0.165f);
        idleAnim = loadAnimation(assetManager.get("pro-simple-idle.png"), 2, 0.5f);
        lightAnim = loadAnimation(assetManager.get("light-magic.png"), 4, 0.6f);
        playerLightAnim = loadAnimation(assetManager.get("player-light.png"), 4, 0.5f);
        arrowAnim = loadAnimation(assetManager.get("arrow-sheet.png"), 8, 0.05f);
        arrowExplodeAnim = loadAnimation(assetManager.get("arrow-explode.png"), 8, 0.05f);
        torchAnim = loadAnimation(assetManager.get("torch-sheet.png"), 2, 0.5f);
        campfireAnim = loadAnimation(assetManager.get("campfire.png"), 8, 0.1f);
        arrowSprite = new Sprite();
        arrowSprite.setBounds(0,0,32,32);
        actorImages = new HashMap<>();
        actorImages.put("ant", assetManager.get("char-style-4.png"));
        currentScenes = new ArrayList<>();
        loadExternalData();

        font = loadFonts("kells.fnt");

        // start room = 20
//        Level startLevel = levels.get(lastLevel); // 28 -> 22 ->
        moveLock = false;

        sceneContainer = new SceneContainer();

        isTitleMenu = true;
        fadeScreen(true, 3.0f, Color.BLACK);
//        setScreenFade(1.0f, Color.BLACK);

        // special
//        startLevel(startLevel, startLevel.getPreviousConnection());
//        Gdx.app.getPreferences("caen-preferences").clear();
	}

	private void loadExternalData() {
        Preferences prefs = Gdx.app.getPreferences("caen-preferences");
        if (prefs.contains("current-spell")) {
            currentSpell = prefs.getString("current-spell");
        } else {
            currentSpell = null;
        }
        if (prefs.contains("last-level")) {
            hasContinue = prefs.getInteger("last-level") != 20;
        } else {
            hasContinue = false;
        }
    }

    private void saveLevelNumber(int levelNumber, String lastConnectionNumber) {
        Preferences prefs = Gdx.app.getPreferences("caen-preferences");
        prefs.putInteger("last-level", levelNumber);
        prefs.putString("last-connection-number", lastConnectionNumber);
        prefs.flush();
        hasContinue = true;
    }

    private void loadLevelFromPrefs() {
        Preferences prefs = Gdx.app.getPreferences("caen-preferences");
        if (prefs.contains("last-level")) {
            lastLevel = prefs.getInteger("last-level");
        } else {
            lastLevel = 20;
        }
        Level level = levels.get(lastLevel);
        Connection connection = level.getPreviousConnection();
        if (prefs.contains("last-connection-number")) {
            lastConnectionNumber = prefs.getString("last-connection-number");
            if (level.hasConnection(lastConnectionNumber)) {
                connection = level.getConnection(lastConnectionNumber);
            }
        }

        startLevel(level, connection);
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

    private void startLevel(Level level, Connection startConnection) {
        currentPlatform = null;
        playerIsDead = false;
        playerDeathTimer = 0;
        stepTimer = -1;
        loadLevel(level);
        currentLevel = level;
        saveLevelNumber(levels.indexOf(currentLevel), startConnection.name);
        playerPos = startConnection.pos.cpy();
        isMoving = false;
        inputVector = new Vector2();
        moveVector = new Vector2();
        arrows = new ArrayList<>();
        movementValue = 0;
        lastConnection = startConnection;
        explosions = new ArrayList<>();
        for (ArrowSource arrowSource : currentLevel.getArrowSources()) {
            arrowSource.start();
        }
        for (Platform platform : currentLevel.getPlatforms()) {
            platform.start(soundPlayer);
        }
        for (Block block : currentLevel.blocks) {
            block.start();
        }
        for (Enemy enemy : currentLevel.enemies) {
            enemy.start();
        }
        for (Connection connection : currentLevel.connections) {
            connection.reset();
        }
        for (PressureTile pressureTile : currentLevel.pressureTiles) {
            pressureTile.start();
        }
        for (Actor actor : currentLevel.actors) {
            actor.start();
        }
        for (Door door : currentLevel.doors) {
            door.start();
        }
        for (SceneSource sceneSource : currentLevel.scenes) {
            sceneSource.start();
            sceneContainer.scenes.get(sceneSource.id).start();
        }
        isLevelDirty = true;
        moveLock = false;
        cameraTargetPos = null;
        playerDir = new Vector2(1,0);
        fightLevel = 0;
        if (level.name.equals("levels/camp-fire.tmx")) {
            staticLevel = true;
            soundPlayer.playSound("sound/chirp-1.ogg", true, 0.2f);
            soundPlayer.playSound("sound/cricket-2.ogg", true, 0.2f);
        } else {
            soundPlayer.stopSound("sound/chirp-1.ogg");
            soundPlayer.stopSound("sound/cricket-2.ogg");
            staticLevel = false;
        }
    }

    private Vector3 getCameraPosition() {
        Vector2 pos = cameraTargetPos == null ? playerPos : cameraTargetPos;
        if (staticLevel) {
            pos = new Vector2(280, 240);
        }
        if (isTitleMenu) {
            return new Vector3(280, 240, 0);
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
        if (currentLevel.name.equals("levels/maze-1.tmx") ) {
            Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        } else {
            Gdx.gl.glClearColor(gamma, gamma, gamma, 1.0f);
        }
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
        for (Explosion explosion : explosions) {
            lightHole.setColor(explosion.color);
            lightHole.setAlpha(1 - explosion.getAlpha() * 0.8f);
            lightHole.setRegion(tr);
            lightHole.setPosition((explosion.pos.x - 12), (explosion.pos.y));
            lightHole.setScale((explosion.getAlpha() * 4) * 6.0f);
            lightHole.draw(bufferBatch);
        }
        lightHole.setScale(6.0f);


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
        for (Enemy enemy : currentLevel.enemies) {
            lightHole.setColor(enemy.color);
            lightHole.setRegion(tr);
            lightHole.setPosition((enemy.pos.x), (enemy.pos.y));
            lightHole.draw(bufferBatch);
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
        if (!isTitleMenu) {
            mapRenderer.setView(camera);
            update();
        }
        screenFader.update(this);
	    getInput();
	    animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
        if (!isTitleMenu) {
            renderLightMasks();
        }
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if (!isLevelDirty && !isTitleMenu && !isViewDirty) {
            Vector2 threeDeeLinePos = playerPos.cpy().add(0, 0);
            mapRenderer.render();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            for (PressureTile pressureTile : currentLevel.pressureTiles) {
                batch.draw(pressureImage, pressureTile.pos.x, pressureTile.pos.y);
            }
            for (Platform platform : currentLevel.getPlatforms()) {
                batch.draw(platformImg, platform.pos.x, platform.pos.y);
            }
            List<BlockLike> blockLikes = currentLevel.getBlockLikes();
            blockLikes.sort((o1, o2) -> (int)(o2.getPos().y - o1.getPos().y));
            for (BlockLike blockLike : blockLikes) {
                if (!(blockLike instanceof Enemy)) {
                    if (blockLike.isGround()) {
                        batch.draw(groundBlockImage, blockLike.getPos().x, blockLike.getPos().y);
                    }
                    if (blockLike.getPos().y > threeDeeLinePos.y && !blockLike.isGround()) {
                        batch.draw(blockImage, blockLike.getPos().x, blockLike.getPos().y);
                    }
                }
                if (blockLike instanceof Enemy) {
                    if (blockLike.getPos().y > threeDeeLinePos.y) {
                        drawEnemy((Enemy) blockLike);
                    }
                }
            }
            for (Arrow arrow : arrows) {
                if (arrow.img.equals(arrowImage)) {
                    TextureRegion currentFrame = arrowAnim.getKeyFrame(animationDelta, true);
                    arrowSprite.setPosition(arrow.pos.x, arrow.pos.y + 12);
                    arrowSprite.setRegion(currentFrame);
                    arrowSprite.draw(batch);
                } else {
                    batch.draw(arrow.img, arrow.pos.x, arrow.pos.y);
                }
            }
            for (Door door : currentLevel.doors) {
                if (door.isOpen) {
                    batch.draw(openDoorImage, door.pos.x, door.pos.y);
                }
                if (!door.isOpen && door.pos.y >= threeDeeLinePos.y) {
                    batch.draw(doorImage, door.pos.x, door.pos.y);
                }
            }
            for (Actor actor : currentLevel.actors) {
                if (!actor.isHidden && actor.pos.y >= threeDeeLinePos.y) {
                    drawActor(actor);
                }
            }
            for (Torch torch : currentLevel.torches) {
                if (torch.pos.y >= threeDeeLinePos.y) {
                    if (torch.isFire) {
                        TextureRegion torchFrame = campfireAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x - 20, torch.pos.y - 10);
                    } else {
                        TextureRegion torchFrame = torchAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x, torch.pos.y);
                    }
                }
            }
            playerSprite.setPosition(playerPos.x, playerPos.y + QUARTER_TILE_SIZE );
            TextureRegion currentFrame;
            if (!playerIsDead && isMoving) {
                if (playerIsPushing || playerWasPushing) {
                    currentFrame = pushBlock.getKeyFrame(walkAnimDelta, true);
                } else {
                    currentFrame = walkRight.getKeyFrame(walkAnimDelta, true);
                }
            } else {
                if (playerIsDead) {
                    if (isFallDeath) {
                        currentFrame = fallDeath.getKeyFrame(animationDelta, false);
                    } else {
                        currentFrame = fireDeath.getKeyFrame(animationDelta, false);
                    }
                } else {
                    if (isPlayerShooting) {
                        currentFrame = playerShoot.getKeyFrame(animationDelta, true);
                    } else {
                        currentFrame = idleAnim.getKeyFrame(animationDelta, true);
                    }
                }
            }
            playerSprite.setRegion(currentFrame);
            if (playerFacingLeft) {
                playerSprite.flip(true, false);
            }
            playerSprite.draw(batch);

            for (BlockLike blockLike : blockLikes) {
                if (!(blockLike instanceof Enemy)) {
                    if (blockLike.getPos().y <= threeDeeLinePos.y && !blockLike.isGround()) {
                        batch.draw(blockImage, blockLike.getPos().x, blockLike.getPos().y);
                    }
                }
                if (blockLike instanceof Enemy) {
                    if (blockLike.getPos().y <= threeDeeLinePos.y && !blockLike.isGround()) {
                        drawEnemy((Enemy) blockLike);
                    }
                }
            }
            for (Door door : currentLevel.doors) {
                if (door.pos.y < threeDeeLinePos.y && !door.isOpen) {
                    batch.draw(doorImage, door.pos.x, door.pos.y);
                }
            }
            for (Torch torch : currentLevel.torches) {
                if (torch.pos.y < threeDeeLinePos.y) {
                    if (torch.isFire) {
                        TextureRegion torchFrame = campfireAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x - 20, torch.pos.y - 10);
                    } else {
                        TextureRegion torchFrame = torchAnim.getKeyFrame(animationDelta, true);
                        batch.draw(torchFrame, torch.pos.x, torch.pos.y);
                    }
                }
            }
            for (Explosion explosion : explosions) {
                TextureRegion frame = arrowExplodeAnim.getKeyFrame(explosion.getTimer(), false);
                batch.draw(frame, explosion.pos.x - 12, explosion.pos.y );
            }

            for (Actor actor : currentLevel.actors) {
                if (!actor.isHidden && actor.pos.y < threeDeeLinePos.y) {
                    drawActor(actor);
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
            batch.end();
        }
        if (isTitleMenu && !isViewDirty) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            titleSprite.setPosition(180, 300);
            titleSprite.draw(batch);
            Vector2 selectedPos = new Vector2(280, 200);
            for (int index = titleOptions.size() - 1; index > -1; index--) {
                String option = titleOptions.get(index);
                if (titleSelectionIndex == titleOptions.size() - 1 - index) {
                    font.setColor(fontColorSelectedMain);
                } else {
                    font.setColor(fontColorMain);
                }
                if (option.equals("continue") && !hasContinue) {
                    font.setColor(fontGreyedOut);
                }
                font.draw(batch, option, selectedPos.x, selectedPos.y, 0f, 1, false);
                selectedPos.add(0, -40);
            }
            batch.end();
        }
        if (screenFade > 0) {
            Vector2 pos = new Vector2(204, 180);
            if (!isTitleMenu) {
                pos = new Vector2(camera.position.x - 76, camera.position.y - 60);
            }
            batch.begin();
            fadeSprite.setColor(fadeColor);
            fadeSprite.setAlpha(screenFade);
            fadeSprite.setPosition(pos.x, pos.y);
            fadeSprite.draw(batch);
            batch.end();
        }

        isLevelDirty = false;
        isViewDirty = false;
	}

    private BitmapFont loadFonts(String fontString) {
        font = new BitmapFont(Gdx.files.internal(fontString),false);
        font.setUseIntegerPositions(false);
        font.setColor(fontColorMain);
        font.getData().setScale(2.0f, 2.0f);
        return font;
    }

    private void drawActor(Actor actor) {
        TextureRegion currentFrame;
        if (actor.isWalking) {
            currentFrame = antWalk.getKeyFrame(animationDelta, true);
        } else {
            currentFrame = antIdle.getKeyFrame(animationDelta, true);
        }
        antSprite.setPosition(actor.pos.x, actor.pos.y + 12);
        antSprite.setRegion(currentFrame);
        antSprite.flip(!actor.isFacingRight, false);
        antSprite.draw(batch);
    }

    private void drawEnemy(Enemy enemy) {
        String enemyImg = enemy.isGround() ? "enemy-ground.png" : "enemy.png";
        enemySprite.setTexture(assetManager.get(enemyImg));
        enemySprite.setRotation(enemy.getRotation());
        enemySprite.setPosition(enemy.pos.x, enemy.pos.y + 8);
        enemySprite.draw(batch);
    }

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
		bufferBatch.dispose();
		buffer.dispose();
	}

	private void update() {
        boolean blocksDirty = false;
        if (playerWasPushing) {
            playerWasPushing = false;
        }
        if (playerDeathTimer > 0) {
            playerDeathTimer = playerDeathTimer - Gdx.graphics.getDeltaTime();

        } else {
            if (playerIsDead) {
                restartLevel();
            }
        }
        if (playerShootingTimer > 0) {
            playerShootingTimer = playerShootingTimer - Gdx.graphics.getDeltaTime();
            if (playerShootingTimer < 0) {
                isPlayerShooting = false;
            }
            if (playerShootingTimer < 0.1f) {
                castCurrentSpell();
            }
        }
        if (isMoving && !playerIsDead) {
            float movementDelta = Gdx.graphics.getDeltaTime();
            movementValue = movementValue - movementDelta;
            walkAnimDelta = walkAnimDelta + movementDelta;
            if (movementValue < 0) {
                isMoving = false;
                wasMoving = true;
                playerWasPushing = playerIsPushing;
                playerIsPushing = false;
                isWalkOne = !isWalkOne;
                movementDelta = movementDelta + movementValue;
            }
            Vector2 movement = moveVector.cpy().scl(movementDelta * PLAYER_SPEED);
            playerPos.add(movement);
            if (currentPlatform != null) {
                playerPos.add(currentPlatform.getMovement());
            }
        }
        if (lazerSoundTimer > 0) {
            lazerSoundTimer = lazerSoundTimer - Gdx.graphics.getDeltaTime();
        }
        if (stepTimer > 0) {
            stepTimer = stepTimer - Gdx.graphics.getDeltaTime();
        }
        for (PressureTile pressureTile : currentLevel.pressureTiles) {
            boolean handled = false;
            if (playerPos.dst2(pressureTile.pos) < 64) {
                pressureTile.handleAction();
                handled = true;
            }
            for (BlockLike block : currentLevel.getBlockLikes()) {
                if (block.getPos().dst2(pressureTile.pos) < 64) {
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
        for (Explosion explosion : explosions) {
            explosion.update();
        }
        explosions.removeIf(Explosion::isDone);
        if (!isMoving && !playerIsDead) {
            Platform platform = currentLevel.getPlatform(playerPos);
            if (platform != null) {
                currentPlatform = platform;
            } else {
                if (currentPlatform != null) {

                }
                currentPlatform = null;
            }
            if (currentPlatform == null && currentLevel.isDeath(playerPos.cpy().add(HALF_TILE_SIZE,HALF_TILE_SIZE))) {
                BlockLike block = currentLevel.getBlockLike(playerPos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE), false);
                if (!(block != null && block.isGround())) {
                    playerDeathTimer = PLAYER_DEATH_TIME;
                    soundPlayer.playSound("sound/fall-0.ogg", false, 0.8f);
                    playerIsDead = true;
                    animationDelta = 0;
                    isFallDeath = true;
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

        for (Connection connection : currentLevel.connections) {
            if (connection.contains(playerPos.cpy().add(QUARTER_TILE_SIZE,QUARTER_TILE_SIZE))) {
                if (connection.active) {
                    if (connection.to != null && !connection.to.isEmpty()) {
                        goToConnection(connection.to);
                        return;
                    }
                }
            } else {
                connection.active = true;
            }
        }

        for (Actor actor : currentLevel.actors) {
            actor.isWalking = false;
        }

        soundPlayer.update(playerPos);

        for (Enemy enemy : currentLevel.enemies) {
            enemy.update(playerPos.cpy().add(HALF_TILE_SIZE,HALF_TILE_SIZE), this);
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
                explosions.add(new Explosion(arrow.pos.cpy()));
                soundPlayer.playSound("sound/blast-1.ogg", false, 0.3f,  MathUtils.random(0.7f, 1.3f));
                arrowIterator.remove();
            }
            Vector2 nextTilePos = arrow.dir.cpy().scl(TILE_SIZE).add(arrow.pos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
            BlockLike block = currentLevel.getBlockLike(nextTilePos, true);
            if (block != null) {
                blocksDirty = true;
                Vector2 nextTileAgain = arrow.dir.cpy().scl(TILE_SIZE * 2.0f).add(arrow.pos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                explosions.add(new Explosion(arrow.pos.cpy()));
                if (currentLevel.isTileBlocked(nextTileAgain)) {
                    arrowIterator.remove();
                } else {
                    block.move(arrow.dir);
                    arrowIterator.remove();
                }
                soundPlayer.playSound("sound/blast-1.ogg", false, 0.3f,  MathUtils.random(0.7f, 1.3f));
            }
            if (!playerIsDead && getPlayerRect().overlaps(arrow.getRect())) {
                playerDeathTimer = PLAYER_DEATH_TIME;
                playerIsDead = true;
                soundPlayer.playSound("sound/flame-0.ogg", false, 0.8f,  MathUtils.random(0.7f, 1.3f));
                animationDelta = 0;
                isFallDeath = false;
                return;
            }
        }

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.update(soundPlayer);
        }

        if (castCooldown > 0) {
            castCooldown = castCooldown - Gdx.graphics.getDeltaTime();
        }

        for (Block block : currentLevel.blocks) {
            block.update();
        }
        for (BlockLike blockLike : currentLevel.getBlockLikes()) {
            if (!blockLike.isMoving() && !blockLike.isGround()) {
                Platform platform = currentLevel.getPlatform(blockLike.getPos());
                if (platform == null) {
                    blockLike.setPos(new Vector2(tileRound(blockLike.getPos().x), tileRound(blockLike.getPos().y)));
                    if (currentLevel.isDeath(blockLike.getPos().cpy().add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE))) {
                        boolean alreadyGround = false;
                        for (Block otherBlock : currentLevel.blocks) {
                            if (otherBlock.isGround()) {
                                if (blockLike.getPos().dst2(otherBlock.getPos()) < 64) {
                                    alreadyGround = true;
                                }
                            }
                        }
                        if (!alreadyGround) {
                            blocksDirty = true;
                            blockLike.setGround(true);
                        }
                    }
                } else {
                    blockLike.setPos(platform.pos.cpy());
                }
            }
        }
        if (blocksDirty) {
            currentLevel.blocks.sort((o1, o2) -> o1.isGround() == o2.isGround() ? 0 : (o1.isGround() ? -1 : 1));
        }
    }


    private float tileRound(float in) {
        return MathUtils.round(in / TILE_SIZE) * TILE_SIZE;
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
        startLevel(currentLevel, lastConnection);
    }

    private void goToNextLevel() {
        Connection connection = currentLevel.getPreviousConnection();
        if (connection != null && connection.to != null && !connection.to.equals("")) {
            goToConnection(connection.to);
        }
    }

    private void goToPreviousLevel() {
        Connection connection = currentLevel.getNextConnection();
        if (connection != null && connection.to != null && !connection.to.equals("")) {
            goToConnection(connection.to);
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

        if (isTitleMenu && !titleLock) {
            titleSelectionIndex = titleSelectionIndex - (int) inputVector.y;
            if (inputVector.y != 0) {
                titleLock = true;
                moveLock = true;
            }
            if (!hasContinue) {
                if (titleSelectionIndex < 1) {
                    titleSelectionIndex = 1;
                }
            }
            if (titleSelectionIndex > titleOptions.size() - 1) {
                titleSelectionIndex = titleOptions.size() - 1;
            }
            if (titleSelectionIndex < 0) {
                titleSelectionIndex = 0;
            }
            if (inputVector.x != 0 || Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                if (titleSelectionIndex == 0) {
                    isTitleMenu = false;
                    loadLevelFromPrefs();
                }
                if (titleSelectionIndex == 1) {
                    isTitleMenu = false;
                    startLevel(levels.get(20), levels.get(20).getConnection("61"));
                }
                if (titleSelectionIndex == 3) {
                    Gdx.app.exit();
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            }
            if (inputVector.x == 0 && inputVector.y == 0) {
                titleLock = false;
            }
            inputVector = new Vector2();
            return;
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
            if (!fighting && !playerIsDead) {
                boolean sceneBlock = !currentScenes.isEmpty() && currentScenes.stream().anyMatch(Scene::isBlocking);
                if (!moveLock && !sceneBlock && !isMoving && !inputVector.isZero()) {
                    boolean blocked = false;
                    moveVector = inputVector.cpy();
                    Vector2 nextTilePos = moveVector.cpy().scl(TILE_SIZE).add(playerPos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                    if (currentLevel.isTileBlocked(nextTilePos)) {
                        BlockLike block = currentLevel.getBlockLike(nextTilePos, true);
                        if (block == null) {
                            checkForSceneSources(nextTilePos);
                            blocked = true;
                        } else {
                            Vector2 nextTileAgain = moveVector.cpy().scl(TILE_SIZE * 2.0f).add(playerPos).add(QUARTER_TILE_SIZE, QUARTER_TILE_SIZE);
                            if (currentLevel.isTileBlocked(nextTileAgain)) {
                                blocked = true;
                            } else {
                                block.move(moveVector);
                                playerIsPushing = true;
                                soundPlayer.playSound("sound/block-3.ogg", false, 0.5f, MathUtils.random(0.9f, 1.1f));
                            }
                        }
                    }
                    if (!blocked) {
                        isMoving = true;

                        if (!wasMoving) {
                            walkAnimDelta = 0;
                        }
                        if (inputVector.x < 0 && !playerFacingLeft) {
                            playerFacingLeft = true;
                        }
                        if (inputVector.x > 0 && playerFacingLeft) {
                            playerFacingLeft = false;
                        }
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
                    if (!sceneBlock && !castLock && !isPlayerShooting) {
//                        castCurrentSpell();
                        isPlayerShooting = true;
                        playerShootingTimer = PLAYER_SHOOTING_TIME;
                        animationDelta = 0;
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
            if (isMoving) {
                float pitch = MathUtils.random(0.75f, 1.25f);
                if (stepTimer < 0) {
                    soundPlayer.playSound("sound/step-2.ogg", false, 0.7f, pitch);
                    stepTimer = 0.165f * 2f;
                }
            }
        } else {
            dialogLock = false;
            moveLock = false;
            skipLock = false;
            fightInputLock = false;
            wasMoving = false;
            titleLock = false;
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
            isTitleMenu = true;
            isViewDirty = true;
            titleLock = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restartLevel();
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.COMMA) && !Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            levelChangeLock = false;
        }
        if (!levelChangeLock && Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            goToPreviousLevel();
            levelChangeLock = true;
        }
        if (!levelChangeLock && Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            goToNextLevel();
            levelChangeLock = true;
        }
    }

    private void castCurrentSpell() {
        if (castCooldown > 0) {
            return;
        }
        if (currentSpell != null && currentSpell.equals("arrow")) {
            Vector2 nextTilePos = playerDir.cpy().scl(TILE_SIZE).add(playerPos);
            addArrow(nextTilePos, playerDir, PLAYER_ARROW_SPEED);
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
                levelActor.isWalking = true;
                if (value.x > 0) {
                    levelActor.isFacingRight = true;
                }
                if (value.x < 0) {
                    levelActor.isFacingRight = false;
                }
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

    void addArrow(Vector2 pos, Vector2 dir, float speed) {
        soundPlayer.playSound("sound/blast-1.ogg", false, 0.3f,  MathUtils.random(0.6f, 1.4f));
        arrows.add(new Arrow(arrowImage, pos, dir, speed));
    }

    void addLazer(Vector2 pos, Vector2 dir) {
        Texture img = dir.x != 0 ? horizontalLazerImage : lazerImage;
        if (lazerSoundTimer <= 0) {
            soundPlayer.playSound("sound/lazer-4.ogg", false, 6.0f, 1.0f);
            lazerSoundTimer = 0.5f;
        }
        arrows.add(new Arrow(img, pos, dir, TILE_SIZE * 16.0f));
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
        if (poster != null && poster.equals("poster-prize.png")) {
            currentSpell = "arrow";
        }
    }

    public void setScreenFade(float amount, Color color) {
        screenFade = amount;
        fadeColor = color;
    }

    public void fadeScreen(boolean inDirection, float time, Color color) {
        screenFader.fadeScreen(inDirection, time, color);
    }

    @Override
    public void goToConnection(String target) {
        for (Level level : levels) {
            if (level.hasConnection(target)) {
                Connection connection = level.getConnection(target);
                startLevel(level, connection);
                break;
            }
        }
    }

    public boolean isArrowBlocking(Vector2 checkPos) {
        return currentLevel.isTileBlocked(checkPos);
    }
}
