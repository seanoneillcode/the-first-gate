package org.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static org.lovely.games.LoadingManager.BOTTOM_TILE;
import static org.lovely.games.LoadingManager.GRASS_TILE;

public class LevelManager {

    List<Tile> tiles = new ArrayList<>();
    private float animationDelta = 0f;
    private float TILE_SIZE = 16;
    private int MAP_SIZE = 16;
    private int SQUARE_SIZE = 8;
    Vector2 TILE_SIZE_VEC = new Vector2(TILE_SIZE, TILE_SIZE);

    public void start() {
        for (int i = 0; i < 10; i++) {
            Vector2 pos = new Vector2(MathUtils.random(0, MAP_SIZE), MathUtils.random(0, MAP_SIZE));
            Vector2 size = new Vector2(MathUtils.random(1, SQUARE_SIZE), MathUtils.random(1, SQUARE_SIZE));
            addSquare(pos, size);
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
    }

    private void addSquare(Vector2 pos, Vector2 size) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                Vector2 tilePos = pos.cpy().scl(TILE_SIZE).add(i * TILE_SIZE, j * TILE_SIZE);
                tiles.add(new Tile(tilePos, TILE_SIZE_VEC, GRASS_TILE, true));
                if (j == 0) {
                    tiles.add(new Tile(tilePos.cpy().sub(0, TILE_SIZE), TILE_SIZE_VEC, BOTTOM_TILE, false));
                }
            }
        }
    }

    public void update() {
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
    }
}
