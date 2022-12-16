package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Vector3f;

//TODO convert to velocity
public class PositionTargetComponent implements Component {

    Vector3f moveTarget;
    float moveProgress;

    @Override
    public void setDefaults() {
        moveTarget = new Vector3f(0f, 0f, 0f);
        moveProgress = 1f;
    }

    public Vector3f getMoveTarget() {
        return moveTarget;
    }

    public void setMoveTarget(Vector3f moveTarget) {
        this.moveTarget = moveTarget;
    }

    public void addMoveTarget(Vector3f moveTarget) {
        this.moveTarget.add(moveTarget);
    }

    public void addMoveTarget(float x, float y, float z) {
        this.moveTarget.add(x, y, z);
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    public void setMoveProgress(float moveProgress) {
        this.moveProgress = moveProgress;
    }

    public void addMoveProgress(float moveProgress) {
        this.moveProgress += moveProgress;
        this.moveProgress = Math.min(this.moveProgress, 1);
    }
}
