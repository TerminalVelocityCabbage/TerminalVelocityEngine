package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Vector2f;

//TODO convert to velocity
public class PitchYawRotationTargetComponent implements Component {

    private Vector2f rotateTarget;
    private Vector2f currentRotation;
    private float rotateProgress;

    @Override
    public void setDefaults() {
        rotateTarget = new Vector2f(0f, 0f);
        currentRotation = new Vector2f(0f, 0f);
        rotateProgress = 1f;
    }

    public Vector2f getRotateTarget() {
        return rotateTarget;
    }

    public void setRotateTarget(Vector2f rotateTarget) {
        this.rotateTarget = rotateTarget;
    }

    public void addRotateTarget(Vector2f rotateTarget) {
        this.rotateTarget.add(rotateTarget);
    }

    public void addRotateTarget(float x, float y) {
        this.rotateTarget.add(x, y);
    }

    public Vector2f getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(Vector2f currentRotation) {
        this.currentRotation = currentRotation;
    }

    public float getRotateProgress() {
        return rotateProgress;
    }

    public void setRotateProgress(float rotateProgress) {
        this.rotateProgress = rotateProgress;
    }

    public void addRotationProgress(float rotation) {
        rotateProgress += rotation;
        rotateProgress = Math.min(rotateProgress, 1);
    }
}
