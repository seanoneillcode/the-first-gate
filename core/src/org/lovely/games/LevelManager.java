package org.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lovely.games.LoadingManager.*;

public class LevelManager {

    List<Tile> tiles = new ArrayList<>();
    private float animationDelta = 0f;
    public static float TILE_SIZE = 16;
    private int MAP_SIZE = 16;
    private int SQUARE_SIZE = 8;
    Vector2 TILE_SIZE_VEC = new Vector2(TILE_SIZE, TILE_SIZE);
    List<Cloud> clouds = new ArrayList<>();
    List<String> cloundImages = Arrays.asList(CLOUD_0, CLOUD_1, CLOUD_2, CLOUD_3);

    public void start() {
        for (int i = 0; i < 16; i++) {
            Vector2 pos = new Vector2(MathUtils.random(0, MAP_SIZE * TILE_SIZE), MathUtils.random(0, MAP_SIZE * TILE_SIZE));
            Vector2 mov = new Vector2(MathUtils.random(0.1f, 1.0f), 0);
            String img = cloundImages.get(MathUtils.random(cloundImages.size() - 1));
            float scale = mov.x / 1.0f;
            addCloud(pos, img, mov, scale);
        }
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

    private void addCloud(Vector2 pos, String image, Vector2 mov, float scale) {
        clouds.add(new Cloud(pos, image, mov, scale));
    }

    private void addSquare(Vector2 pos, Vector2 size) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                Vector2 tilePos = pos.cpy().scl(TILE_SIZE).add(i * TILE_SIZE, j * TILE_SIZE);
                Color color = Color.WHITE.cpy().fromHsv(MathUtils.random(180f, 190f), 1, 1);
                tiles.add(new Tile(tilePos, TILE_SIZE_VEC, GRASS_TILE, true, color));
                if (j == 0) {
                    tiles.add(new Tile(tilePos.cpy().sub(0, TILE_SIZE), TILE_SIZE_VEC, BOTTOM_TILE, false, color));
                }
            }
        }
    }

    public void update() {
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
        for (Cloud cloud : clouds) {
            cloud.pos.add(cloud.mov);
            if (cloud.pos.x > (MAP_SIZE * TILE_SIZE * 2)) {
                cloud.pos.x = -(MAP_SIZE * TILE_SIZE);
                cloud.pos.y = MathUtils.random(0, MAP_SIZE * TILE_SIZE);
            }
        }
    }

    public Vector2 getStartPos() {
        return tiles.get(tiles.size() - 1).pos.cpy();
    }
}
