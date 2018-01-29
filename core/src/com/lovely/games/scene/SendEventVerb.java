package com.lovely.games.scene;

import com.lovely.games.Stage;
import com.lovely.games.Trunk;

public class SendEventVerb implements SceneVerb {

    Trunk trunk;
    String switchId;
    boolean isDone;

    public SendEventVerb(Trunk trunk, String switchId) {
        this.trunk = trunk;
        this.switchId = switchId;
        this.isDone = false;
    }

    @Override
    public void update(Stage stage) {
        trunk.broadcast(switchId);
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}
