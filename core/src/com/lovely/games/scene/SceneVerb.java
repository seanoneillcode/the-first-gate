package com.lovely.games.scene;

import com.lovely.games.Stage;

public interface SceneVerb {

    void update(Stage stage);
    boolean isDone();
}
