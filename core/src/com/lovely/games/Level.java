package com.lovely.games;


import static com.lovely.games.TheFirstGate.TILE_SIZE;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lovely.games.scene.Scene;
import com.lovely.games.scene.SceneSource;

class Level {

    static final int DEFAULT_DELAY = 1;

    List<Connection> connections;
    List<ArrowSource> arrowSources;
    List<Block> blocks;
    boolean[][] walls;
    boolean[][] deaths;
    List<Platform> platforms;
    String name;
    int numXTiles;
    int numYTiles;
    List<PressureTile> pressureTiles;
    List<Door> doors;
    List<LevelLight> lights;
    List<DialogSource> dialogSources;
    List<Torch> torches;
    List<SceneSource> scenes;
    Trunk trunk;
    List<Actor> actors;

    Level(List<Connection> connections, boolean[][] walls, boolean[][] deaths, String name, int numXTiles,
                 int numYTiles, List<ArrowSource> arrowSources, List<Platform> platforms, List<Block> blocks,
          List<PressureTile> pressureTiles, List<Door> doors, List<DialogSource> dialogSources,
          List<LevelLight> lights, List<Torch> torches, List<SceneSource> scenes, Trunk trunk, List<Actor> actors) {
        this.connections = connections;
        this.walls = walls;
        this.deaths = deaths;
        this.name = name;
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;
        this.arrowSources = arrowSources;
        this.platforms = platforms;
        this.blocks = blocks;
        this.pressureTiles = pressureTiles;
        this.doors = doors;
        this.dialogSources = dialogSources;
        this.lights = lights;
        this.torches = torches;
        this.scenes = scenes;
        this.trunk = trunk;
        this.actors = actors;
    }

    Vector2 getConnectionPosition(String name) {
        for (Connection connection : connections) {
            if (connection.name.equals(name)) {
                return connection.pos.cpy();
            }
        }
        return null;
    }

    boolean isWall(Vector2 pos) {
        int tilex = MathUtils.floor(pos.x / TILE_SIZE);
        int tiley = MathUtils.floor(pos.y / TILE_SIZE);

        if (tilex < 0 || tiley < 0 || tilex >= numXTiles || tiley >= numYTiles) {
            return false;
        }
        return walls[tilex][tiley];
    }

    Block getBlock(Vector2 pos, boolean noGround) {
        for (Block block : blocks) {
            if (!(noGround && block.isGround)) {
                if (pos.dst2(block.pos) < 256) {
                    return block;
                }
            }
        }
        return null;
    }

    DialogSource getDialogSource(Vector2 pos) {
        for (DialogSource dialogSource : dialogSources) {
            if (!dialogSource.done && pos.dst2(dialogSource.pos) < 256) {
                return dialogSource;
            }
        }
        return null;
    }

    List<SceneSource> getSceneSources(Vector2 pos) {
        List<SceneSource> sceneSources = new ArrayList<>();
        for (SceneSource sceneSource : scenes) {
            if (!sceneSource.isDone && sceneSource.isActive) {
                Rectangle playerRect = new Rectangle(pos.x + 1, pos.y + 1, 20, 20);
                if (playerRect.overlaps(new Rectangle(sceneSource.pos.x, sceneSource.pos.y, sceneSource.size.x, sceneSource.size.y))) {
                    System.out.println("layer ovralps scne " + sceneSource.id);
                    sceneSource.isDone = true;
                    sceneSources.add(sceneSource);
                }
            }
        }
        return sceneSources;
    }

    Door getDoor(Vector2 pos, boolean isOpen) {
        for (Door door : doors) {
            if (door.isOpen == isOpen) {
                if (pos.dst2(door.pos) < 256) {
                    return door;
                }
            }
        }
        return null;
    }

    boolean isTileBlocked(Vector2 pos) {
        Block block = getBlock(pos, true);
        Door door = getDoor(pos, false);
        return isWall(pos) || block != null || door != null;
    }

    boolean isDeath(Vector2 pos) {
        int tilex = MathUtils.floor(pos.x / TILE_SIZE);
        int tiley = MathUtils.floor(pos.y / TILE_SIZE);

        if (tilex < 0 || tiley < 0 || tilex >= numXTiles || tiley >= numYTiles) {
            return false;
        }
        return deaths[tilex][tiley];
    }

    boolean isOutOfBounds(Vector2 pos) {
        return pos.x > (numXTiles * TILE_SIZE) || pos.x < 0 || pos.y < 0 || pos.y > (numYTiles * TILE_SIZE);
    }

    List<Platform> getPlatforms() {
        return platforms;
    }

    Platform getPlatform(Vector2 pos) {
        for (Platform platform : platforms) {
            if (pos.dst2(platform.pos) < 256) {
                return platform;
            }
        }
        return null;
    }

    Connection getConnection(Vector2 pos) {
        for (Connection connection : connections) {
            if(connection.contains(pos)) {
                return connection;
            }
        }
        return null;
    }

    boolean hasConnection(String name) {
        for (Connection connection : connections) {
            if (connection.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    List<ArrowSource> getArrowSources() {
        return this.arrowSources;
    }

    public void resetSceneSources(Vector2 pos) {
        for (SceneSource sceneSource : scenes) {
            if (sceneSource.isDone && sceneSource.isActive && !sceneSource.isPlayOnce()) {
                Rectangle playerRect = new Rectangle(pos.x + 1, pos.y + 1, 20, 20);
                if (!playerRect.overlaps(new Rectangle(sceneSource.pos.x, sceneSource.pos.y, sceneSource.size.x, sceneSource.size.y))) {
                    sceneSource.isDone = false;
                }
            }
        }
    }

    /**
     * BUILDEr
     */
    private static class Builder {
        List<Connection> connections = new ArrayList<>();
        List<ArrowSource> arrowSources = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();
        List<PressureTile> pressureTiles = new ArrayList<>();
        boolean[][] walls;
        boolean[][] deaths;
        String name;
        int numXTiles = 0;
        int numYTiles = 0;
        List<Platform> platforms = new ArrayList<>();
        Trunk trunk = new Trunk();
        List<Door> doors = new ArrayList<>();
        List<DialogSource> dialogSources = new ArrayList<>();
        List<LevelLight> lights = new ArrayList<>();
        List<Torch> torches = new ArrayList<>();
        List<SceneSource> scenes = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();

        Builder(String name, int numXTiles, int numYTiles) {
            this.name = name;
            this.numXTiles = numXTiles;
            this.numYTiles = numYTiles;
        }

        Builder addConnection(String name, String to, Vector2 pos) {
            this.connections.add(new Connection(name, to, pos));
            return this;
        }

        Builder setWalls(boolean[][] walls) {
            this.walls = walls;
            return this;
        }

        Builder setDeaths(boolean[][] deaths) {
            this.deaths = deaths;
            return this;
        }

        Builder addArrowSource(Vector2 dir, Vector2 pos, float offset, float delay, boolean isActive, String switchId) {
            ArrowSource arrowSource = new ArrowSource(pos, dir, offset, delay, isActive, switchId);
            if (switchId != null) {
                trunk.addListener(arrowSource);
            }
            this.arrowSources.add(arrowSource);
            return this;
        }

        Builder addPlatform(Platform platform) {
            this.platforms.add(platform);
            if (platform.switchId != null) {
                trunk.addListener(platform);
            }
            return this;
        }

        Builder addBlock(Block block) {
            this.blocks.add(block);
            return this;
        }

        Builder addDoor(Door door) {
            doors.add(door);
            if (door.switchId != null) {
                trunk.addListener(door);
            }
            return this;
        }

        Builder addTorch(Vector2 pos, Color color, boolean isFire) {
            this.torches.add(new Torch(pos, color, isFire));
            return this;
        }

        Builder addPressureTile(PressureTile pressureTile) {
            this.pressureTiles.add(pressureTile);
            if (pressureTile.switchId != null) {
                pressureTile.setTrunk(trunk);
            }
            return this;
        }

        Builder addDialogSource(DialogSource dialogSource) {
            this.dialogSources.add(dialogSource);
            return this;
        }

        Builder addLight(LevelLight levelLight) {
            lights.add(levelLight);
            return this;
        }

        Builder addActor(Vector2 pos, String id, boolean isHide) {
            actors.add(new Actor(pos, id, isHide));
            return this;
        }

        Builder addScene(String id, Vector2 pos, Vector2 size, boolean playOnce, boolean isActive, String switchId) {
            SceneSource sceneSource = new SceneSource(pos, id, size, playOnce, isActive, switchId);
            if (switchId != null) {
                trunk.addListener(sceneSource);
            }
            this.scenes.add(sceneSource);
            return this;
        }

        Trunk getTrunk() {
            return this.trunk;
        }

        Level build() {
            if (walls == null) {
                walls = new boolean[numXTiles][numYTiles];
            }
            if (deaths == null) {
                deaths = new boolean[numXTiles][numYTiles];
            }
            return new Level(connections, walls, deaths, name, numXTiles, numYTiles, arrowSources, platforms, blocks,
                    pressureTiles, doors, dialogSources, lights, torches, scenes, trunk, actors);
        }
    }

    static Level loadLevel(AssetManager assetManager, String name) {
        TiledMap tiledMap = assetManager.get(name);
        MapProperties mapProperties = tiledMap.getProperties();
        int levelWidth = (Integer) mapProperties.get("width");
        int levelHeight = (Integer) mapProperties.get("height");
        Builder builder = new Builder(name, levelWidth, levelHeight);

        MapLayer objectLayer = tiledMap.getLayers().get("objects");
        MapObjects mapObjects = objectLayer.getObjects();

        // connections
        for (MapObject obj : mapObjects) {
            MapProperties properties = obj.getProperties();
            if (properties.containsKey("type") && properties.get("type").equals("connection")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                String objName = obj.getName();
                String to = null;
                if (properties.containsKey("to")) {
                    to = properties.get("to").toString();
                }
                builder.addConnection(objName, to, new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y));
            }
            if (properties.containsKey("type") && properties.get("type").equals("arrowSource")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 dir = new Vector2(0, 0);
                float offset = 0;
                float delay = DEFAULT_DELAY;
                if (properties.containsKey("xdir")) {
                    dir.x = Integer.parseInt(properties.get("xdir").toString());
                }
                if (properties.containsKey("ydir")) {
                    dir.y = Integer.parseInt(properties.get("ydir").toString());
                }
                if (properties.containsKey("offset")) {
                    offset = Float.parseFloat(properties.get("offset").toString());
                }
                if (properties.containsKey("delay")) {
                    delay = Float.parseFloat(properties.get("delay").toString());
                }
                boolean isActive = true;
                if (properties.containsKey("isActive")) {
                    isActive = Boolean.parseBoolean(properties.get("isActive").toString());
                }
                String switchId = null;
                if (properties.containsKey("switch")) {
                    switchId = properties.get("switch").toString();
                }
                Vector2 midPos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                builder.addArrowSource(dir, midPos, offset, delay, isActive, switchId);
            }
            if (properties.containsKey("type") && properties.get("type").equals("platform")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 start = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                Vector2 end = start.cpy();
                float offset = 0;
                boolean isActive = true;
                if (properties.containsKey("movex")) {
                    end.x = end.x + (TILE_SIZE * Float.parseFloat(properties.get("movex").toString()));
                }
                if (properties.containsKey("movey")) {
                    end.y = end.y + (TILE_SIZE * Float.parseFloat(properties.get("movey").toString()));
                }
                if (properties.containsKey("offset")) {
                    offset = Float.parseFloat(properties.get("offset").toString());
                }
                if (properties.containsKey("isActive")) {
                    isActive = Boolean.parseBoolean(properties.get("isActive").toString());
                }
                String switchId = null;
                if (properties.containsKey("switch")) {
                    switchId = properties.get("switch").toString();
                }
                Platform platform = new Platform(start, end, offset, isActive, switchId);
                builder.addPlatform(platform);
            }
            if (properties.containsKey("type") && properties.get("type").equals("block")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                builder.addBlock(new Block(pos));
            }
            if (properties.containsKey("type") && properties.get("type").equals("torch")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                boolean isFire = false;
                if (properties.containsKey("isFire")) {
                    isFire = Boolean.parseBoolean(properties.get("isFire").toString());
                }
                Color color = new Color(1.0f,0.75f,0.25f,1.0f);
                if (properties.containsKey("r")) {
                    float r = Float.valueOf(properties.get("r").toString());
                    float g = Float.valueOf(properties.get("g").toString());
                    float b = Float.valueOf(properties.get("b").toString());
                    float a = Float.valueOf(properties.get("a").toString());
                    color = new Color(r, g, b, a);
                }
                builder.addTorch(pos, color, isFire);
            }
            if (properties.containsKey("type") && properties.get("type").equals("actor")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                boolean isHide = false;
                if (properties.containsKey("isHide")) {
                    isHide = Boolean.parseBoolean(properties.get("isHide").toString());
                }
                builder.addActor(pos, rectObj.getName(), isHide);
            }
            if (properties.containsKey("type") && properties.get("type").equals("door")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                String switchId = null;
                if (properties.containsKey("switch")) {
                    switchId = properties.get("switch").toString();
                }
                boolean isOpen = false;
                if (properties.containsKey("isOpen")) {
                    isOpen = Boolean.parseBoolean(properties.get("isOpen").toString());
                }
                builder.addDoor(new Door(pos, isOpen, switchId));
            }
            if (properties.containsKey("type") && properties.get("type").equals("light")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                Vector2 size = new Vector2(rectObj.getRectangle().width, rectObj.getRectangle().height);

                Color color = null;
                if (properties.containsKey("r")) {
                    float r = Float.valueOf(properties.get("r").toString());
                    float g = Float.valueOf(properties.get("g").toString());
                    float b = Float.valueOf(properties.get("b").toString());
                    float a = Float.valueOf(properties.get("a").toString());
                    color = new Color(r, g, b, a);
                }
                builder.addLight(new LevelLight(pos, size, color));
            }
            if (properties.containsKey("type") && properties.get("type").equals("scene")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                Vector2 size = new Vector2(rectObj.getRectangle().width, rectObj.getRectangle().height);
                boolean playOnce = true;
                boolean isActive = true;
                if (properties.containsKey("playOnce")) {
                    playOnce = Boolean.parseBoolean(properties.get("playOnce").toString());
                }
                if (properties.containsKey("isActive")) {
                    isActive = Boolean.parseBoolean(properties.get("isActive").toString());
                }
                String switchId = null;
                if (properties.containsKey("switch")) {
                    switchId = properties.get("switch").toString();
                }
                builder.addScene(obj.getName(), pos, size, playOnce, isActive, switchId);
            }
            if (properties.containsKey("type") && properties.get("type").equals("pressure")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                String switchId = null;
                if (properties.containsKey("switch")) {
                    switchId = properties.get("switch").toString();
                } else {
                    System.out.println("found pressure tile missing switchlink x, y: " + pos.x + ", " + pos.y);
                }
                boolean isSwitch = false;
                if (properties.containsKey("isSwitch")) {
                    isSwitch = Boolean.parseBoolean(properties.get("isSwitch").toString());
                }
                builder.addPressureTile(new PressureTile(pos, switchId, isSwitch));
            }
            if (properties.containsKey("type") && properties.get("type").equals("dialog")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                String id = obj.getName();
                builder.addDialogSource(new DialogSource(pos, id));
            }
        }

        // walls
        boolean[][] walls = new boolean[levelWidth][levelHeight];
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");
        for (int y = 0; y < tiledLayer.getHeight(); y++) {
            for (int x = 0; x < tiledLayer.getWidth(); x++) {
                walls[x][y] = tiledLayer.getCell(x,y) != null;
            }
        }
        builder.setWalls(walls);

        // deaths
        boolean[][] deaths = new boolean[levelWidth][levelHeight];
        tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("death");
        if (tiledLayer != null) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                for (int x = 0; x < tiledLayer.getWidth(); x++) {
                    deaths[x][y] = tiledLayer.getCell(x,y) != null;
                }
            }
            builder.setDeaths(deaths);
        }

        return builder.build();
    }
}
