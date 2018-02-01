package com.lovely.games.scene;

import static com.lovely.games.scene.Scene.builder;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class SceneContainer {

    public Map<String, Scene> scenes;

    public SceneContainer() {
        scenes = new HashMap<>();

        // ant tells player to stand on switch
        scenes.put("1", builder()
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant"))
                .verb(new DialogVerb("6"))
                .verb(new MoveVerb(new Vector2(4 * 32, 0), "ant", true))
                .verb(new MoveVerb(new Vector2(0, 32), "ant", true))
                .build());

        // player steps on swithc, closes them off
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
                .verb(new MoveVerb(new Vector2(0, 32), "ant", true))
                .verb(new MoveVerb(new Vector2(-4 * 32, 0), "ant", true))
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant", true))
                .build());

        // reads the waring stone in start
        scenes.put("3", builder()
                .verb(new DialogVerb("8"))
                .build());

        // at start, tells player not to follow
        scenes.put("4", builder()
                .verb(new DialogVerb("9"))
                .verb(new MoveVerb(new Vector2(0, 3 * 32), "ant", true))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

        // catching up with ant
        scenes.put("5", builder()
                .verb(new CameraVerb(new Vector2(7 * 32, 32 * 8)))
                .verb(new DialogVerb("10"))
                .verb(new WaitVerb(0.5f))
                .verb(new MoveVerb(new Vector2(6 * 32, 0), "ant", false))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

        // ant fucks over player
        scenes.put("6", builder()
                .verb(new CameraVerb(new Vector2(11 * 32, 32 * 2)))
                .verb(new DialogVerb("11"))
                .verb(new MoveVerb(new Vector2(6 * 32, 0), "ant", false))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

    }

}
