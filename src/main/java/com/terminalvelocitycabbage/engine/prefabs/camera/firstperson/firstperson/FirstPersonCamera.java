package com.terminalvelocitycabbage.engine.prefabs.camera.firstperson.firstperson;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.utils.EasingUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.terminalvelocitycabbage.engine.prefabs.camera.firstperson.firstperson.FirstPersonInputHandler.*;
import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Direction.OUT;
import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Function.CIRCULAR;

public class FirstPersonCamera extends Camera {

    private Vector2f rotateTarget = new Vector2f(0f, 0f);
    private Vector2f currentRotation = new Vector2f(0f, 0f);
    private float rotateProgress = 1f;
    private static final float rotateSpeed = .06f;

    private Vector3f moveTarget = new Vector3f(0f, 0f, 0f);
    private Vector3f currentPosition = new Vector3f(0f, 0f, 0f);
    private float moveProgress = 1f;
    private static final float movementSpeed = .6f;

    public FirstPersonCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);
    }

    @Override
    public <T extends InputHandler> void update(T inputHandler, float deltaTime) {

        //Avoid division by 0
        if (deltaTime == 0) deltaTime = 0.01f;

        //Update Target Rotations
        if (inputHandler.isRightButtonHolding()) {
            modifyRotateTarget(inputHandler.getMouseDeltaY(), inputHandler.getMouseDeltaX(), deltaTime);
        }

        //Update the rotations
        updateRotations(deltaTime);

        int xInput = LEFT.isKeyPressed() ? RIGHT.isKeyPressed() ? 0 : 1 : -1;
        int yInput = UP.isKeyPressed() ? DOWN.isKeyPressed() ? 0 : 1 : -1;
        int zInput = FORWARD.isKeyPressed() ? BACKWARDS.isKeyPressed() ? 0 : 1 : -1;

        float xMovement = movementSpeed * xInput / deltaTime;
        float yMovement = movementSpeed * yInput / deltaTime;
        float zMovement = movementSpeed * zInput / deltaTime;

        moveTarget.add(
                ((float)Math.sin(currentRotation.y) * zMovement) + (float)Math.sin(currentRotation.y - Math.toRadians(90)) * xMovement,
                -yMovement,
                ((float)Math.cos(currentRotation.y) * -zMovement) + (float)Math.cos(currentRotation.y - Math.toRadians(90)) * -xMovement
        );

        updateMovements(deltaTime);
    }

    public void modifyRotateTarget(float deltaVelocityX, float deltaVelocityY, float deltaTime) {
        if (deltaVelocityX != 0 || deltaVelocityY != 0) {
            rotateTarget.add(rotateSpeed * deltaVelocityX / deltaTime, rotateSpeed * deltaVelocityY / deltaTime);
            rotateProgress = 0;
        }
    }

    public void updateRotations(float deltaTime) {
        rotateProgress += 0.6 / deltaTime;
        rotateProgress = Math.min(rotateProgress, 1);
        if (deltaTime > 0 && rotateTarget.x != currentRotation.x && rotateTarget.y != currentRotation.y) {
            currentRotation.set(
                    EasingUtil.lerp(currentRotation.x, rotateTarget.x, rotateProgress, OUT, CIRCULAR),
                    EasingUtil.lerp(currentRotation.y, rotateTarget.y, rotateProgress, OUT, CIRCULAR)
            );
        }
    }

    public void updateMovements(float deltaTime) {
        moveProgress += movementSpeed / deltaTime;
        moveProgress = Math.min(moveProgress, 1);
        if (deltaTime > 0 && (moveTarget.x != currentPosition.x || moveTarget.y != currentPosition.y || moveTarget.z != currentPosition.z)) {
            currentPosition.set(
                    EasingUtil.lerp(currentPosition.x, moveTarget.x, moveProgress, OUT, CIRCULAR),
                    EasingUtil.lerp(currentPosition.y, moveTarget.y, moveProgress, OUT, CIRCULAR),
                    EasingUtil.lerp(currentPosition.z, moveTarget.z, moveProgress, OUT, CIRCULAR)
            );
        }
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotateX(currentRotation.x).rotateY(currentRotation.y).translate(currentPosition);
    }

    public Vector3f getPosition() {
        return currentPosition;
    }

    public float getPitch() {
        return currentRotation.x;
    }

    public float getYaw() {
        return currentRotation.y;
    }
}
