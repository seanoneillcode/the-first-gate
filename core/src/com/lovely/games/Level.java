package com.lovely.games;

import java.util.ArrayList;
import java.util.List;

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

    private Level(List<Connection> connections, boolean[][] walls) {
        this.connections = connections;
        this.walls = walls;
    }

    public Vector2 getConnectionPosition(String name) {
        for (Connection connection : connections) {
            if (connection.name.equals(name)) {
                return connection.pos.cpy();
            }
        }
        return null;
    }

    public boolean canMove(Vector2 to) {
        int tilex = MathUtils.floor(to.x / TILE_SIZE);
        int tiley = MathUtils.floor(to.y / TILE_SIZE);

        if (tilex < 0 || tiley < 0 || tilex >= NUM_X_TILES || tiley >= NUM_Y_TILES) {
            return false;
        }
        return !walls[tilex][tiley];
    }

    private static class Builder {
        List<Connection> connections = new ArrayList<Connection>();
        boolean[][] walls = new boolean[NUM_X_TILES][NUM_Y_TILES];

        Builder addConnection(String name, String to, Vector2 pos) {
            this.connections.add(new Connection(name, to, pos));
            return this;
        }

        Builder setWalls(boolean[][] walls) {
            this.walls = walls;
            return this;
        }

        Level build() {
            return new Level(connections, walls);
        }
    }

    static Level loadLevel(TiledMap tiledMap) {
        Builder builder = new Builder();
        MapLayer objectLayer = tiledMap.getLayers().get("objects");
        MapObjects mapObjects = objectLayer.getObjects();

        // connections
        for (MapObject obj : mapObjects) {
            MapProperties properties = obj.getProperties();
            if (properties.containsKey("type") && properties.get("type").equals("connection")) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                String name = obj.getName();
                String to = null;
                if (properties.containsKey("to")) {
                    to = properties.get("to").toString();
                }
                builder.addConnection(name, to, new Vector2(rectObj.getRectangle().x, rectObj.getRectangle().y));
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

        return builder.build();
    }
}
