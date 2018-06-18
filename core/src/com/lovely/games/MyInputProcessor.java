package com.lovely.games;

import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {

    public int lastKeyCode = 0;
    public boolean hasInput = false;

    public void acceptInput() {
        hasInput = false;
        lastKeyCode = 0;
    }

    public boolean keyDown (int keycode) {
        hasInput = true;
        lastKeyCode = keycode;
        return false;
    }

    public boolean keyUp (int keycode) {
        lastKeyCode = keycode;
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
}