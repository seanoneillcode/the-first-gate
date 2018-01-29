package com.lovely.games.scene;

import static com.lovely.games.scene.Scene.builder;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class SceneContainer {

    public Map<String, Scene> scenes;

    public SceneContainer() {
        scenes = new HashMap<>();
        scenes.put("1", builder()
                .verb(new WaitVerb(1))
                .verb(new CameraVerb(new Vector2(400,100)))
                .verb(new WaitVerb(1))
                .verb(new DialogVerb("6"))
                .build());
    }

}
