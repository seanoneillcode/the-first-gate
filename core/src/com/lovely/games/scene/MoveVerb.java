package com.lovely.games.scene;

import static com.lovely.games.TheFirstGate.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lovely.games.Stage;

public class MoveVerb implements SceneVerb {

    float speed = TILE_SIZE * 2.0f;
    Vector2 pos, amount, total;
    boolean isDone;
    String actor;

    public MoveVerb(Vector2 amount, String actor) {
        this.amount = amount;
        this.pos = amount.cpy();
        this.total = new Vector2();
        this.pos.x = pos.x / speed;
        this.pos.y = pos.y / speed;
        this.isDone = false;
        this.actor = actor;
    }

    @Override
    public void update(Stage stage) {
        if (total.dst2(amount) < 16) {
            isDone = true;
        } else {
            pos.cpy().scl(Gdx.graphics.getDeltaTime());
            total.add(pos);
            stage.moveActor(actor, pos);
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
