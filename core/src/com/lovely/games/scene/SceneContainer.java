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
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant"))
                .verb(new WaitVerb(1f))
                .verb(new CameraVerb(new Vector2(300,100)))
                .verb(new WaitVerb(0.5f))
                .verb(new DialogVerb("6"))
                .build());

        scenes.put("2", builder()
                .verb(new CameraVerb(new Vector2(300,100)))
                .verb(new WaitVerb(0.5f))
                .verb(new SendEventVerb("a"))
                .verb(new WaitVerb(0.5f))
                .verb(new CameraVerb(new Vector2(100,100)))
                .verb(new WaitVerb(0.5f))
                .verb(new SendEventVerb("b"))
                .verb(new DialogVerb("7"))
                .build());
    }

}
