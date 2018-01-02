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

    public static final int NUM_X_TILES = 15;
    public static final int NUM_Y_TILES = 10;
    public static final float TILE_SIZE = 32f;

    List<Connection> connections;
    boolean[][] walls;
    boolean[][] deaths;
    String name;

    public Level(List<Connection> connections, boolean[][] walls, boolean[][] deaths, String name) {
        this.connections = connections;
        this.walls = walls;
        this.deaths = deaths;
        this.name = name;
    }

    public Vector2 getConnectionPosition(String name) {
        for (Connection connection : connections) {
            if (connection.name.equals(name)) {
                return connection.pos.cpy();
            }
        }
        return null;
    }

    public boolean canMove(Vector2 pos) {
        int tilex = MathUtils.floor(pos.x / TILE_SIZE);
        int tiley = MathUtils.floor(pos.y / TILE_SIZE);

        if (tilex < 0 || tiley < 0 || tilex >= NUM_X_TILES || tiley >= NUM_Y_TILES) {
            return false;
        }
        return !walls[tilex][tiley];
    }

    public boolean isDeath(Vector2 pos) {
        int tilex = MathUtils.floor(pos.x / TILE_SIZE);
        int tiley = MathUtils.floor(pos.y / TILE_SIZE);

        if (tilex < 0 || tiley < 0 || tilex >= NUM_X_TILES || tiley >= NUM_Y_TILES) {
            return false;
        }
        return deaths[tilex][tiley];
    }

    public Connection getConnection(Vector2 pos) {
        for (Connection connection : connections) {
            if(connection.contains(pos)) {
                return connection;
            }
        }
        return null;
    }

    public boolean hasConnection(String name) {
        for (Connection connection : connections) {
            if (connection.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static class Builder {
        List<Connection> connections = new ArrayList<Connection>();
        boolean[][] walls = new boolean[NUM_X_TILES][NUM_Y_TILES];
        boolean[][] deaths = new boolean[NUM_X_TILES][NUM_Y_TILES];
        String name;

        Builder(String name) {
            this.name = name;
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

        Level build() {
            return new Level(connections, walls, deaths, name);
        }
    }

    static Level loadLevel(AssetManager assetManager, String name) {
        TiledMap tiledMap = assetManager.get(name);
        Builder builder = new Builder(name);
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
        }

        // walls
        boolean[][] walls = new boolean[NUM_X_TILES][NUM_Y_TILES];
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");
        for (int y = 0; y < tiledLayer.getHeight(); y++) {
            for (int x = 0; x < tiledLayer.getWidth(); x++) {
                walls[x][y] = tiledLayer.getCell(x,y) != null;
            }
        }
        builder.setWalls(walls);

        // deaths
        boolean[][] deaths = new boolean[NUM_X_TILES][NUM_Y_TILES];
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
