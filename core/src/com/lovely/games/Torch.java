package com.lovely.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import static com.lovely.games.TheFirstGate.HALF_TILE_SIZE;
import static com.lovely.games.TheFirstGate.QUARTER_TILE_SIZE;

public class Torch implements Switchable {

    Vector2 pos;
    Color color;
    boolean isFire;
    boolean isOn;
    boolean originalIsOn;
    String switchId;
    private ParticleSource partilcSource;

    public Torch(Vector2 pos, Color color, boolean isFire, String switchId, boolean isOn) {
        this.pos = pos;
        this.color = color;
        this.isFire = isFire;
        this.isOn = isOn;
        this.originalIsOn = isOn;
        this.switchId = switchId;
    }

    public void start(TheFirstGate theFirstGate) {
        this.isOn = originalIsOn;
        this.partilcSource = getTorchParticleSource(this);
        theFirstGate.addParticle(partilcSource);
    }

    public void update() {
        this.partilcSource.isActive = isOn;
    }

    public ParticleSource getTorchParticleSource(Torch torch) {
        float lifeTimer = -1f;
        String image = "particles/fire.png";
        Vector2 pos = torch.pos.cpy().add(QUARTER_TILE_SIZE, HALF_TILE_SIZE);
        Vector2 mov = new Vector2(-0.2f, 0.2f);
        Vector2 randPos = new Vector2(HALF_TILE_SIZE, HALF_TILE_SIZE);
        Vector2 randMov = new Vector2(-0.1f, 0.1f);
        Vector2 randLife = new Vector2(0.4f, 0.8f);
        int numParticles = 4;
        Color startColor = Color.valueOf("2b0009");
        Color targetColor = Color.valueOf("ffd600");
        targetColor.a = 0.2f;
        ParticleSource p = new ParticleSource(lifeTimer, image, pos, mov, randMov, randPos, randLife, numParticles, startColor, targetColor);
        p.start();
        return p;
    }

    @Override
    public void handleMessage(String id) {
        if (switchId != null && switchId.equals(id)) {
            isOn = !isOn;
        }
    }
}
