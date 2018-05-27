package com.lovely.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static com.lovely.games.TheFirstGate.PLAYER_ARROW_SPEED;
import static com.lovely.games.TheFirstGate.TILE_SIZE;

public class Actor {

    public static final float SHOOT_TIMER_LIMIT = 0.6f;
    Vector2 pos;
    String id;
    boolean isHidden;
    Vector2 originalPos;
    boolean originalIsHide;
    boolean isWalking;
    boolean isFacingRight;
    boolean isBoss;
    float shootTimer;
    int bossLives;
    private Vector2 oldTeleportPos;
    private Vector2 newTeleportPos;
    private boolean wasHit = false;
    boolean isDone = false;

    public Actor(Vector2 pos, String id, boolean isHide, boolean isRight, boolean isBoss) {
        this.pos = pos;
        this.id = id;
        this.originalIsHide = isHide;
        this.isHidden = isHide;
        this.originalPos = pos.cpy();
        this.isWalking = false;
        this.isFacingRight = isRight;
        this.isBoss = isBoss;
        this.shootTimer = 0;
        this.bossLives = 3;
        this.oldTeleportPos = pos.cpy();
        this.newTeleportPos = pos.cpy();
    }

    public void start() {
        this.pos = originalPos.cpy();
        this.isHidden = originalIsHide;
        this.oldTeleportPos = originalPos.cpy();
        this.newTeleportPos = originalPos.cpy();
        this.shootTimer = 0;
        this.bossLives = 3;
        wasHit = false;
        isDone = false;
    }

    public void update(TheFirstGate stage, Platform platform) {
        if (isHidden || isDone) {
            return;
        }
        shootTimer += Gdx.graphics.getDeltaTime();
        if (shootTimer > SHOOT_TIMER_LIMIT) {
            shootTimer = 0;
            Vector2 arrowPos = pos.cpy().add(0, -24);
            stage.addArrow(arrowPos, new Vector2(0, -1), PLAYER_ARROW_SPEED);
        }
        if (wasHit) {
            wasHit = false;
            bossLives = bossLives - 1;
            getNextPlatformPos(stage, platform);
            pos = newTeleportPos.cpy();
        }
        if (bossLives < 0) {
            stage.playScene("29");
            isDone = true;
        }
    }

    void getNextPlatformPos(TheFirstGate stage, Platform platform) {
        List<Platform> platformList = stage.currentLevel.getPlatforms();
        int index = platformList.indexOf(platform);
        index++;
        if (index >= platformList.size()) {
            index = 0;
        }
        oldTeleportPos = platform.pos.cpy();
        newTeleportPos = platformList.get(index).pos.cpy();
    }

    protected Rectangle getHitRect() {
        float buffer = TILE_SIZE * 0.2f;
        float playerSize = TILE_SIZE - buffer - buffer;
        return new Rectangle(pos.x + buffer, pos.y + buffer, playerSize, playerSize);
    }

    public void handleHit() {
        if (isBoss && !isHidden) {
            wasHit = true;
        }
    }
}
