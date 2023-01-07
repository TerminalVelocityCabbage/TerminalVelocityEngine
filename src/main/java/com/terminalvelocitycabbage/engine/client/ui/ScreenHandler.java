package com.terminalvelocitycabbage.engine.client.ui;

import java.util.ArrayList;
import java.util.List;

public class ScreenHandler {

    private List<Screen> screens;

    public ScreenHandler() {
        screens = new ArrayList<>();
    }

    public <T extends Screen> void addScreen(T screen) {
        screens.add(screen);
    }

    public void init() {
        for (Screen screen : screens) {
            screen.init();
        }
    }

    public void update() {
        for (Screen screen : screens) {
            screen.update();
        }
    }

    public void draw(long vg) {
        for (Screen screen : screens) {
            screen.draw(vg);
        }
    }

    public void cleanup() {
        for (Screen screen : screens) {
            screen.cleanup();
        }
    }
}
