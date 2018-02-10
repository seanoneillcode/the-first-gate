package com.lovely.games.scene;

import java.util.ArrayList;
import java.util.List;

import com.lovely.games.Stage;

public class Scene {

    List<SceneVerb> verbs;
    int currentVerb;
    boolean isDone;
    boolean resetCamera;
    String outcome;

    public Scene(List<SceneVerb> verbs, boolean resetCamera) {
        this.verbs = verbs;
        this.currentVerb = 0;
        this.isDone = false;
        this.resetCamera = resetCamera;
        this.outcome = null;
    }

    public void start() {
        this.isDone = false;
        this.currentVerb = 0;
        for (SceneVerb sceneVerb : verbs) {
            sceneVerb.start();
        }
        this.outcome = null;
    }

    public void skip() {
        verbs.get(currentVerb).skip();
    }

    public void update(Stage stage) {
        if (!isDone) {
            SceneVerb verb = verbs.get(currentVerb);
            verb.update(stage);
            if (verb.isDone()) {
                System.out.println("current verb is done " + verb.getClass().getName());
                System.out.println("verb outcome is " + verb.getOutcome());
                if (verb.getOutcome() != null) {
                    this.outcome = verb.getOutcome();
                }
                currentVerb = currentVerb + 1;
            }
            if (currentVerb == verbs.size()) {
                currentVerb = 0;
                isDone = true;
                System.out.println("resetting camera, all verbs done ");
                if (resetCamera) {
                    stage.resetCamera();
                }
            }
        }
    }

    public boolean isBlocking() {
        return verbs.get(currentVerb).isBlocking();
    }

    public boolean isDone() {
        return isDone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOutcome() {
        return outcome;
    }

    public static class Builder {

        List<SceneVerb> verbs = new ArrayList<>();
        boolean resetCamera = true;

        public Builder verb(SceneVerb verb) {
            verbs.add(verb);
            return this;
        }

        public Builder resetCamera(boolean resetCamera) {
            this.resetCamera = resetCamera;
            return this;
        }

        public Scene build() {
            return new Scene(verbs, resetCamera);
        }
    }

}
