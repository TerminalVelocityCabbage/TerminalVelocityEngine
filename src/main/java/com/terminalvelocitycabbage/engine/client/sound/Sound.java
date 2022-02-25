package com.terminalvelocitycabbage.engine.client.sound;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Sound {

    private final int soundID;

    private ShortBuffer pcm;
    private final boolean stereo;
    private final int sampleRate;

    public Sound(ResourceManager resourceManager, Identifier identifier) {

        //Allocate a pointer for this sound
        this.soundID = alGenBuffers();

        //Load its resource into something we can read
        ByteBuffer vorbis = null;
        if (resourceManager.getResource(identifier).isPresent()) {
            vorbis = resourceManager.getResource(identifier).get().asByteBuffer().orElseThrow();
        } else {
            Log.crash("Could not read sound " + identifier.toString(), new RuntimeException("Sound Read Error, sound not found: " + identifier));
        }

        //Read the ogg format into PCM, so we can use it with OPENAL
        try (STBVorbisInfo info = STBVorbisInfo.malloc()){
            pcm = readVorbis(vorbis, info);
            stereo = info.channels() != 1;
            sampleRate = info.sample_rate();
        }
    }

    public Sound init() {
        alBufferData(soundID, stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16, pcm, sampleRate);
        return this;
    }

    private ShortBuffer readVorbis(ByteBuffer vorbis, STBVorbisInfo info) {

        try (MemoryStack stack = MemoryStack.stackPush()) {

            //Try to decode the vorbis ByteBuffer
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            //Get some meta about the vorbis ByteBuffer
            stb_vorbis_get_info(decoder, info);
            int channels = info.channels();
            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            //Convert Everything and cleanup
            pcm = MemoryUtil.memAllocShort(lengthSamples);
            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }

    public int getSoundID() {
        return soundID;
    }

    public void destroy() {
        alDeleteBuffers(this.soundID);
        if (pcm != null) {
            MemoryUtil.memFree(pcm);
        }
    }
}
