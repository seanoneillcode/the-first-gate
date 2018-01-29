package com.lovely.games;

import com.badlogic.gdx.math.Vector2;
import com.lovely.games.scene.DialogVerb;

public interface Stage {

    void startDialog(String id, DialogVerb dialogVerb);

    void moveActor(String actor, Vector2 pos);

    void moveCamera(Vector2 pos);

    void resetCamera();
}
