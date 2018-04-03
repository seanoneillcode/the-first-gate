package org.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lovely.games.LevelManager.TILE_SIZE;
import static org.lovely.games.LoadingManager.*;

public class LevelGenerator {

    private static final int MAX_PLATFORM_SIZE = 12;
    private static Vector2 TILE_SIZE_VEC = new Vector2(TILE_SIZE, TILE_SIZE);
    private static List<String> GRASS_TILES = Arrays.asList(GRASS_TILE_2, GRASS_TILE_3, GRASS_TILE_2, GRASS_TILE_3, GRASS_TILE_8,
            GRASS_TILE_4, GRASS_TILE_5, GRASS_TILE_6, GRASS_TILE_7, GRASS_TILE_8, GRASS_TILE_2, GRASS_TILE_3, GRASS_TILE_2, GRASS_TILE_3, GRASS_TILE_8,
            GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE_9, GRASS_TILE, GRASS_TILE);

    public List<Tile> generate(int points, int mapSize) {
        List<Tile> tiles = new ArrayList<>();
        Vector2 prev = new Vector2();
        for (int i = 0; i < points; i++) {
            Vector2 next = new Vector2(MathUtils.random(0, mapSize), MathUtils.random(0, mapSize));
            tiles.addAll(addLine(prev, next));
            prev = next.cpy();
        }
        tiles.sort((o1, o2) -> {
            if (o1.isGround && !o2.isGround) {
                return 1;
            }
            if (!o1.isGround && o2.isGround) {
                return -1;
            }
            return 0;
        });
        return tiles;
    }

    private List<Tile> addLine(Vector2 startPos, Vector2 endPos) {
        List<Tile> tiles = new ArrayList<>();
        Vector2 dir = new Vector2();
        if (startPos.x < endPos.x) {
            dir.x = 1;
        }
        if (startPos.x > endPos.x) {
            dir.x = -1;
        }
        if (startPos.y < endPos.y) {
            dir.y = 1;
        }
        if (startPos.y > endPos.y) {
            dir.y = -1;
        }
        Vector2 dis = new Vector2(Math.abs(endPos.x - startPos.x), Math.abs(endPos.y - startPos.y));

        for (int i = 0, j = 0; i < dis.x || j < dis.y; ) {
            int width = MathUtils.random(1, MAX_PLATFORM_SIZE);
            int height = MathUtils.random(1, MAX_PLATFORM_SIZE);
            tiles.addAll(addSquare(new Vector2(startPos.cpy().add(i * dir.x, j * dir.y)), new Vector2(width, height)));

            if (i < dis.x) {
                i = i + width + getWeightedJumpRandInt();
            }
            if  (j < dis.y) {
                j = j + height + getWeightedJumpRandInt();
            }
        }

        return tiles;
    }

    private int getWeightedJumpRandInt() {
        float randx = MathUtils.random(0, 1f);
        int randix = 0;
        if (randx > 0.3f) {
            randix = 1;
        }
        if (randx > 0.6f) {
            randix = 2;
        }
        if (randx > 0.8f) {
            randix = 3;
        }
        if (randx > 0.9f) {
            randix = 4;
        }
        return randix;
    }

    private List<Tile> addSquare(Vector2 pos, Vector2 size) {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                Vector2 tilePos = pos.cpy().scl(TILE_SIZE).add(i * TILE_SIZE, j * TILE_SIZE);
                Color color = Color.WHITE;//.cpy().fromHsv(MathUtils.random(180f, 190f), 1, 1);
                tiles.add(new Tile(tilePos, TILE_SIZE_VEC, GRASS_TILES.get(MathUtils.random(0,GRASS_TILES.size() - 1)), true, color));
                if (j == 0) {
                    tiles.add(new Tile(tilePos.cpy().sub(0, TILE_SIZE), TILE_SIZE_VEC, BOTTOM_TILE, false, color));
                }
            }
        }
        return tiles;
    }
}
