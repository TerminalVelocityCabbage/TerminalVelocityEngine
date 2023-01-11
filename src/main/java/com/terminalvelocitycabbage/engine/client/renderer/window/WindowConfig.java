package com.terminalvelocitycabbage.engine.client.renderer.window;

public class WindowConfig {

    boolean fullscreen;
    boolean blit;
    int width;
    int height;
    String title;
    boolean vSync;
    boolean center;
    boolean lockAndHideCursor;

    public WindowConfig(boolean fullscreen, boolean blit, int width, int height, String title, boolean vSync, boolean center, boolean lockAndHideCursor) {
        this.fullscreen = fullscreen;
        this.blit = blit;
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.center = center;
        this.lockAndHideCursor = lockAndHideCursor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        boolean fullscreen;
        boolean blit;
        int width;
        int height;
        String title;
        boolean vSync;
        boolean center;
        boolean lockAndHideCursor;

        private Builder() {
            this.fullscreen = false;
            this.blit = false;
            this.width = 0;
            this.height = 0;
            this.title = "window title";
            this.vSync = false;
            this.center = false;
            this.lockAndHideCursor = false;
        }

        public WindowConfig build() {
            return new WindowConfig(fullscreen, blit, width, height, title, vSync, center, lockAndHideCursor);
        }

        public Builder fullscreen() {
            this.fullscreen = true;
            return this;
        }

        public Builder blit() {
            this.blit = true;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder vSync() {
            this.vSync = true;
            return this;
        }

        public Builder center() {
            this.center = true;
            return this;
        }

        public Builder lockAndHideCursor() {
            this.lockAndHideCursor = true;
            return this;
        }
    }
}
