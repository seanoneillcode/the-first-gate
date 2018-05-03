package com.lovely.games.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.lovely.games.TheFirstGate;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1200, 960);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new TheFirstGate();
        }
}