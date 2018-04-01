package org.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static org.lovely.games.LoadingManager.GRASS_TILE;

public class LevelManager {

    List<Tile> tiles = new ArrayList<>();
    private float animationDelta = 0f;
    private float TILE_SIZE = 16;

    public void start() {
        addSquare(new Vector2(3, 4), new Vector2(6, 2));
    }

    private void addSquare(Vector2 pos, Vector2 size) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                Vector2 tilePos = pos.cpy().scl(TILE_SIZE).add(i * TILE_SIZE, j * TILE_SIZE);
                tiles.add(new Tile(tilePos, GRASS_TILE));
            }
        }
    }

    public void update() {
        animationDelta = animationDelta + Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch, LoadingManager loadingManager) {
        for (Tile tile : tiles) {
            TextureRegion frame = loadingManager.getAnim(tile.image).getKeyFrame(animationDelta, true);
            batch.draw(frame, tile.pos.x, tile.pos.y);
        }
    }
}
