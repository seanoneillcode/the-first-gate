package com.lovely.games;

import java.util.ArrayList;
import java.util.List;

class Trunk {

    private List<Switchable> listeners;

    Trunk() {
        listeners = new ArrayList<>();
    }

    void broadcast(String id) {
        for (Switchable listener : listeners) {
            listener.handleMessage(id);
        }
    }

    void addListener(Switchable listener) {
        this.listeners.add(listener);
    }
}
