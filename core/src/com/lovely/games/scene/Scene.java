package com.lovely.games.scene;

import java.util.ArrayList;
import java.util.List;

import com.lovely.games.Stage;

public class Scene {

    List<SceneVerb> verbs;
    int currentVerb;
    boolean isDone;

    public Scene(List<SceneVerb> verbs) {
        this.verbs = verbs;
        this.currentVerb = 0;
        this.isDone = false;
    }

    public void update(Stage stage) {
        if (!isDone) {
            SceneVerb verb = verbs.get(currentVerb);
            verb.update(stage);
            if (verb.isDone()) {
                System.out.println("current verb is done " + verb.getClass().getName());
                currentVerb = currentVerb + 1;
            }
            if (currentVerb == verbs.size()) {
                currentVerb = 0;
                isDone = true;
                System.out.println("resetting camera, all verbs done ");
                stage.resetCamera();
            }
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        List<SceneVerb> verbs = new ArrayList<>();

        public Builder verb(SceneVerb verb) {
            verbs.add(verb);
            return this;
        }

        public Scene build() {
            return new Scene(verbs);
        }
    }

}
