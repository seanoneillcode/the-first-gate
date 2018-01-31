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
                .verb(new DialogVerb("6"))
                .verb(new MoveVerb(new Vector2(4 * 32, 0), "ant", false))
                .verb(new MoveVerb(new Vector2(0, 32), "ant", false))
                .build());

        scenes.put("2", builder()
                .verb(new CameraVerb(new Vector2(200,340)))
                .verb(new WaitVerb(0.5f))
                .verb(new SendEventVerb("a"))
                .verb(new WaitVerb(0.5f))
                .verb(new CameraVerb(new Vector2(200,96)))
                .verb(new WaitVerb(0.5f))
                .verb(new SendEventVerb("b"))
                .verb(new DialogVerb("7"))
                .verb(new CameraVerb(new Vector2(200,340)))
                .verb(new MoveVerb(new Vector2(0, 32), "ant", false))
                .verb(new MoveVerb(new Vector2(-4 * 32, 0), "ant", false))
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant", false))
                .build());

        scenes.put("3", builder()
                .verb(new DialogVerb("8"))
                .build());

        scenes.put("4", builder()
                .verb(new DialogVerb("9"))
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant", false))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

        scenes.put("5", builder()
                .verb(new CameraVerb(new Vector2(7 * 32, 32 * 8)))
                .verb(new DialogVerb("10"))
                .verb(new WaitVerb(0.5f))
                .verb(new MoveVerb(new Vector2(6 * 32, 0), "ant", false))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

    }

}
