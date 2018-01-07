package com.lovely.games;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Level {

    static final float TILE_SIZE = 32f;
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

    Level(List<Connection> connections, boolean[][] walls, boolean[][] deaths, String name, int numXTiles,
                 int numYTiles, List<ArrowSource> arrowSources, List<Platform> platforms, List<Block> blocks) {
        this.connections = connections;
        this.walls = walls;
        this.deaths = deaths;
        this.name = name;
        this.numXTiles = numXTiles;
        this.numYTiles = numYTiles;
        this.arrowSources = arrowSources;
        this.platforms = platforms;
        this.blocks = blocks;
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

    /**
     * BUILDEr
     */
    private static class Builder {
        List<Connection> connections = new ArrayList<>();
        List<ArrowSource> arrowSources = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();
        boolean[][] walls;
        boolean[][] deaths;
        String name;
        int numXTiles = 0;
        int numYTiles = 0;
        List<Platform> platforms = new ArrayList<>();

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

        Builder addArrowSource(Vector2 dir, Vector2 pos, float offset, float delay) {
            this.arrowSources.add(new ArrowSource(pos, dir, offset, delay));
            return this;
        }

        Builder addPlatform(Platform platform) {
            this.platforms.add(platform);
            return this;
        }

        Builder addBlock(Block block) {
            this.blocks.add(block);
            return this;
        }

        Level build() {
            if (walls == null) {
                walls = new boolean[numXTiles][numYTiles];
            }
            if (deaths == null) {
                deaths = new boolean[numXTiles][numYTiles];
            }
            return new Level(connections, walls, deaths, name, numXTiles, numYTiles, arrowSources, platforms, blocks);
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
                Vector2 midPos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                builder.addArrowSource(dir, midPos, offset, delay);
            }
            if (properties.containsKey("type") && properties.get("type").equals("platform")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 start = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                Vector2 end = start.cpy();
                float offset = 0;
                if (properties.containsKey("movex")) {
                    end.x = end.x + (TILE_SIZE * Float.parseFloat(properties.get("movex").toString()));
                }
                if (properties.containsKey("movey")) {
                    end.y = end.y + (TILE_SIZE * Float.parseFloat(properties.get("movey").toString()));
                }
                if (properties.containsKey("offset")) {
                    offset = Float.parseFloat(properties.get("offset").toString());
                }
                builder.addPlatform(new Platform(start, end, offset));
            }
            if (properties.containsKey("type") && properties.get("type").equals("block")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Vector2 pos = new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y);
                builder.addBlock(new Block(pos));
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
        for (int y = 0; y < tiledLayer.getHeight(); y++) {
            for (int x = 0; x < tiledLayer.getWidth(); x++) {
                deaths[x][y] = tiledLayer.getCell(x,y) != null;
            }
        }
        builder.setDeaths(deaths);

        return builder.build();
    }
}
