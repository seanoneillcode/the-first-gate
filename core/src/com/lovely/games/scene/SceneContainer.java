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

        scenes.put("7", builder()
                .resetCamera(false)
                .verb(new SendEventVerb("c"))
                .build());

        // ant fucks over player
        scenes.put("6", builder()
                .verb(new CameraVerb(new Vector2(11 * 32, 32 * 4)))
                .verb(new WaitVerb(2f))
                .verb(new MoveVerb(new Vector2(3 * 32, 0), "ant", true))
                .verb(new WaitVerb(2f))
                .verb(new DialogVerb("11"))
                .verb(new MoveVerb(new Vector2(0, -5 * 32), "ant", false))
                .verb(new MoveVerb(new Vector2(-5 * 32, 0), "ant", false))
                .verb(new MoveVerb(new Vector2(0, -3 * 32), "ant", false))
                .verb(new HideShowActorVerb(true, "ant"))
                .build());

        // prize fight - before confrontation
        scenes.put("8", builder()
                .verb(new HideShowActorVerb(false, "ant"))
                .verb(new MoveVerb(new Vector2(32 * 8, 0), "ant", true))
                .verb(new DialogVerb("12"))
                .build());

        scenes.put("9", builder()
                .verb(new MoveVerb(new Vector2(0, 32 * -2), "ant", true))
                .verb(new SendEventVerb("a"))
                .verb(new FightVerb("prize"))
                .build());

        // ant asks for help
        scenes.put("10", builder()
                .verb(new CameraVerb(new Vector2(3 * 32, 32 * 8)))
                .verb(new HideShowActorVerb(false, "ant"))
                .verb(new MoveVerb(new Vector2(2 * 32, 0), "ant", true))
                .verb(new MoveVerb(new Vector2(0, 32 * 5), "ant", true))
                .verb(new MoveVerb(new Vector2(2 * 32, 0), "ant", true))
                .verb(new DialogVerb("14"))
                .verb(new SendEventVerb("b"))
                .build());

        // pro feels the magic
        scenes.put("11", builder()
                .verb(new DialogVerb("13"))
                .build());

        // pro walks away to leave ant to die
        scenes.put("12", builder()
                .verb(new DialogVerb("15"))
                .build());

        // pro is a kind fool!
        scenes.put("13", builder()
                .verb(new DialogVerb("16"))
                .verb(new WaitVerb(3f))
                .verb(new MoveVerb(new Vector2(9 * 32, 0), "ant", true))
                .verb(new DialogVerb("17"))
                .build());

        scenes.put("14", builder()
                .verb(new DialogVerb("18"))
                .verb(new PosterVerb("poster-prize.png"))
                .build());

        scenes.put("15", builder()
                .verb(new DialogVerb("19"))
                .build());

        // standing in fire
        scenes.put("16", builder()
                .verb(new DialogVerb("20"))
                .build());

    }

}
