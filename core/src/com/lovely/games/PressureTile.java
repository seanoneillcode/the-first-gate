package com.lovely.games;

import com.badlogic.gdx.math.Vector2;

class PressureTile {

    Vector2 pos;
    Trunk trunk;
    private boolean handledAction;
    String switchId;
    private boolean isSwitch;

    PressureTile(Vector2 pos, String switchId, boolean isSwitch) {
        this.pos = pos;
        this.handledAction = false;
        this.switchId = switchId;
        this.trunk = null;
        this.isSwitch = isSwitch;
    }

    void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    void start() {
        this.handledAction = false;
    }

    void handlePressureOff() {
        if (handledAction) {
            if (!isSwitch) {
                if (trunk != null) {
                    trunk.broadcast(switchId);
                }
            }
        }
        this.handledAction = false;
    }

    void handleAction() {
        if (!handledAction && trunk != null) {
            trunk.broadcast(switchId);
            handledAction = true;
        }
    }
}
