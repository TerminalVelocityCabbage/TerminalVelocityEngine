package com.terminalvelocitycabbage.engine.client.sound;

import com.terminalvelocitycabbage.engine.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private SoundListener listener;
    private final Map<Identifier, Sound> sounds;
    private final Map<Identifier, SoundSource> soundSources;

    public SoundManager() {
        sounds = new HashMap<>();
        soundSources = new HashMap<>();
    }

    public void addSoundSource(Identifier name, SoundSource soundSource) {
        this.soundSources.put(name, soundSource.setManager(this));
    }

    public SoundSource getSoundSource(Identifier name) {
        return this.soundSources.get(name);
    }

    public void playSoundSource(Identifier name) {
        SoundSource soundSource = this.soundSources.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(Identifier name) {
        this.soundSources.remove(name);
    }

    public void addSound(Identifier name, Sound sound) {
        this.sounds.put(name, sound);
    }

    public Sound getSound(Identifier soundIdentifier) {
        return sounds.get(soundIdentifier);
    }

    public SoundListener getListener() {
        return this.listener;
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void cleanup() {
        soundSources.values().forEach(SoundSource::destroy);
        soundSources.clear();
        sounds.values().forEach(Sound::destroy);
        sounds.clear();
    }

}
