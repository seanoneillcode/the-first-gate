package com.lovely.games.scene;

import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lovely.games.Stage;

public class MoveVerb implements SceneVerb {

    float speed = TILE_SIZE * 3.0f;
    Vector2 pos, target;
    boolean isDone;
    String actor;

    public MoveVerb(Vector2 pos, Vector2 target, String actor) {
        this.pos = pos;
        this.target = target;
        this.isDone = false;
        this.actor = actor;
    }

    @Override
    public void update(Stage stage) {
        if (Math.abs(pos.x - target.x) < 2 && Math.abs(pos.y - target.y) < 2 ) {
            isDone = true;
        } else {
            Vector2 dir = target.cpy().sub(pos).scl(-1).nor();
            pos = pos.add(dir.scl(Gdx.graphics.getDeltaTime() * speed));
            stage.moveActor(actor, pos);
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
