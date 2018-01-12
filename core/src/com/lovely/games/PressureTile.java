package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

class PressureTile {

    Vector2 pos;
    Trunk trunk;
    private boolean handledAction;
    String switchId;

    PressureTile(Vector2 pos, String switchId) {
        this.pos = pos;
        this.handledAction = false;
        this.switchId = switchId;
        this.trunk = null;
    }

    void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    void reset() {
        this.handledAction = false;
    }

    void handleAction() {
        if (!handledAction && trunk != null) {
            trunk.broadcast(switchId);
            handledAction = true;
        }
    }
}
