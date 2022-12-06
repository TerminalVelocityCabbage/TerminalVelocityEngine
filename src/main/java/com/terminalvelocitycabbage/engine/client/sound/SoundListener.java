package com.terminalvelocitycabbage.engine.client.sound;

import com.terminalvelocitycabbage.engine.client.renderer.Camera;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundListener {

    public SoundListener() {
        alListener3f(AL_POSITION, 0, 0, 0);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public SoundListener(Vector3f position) {
        setPosition(position);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setDistanceModel(SoundDistanceModels model) {
        alDistanceModel(model.getAlModel());
    }

    public void updateOrientationCamera(Camera camera) {

        //Init vars for passing
        Vector3f at = new Vector3f();
        Vector3f up = new Vector3f();

        //Get data from the camera
        var cameraMatrix = camera.getViewMatrix();
        cameraMatrix.positiveZ(at).negate();
        cameraMatrix.positiveY(up);

        //Pass data to the listener
        setOrientation(at, up);
    }

    public void setVelocity(Vector3f velocity) {
        alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public void setOrientation(Vector3f position, Vector3f up) {
        float[] data = new float[] {
            position.x, position.y, position.z, up.x, up.y, up.z
        };
        alListenerfv(AL_ORIENTATION, data);
    }

    /**
     * @param gain The gain applied.
     *             Range: [0.0f, Infinity]
     *             This is weird in OpenAl. it's not actually Logarithmic, it's on a wrong decibel scale.
     *             Every multiplication of 2 increases +6dB and every division by 2 decreases by -6dB
     *             0.0f means that there will be no sound played.
     */
    public void setGain(float gain) {
        alListenerf(AL_GAIN, gain);
    }
}
