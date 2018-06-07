package com.lovely.games;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleSource {

    List<Particle> particles;
    float lifeTimer;
    String image;
    Vector2 pos;
    Vector2 mov;
    Vector2 randMov;
    Vector2 randPos;
    Vector2 randLife;
    Vector2 colorStep;
    int numParticles;
    private Color startColor;
    private Color targetColor;
    boolean isActive;

    public ParticleSource(float lifeTimer, String image, Vector2 pos, Vector2 mov, Vector2 randMov, Vector2 randPos, Vector2 randLife, int numParticles, Color startColor, Color targetColor) {
        this.lifeTimer = lifeTimer;
        this.image = image;
        this.pos = pos;
        this.mov = mov;
        this.randMov = randMov;
        this.randPos = randPos;
        this.randLife = randLife;
        this.numParticles = numParticles;
        this.startColor = startColor;
        this.targetColor = targetColor;
        particles = new ArrayList<>();
        isActive = true;
    }

    public void start() {
        for (int i = 0; i < numParticles; i++) {
            particles.add(generateNewParticle());
        }
    }

    public void update() {
        lifeTimer = lifeTimer - Gdx.graphics.getDeltaTime();
        if (!isActive) {
            return;
        }
        for (Particle particle : particles) {
            particle.update();
            if (!particle.isAlive()) {
                generateParticleData(particle);
            }
        }
    }

    private Particle generateNewParticle() {
        Particle particle = new Particle();
        generateParticleData(particle);
        particle.lifeTimer = MathUtils.random(0, particle.lifeTimer);
        return particle;
    }

    private void generateParticleData(Particle particle) {
        Vector2 position = this.pos.cpy();
        position.x = position.x + MathUtils.random(randPos.x);
        position.y = position.y + MathUtils.random(randPos.y);
        Vector2 move = this.mov.cpy();
        move.x = move.x + MathUtils.random(randMov.x);
        move.y = move.y + MathUtils.random(randMov.y);
        float life = MathUtils.random(randLife.x, randLife.y);

        particle.pos = position.cpy();
        particle.mov = move.cpy();
        particle.lifeTimer = 0;
        particle.image = image;
        particle.totalLife = life;
        particle.color = this.startColor.cpy();
        particle.targetColor = this.targetColor.cpy();
        particle.colorStep = 1f / life / life / 10f;
    }

    public boolean isDone() {
        return lifeTimer < 0;
    }

}
