package com.terminalvelocitycabbage.engine.client.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundManager {

    private SoundListener listener;
    private final List<Sound> sounds;
    private final Map<String, SoundSource> soundSources;

    public SoundManager() {
        sounds = new ArrayList<>();
        soundSources = new HashMap<>();
    }

    public void addSoundSource(String name, SoundSource soundSource) {
        this.soundSources.put(name, soundSource);
    }

    public SoundSource getSoundSource(String name) {
        return this.soundSources.get(name);
    }

    public void playSoundSource(String name) {
        SoundSource soundSource = this.soundSources.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSources.remove(name);
    }

    public void addSound(Sound sound) {
        this.sounds.add(sound);
    }

    public SoundListener getListener() {
        return this.listener;
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void cleanup() {
        for (SoundSource soundSource : soundSources.values()) {
            soundSource.destroy();
        }
        soundSources.clear();
        for (Sound soundBuffer : sounds) {
            soundBuffer.destroy();
        }
        sounds.clear();
    }

}
