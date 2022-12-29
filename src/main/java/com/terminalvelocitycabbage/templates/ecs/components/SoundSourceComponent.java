package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.sound.SoundSource;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class SoundSourceComponent implements Component {

    boolean locked; //Mainly used when this component is added back to the pool to mark this as needing the sound replaced
    SoundSource soundSource;

    @Override
    public void setDefaults() {
        //We will want to re-use the sound source if this component is made free
        if (soundSource == null) soundSource = new SoundSource();
        locked = true; //This source does not have a new sound set
    }

    /**
     * Use this method to set the sound sources' sound so that it is compatible with pooling. Do NOT use
     * getSoundSource().setSound() in this case.
     * @param sound The sound that this sound source is to play
     */
    public void setSound(Identifier sound) {
        soundSource.setSound(sound);
        locked = false;
    }

    public SoundSource getSoundSource() {
        return soundSource;
    }

    @Override
    public void cleanup() {
        soundSource.destroy();
    }
}
