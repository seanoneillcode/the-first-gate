package org.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EntManager {

    List<Ent> ents;

    EntManager() {
        ents = new ArrayList<>();
    }

    public Ent addEnt(Vector2 pos, Vector2 size, Vector2 offset, boolean needsGround) {
        Ent ent = new Ent(pos, size, offset, needsGround);
        ents.add(ent);
        return ent;
    }

    public void update(LevelManager levelManager) {
        boolean hasDeadEnts = false;
        for (Ent ent : ents) {
            if (ent.state == Ent.EntState.ALIVE && ent.needsGround && !isOnGround(levelManager, ent.pos.cpy().add(ent.offset), ent.size)) {
                ent.fall();
            }
            if (ent.state == Ent.EntState.FALLING) {
                ent.fallTimer = ent.fallTimer - Gdx.graphics.getDeltaTime();
                if (ent.fallTimer < 0) {
                    ent.state = Ent.EntState.DEAD;
                    hasDeadEnts = true;
                }
            }
            if (ent.state == Ent.EntState.JUMPING) {
                if (ent.jumpTimer < 0) {
                    ent.state = Ent.EntState.ALIVE;
                }
                ent.pos = ent.pos.cpy().add(ent.physics);
            }
            ent.jumpTimer = ent.jumpTimer - Gdx.graphics.getDeltaTime();
            ent.delta = ent.delta + Gdx.graphics.getDeltaTime();
        }
        if (hasDeadEnts) {
            ents.removeIf(ent -> ent.state == Ent.EntState.DEAD);
        }
    }

    private boolean isOnGround(LevelManager levelManager, Vector2 pos, Vector2 size) {
        for (Tile tile : levelManager.tiles) {
            if (tile.isGround && isOverlap(tile.pos, tile.size, pos, size)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlap(Vector2 p1, Vector2 s1, Vector2 p2, Vector2 s2) {
        Rectangle rect1 = new Rectangle(p1.x, p1.y, s1.x, s1.y);
        Rectangle rect2 = new Rectangle(p2.x, p2.y, s2.x, s2.y);
        return rect1.overlaps(rect2);
    }

}
