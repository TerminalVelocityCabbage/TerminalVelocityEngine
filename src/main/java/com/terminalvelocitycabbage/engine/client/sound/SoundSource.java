package com.terminalvelocitycabbage.engine.client.sound;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;

public class SoundSource {

    private final int sourceID;

    public SoundSource() {
        this.sourceID = alGenSources();
        alSourcei(sourceID, AL_LOOPING, AL_FALSE);
        alSourcei(sourceID, AL_SOURCE_RELATIVE, AL_TRUE);
    }

    public void setSound(Sound sound) {
        stop();
        alSourcei(sourceID, AL_BUFFER, sound.getSoundID());
    }

    public void play() {
        alSourcePlay(sourceID);
    }

    public void pause() {
        alSourcePause(sourceID);
    }

    public void stop() {
        alSourceStop(sourceID);
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void destroy() {
        stop();
        alDeleteSources(sourceID);
    }

    public void setLooping(boolean loop) {
        alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public void setRelative(boolean relative) {
        alSourcei(sourceID, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
    }

    /**
     * @param degreeAngle The inner cone angle in degrees
     *                    Range: [0.0f, 360.0f]
     */
    public void setConeInnerAngle(float degreeAngle) {
        if (degreeAngle < 0.0f || degreeAngle > 360.0f) {
            Log.warn("Tried to set a cone inner angle value " + degreeAngle + " out of range 0.0f - 360.0f. We applied a modulo your value.");
            degreeAngle = degreeAngle % 360.0f;
        }
        alSourcef(sourceID, AL_CONE_INNER_ANGLE, degreeAngle);
    }

    /**
     * @param degreeAngle The outer cone angle in degrees
     *                    Range: [0.0f, 360.0f]
     */
    public void setConeOuterAngle(float degreeAngle) {
        if (degreeAngle < 0.0f || degreeAngle > 360.0f) {
            Log.warn("Tried to set a cone outer angle value " + degreeAngle + " out of range 0.0f - 360.0f. We applied a modulo your value.");
            degreeAngle = degreeAngle % 360.0f;
        }
        alSourcef(sourceID, AL_CONE_OUTER_ANGLE, degreeAngle);
    }

    /**
     * @param gain Set the gain of the outer cone relative to the inner cone (controlled by #setGain)
     *             Range: (Logarithmic) [0.0f, 1.0f]
     */
    public void setOuterConeGain(float gain) {
        if (gain < 0.0f || gain > 1.0f) {
            Log.warn("Tried to set an outer cone gain value " + gain + " out of range 0.0f - 1.0f We clamped your value.");
            gain = Math.max(0.0f, Math.min(1.0f, gain));
        }
        alSourcef(sourceID, AL_CONE_OUTER_GAIN, gain);
    }

    /**
     * @param gain The gain applied.
     *             Range: [0.0f, Infinity]
     *             This is weird in OpenAl. it's not actually Logarithmic, it's on a wrong decibel scale.
     *             Every multiplication of 2 increases +6dB and every division by 2 decreases by -6dB
     *             0.0f means that there will be no sound played.
     */
    public void setGain(float gain) {
        if (gain < 0.0f) {
            Log.warn("Tried to set a gain value " + gain + " out of range >= 0.0f We clamped your value.");
            gain = 0.0f;
        }
        alSourcef(sourceID, AL_GAIN, gain);
    }

    /**
     * @param distance The distance above which sources are no longer attenuated.
     *                 Range: [0.0f, Infinity]
     */
    public void setMaxDistance(float distance) {
        if (distance < 0.0f) {
            Log.warn("Tried to set a distance value " + distance + " out of range >= 0.0f We clamped your value.");
            distance = 0.0f;
        }
        alSourcef(sourceID, AL_MAX_DISTANCE, distance);
    }

    /**
     * @param gain Set the maximum source attenuation
     *             Range: (Logarithmic) [0.0f, 1.0f]
     */
    public void setMaxGain(float gain) {
        if (gain < 0.0f || gain > 1.0f) {
            Log.warn("Tried to set a max gain value " + gain + " out of range 0.0f - 1.0f We clamped your value.");
            gain = Math.max(0.0f, Math.min(1.0f, gain));
        }
        alSourcef(sourceID, AL_MAX_GAIN, gain);
    }

    /**
     * @param gain Set the minimum source attenuation
     *             Range: (Logarithmic) [0.0f, 1.0f]
     */
    public void setMinGain(float gain) {
        if (gain < 0.0f || gain > 1.0f) {
            Log.warn("Tried to set a min gain value " + gain + " out of range 0.0f - 1.0f We clamped your value.");
            gain = Math.max(0.0f, Math.min(1.0f, gain));
        }
        alSourcef(sourceID, AL_MIN_GAIN, gain);
    }

    /**
     * @param pitch Pitch modification to be applied.
     *              Range: [0.0f, 2.0f]
     */
    public void setPitch(float pitch) {
        if (pitch < 0.5f || pitch > 2.0f) {
            Log.warn("Tried to set a pitch value " + pitch + " out of range 0.5f - 2.0f We clamped your value.");
            pitch = Math.max(0.5f, Math.min(2.0f, pitch));
        }
        alSourcef(sourceID, AL_PITCH, pitch);
    }

    /**
     * @param distance Source-specific distance to reference for attenuation.
     *                 At 0.0f, no attenuation occurs.
     *                 Range: [0.0f, Infinity]
     */
    public void setReferenceDistance(float distance) {
        if (distance < 0.0f) {
            Log.warn("Tried to set a reference distance value " + distance + " out of range >= 0.0f We clamped your value.");
            distance = 0.0f;
        }
        alSourcef(sourceID, AL_REFERENCE_DISTANCE, distance);
    }

    /**
     * @param rolloffFactor source-specific rolloff factor.
     *                      Range: [0.0f, Infinity]
     */
    public void setRolloffFactor(float rolloffFactor) {
        if (rolloffFactor < 0.0f) {
            Log.warn("Tried to set a rolloff factor value " + rolloffFactor + " out of range >= 0.0f. We clamped your value.");
            rolloffFactor = 0.0f;
        }
        alSourcef(sourceID, AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    /**
     * @param secOffset the number in seconds to set the current playback time to
     */
    public void setSecOffset(float secOffset) {
        alSourcef(sourceID, AL_SEC_OFFSET, secOffset);
    }

    /**
     * @param direction The direction that the cones (if not 360 degrees) are facing.
     */
    public void setDirection(Vector3f direction) {
        alSource3f(sourceID, AL_DIRECTION, direction.x, direction.y, direction.z);
    }

    /**
     * @param position The position of this sound source.
     */
    public void setPosition(Vector3f position) {
        alSource3f(sourceID, AL_POSITION, position.x, position.y, position.z);
    }

    /**
     * @param velocity a vector defining the velocity of this sound source.
     *                 This is used to simulate the doppler effect of sounds.
     */
    public void setVelocity(Vector3f velocity) {
        alSource3f(sourceID, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

}
