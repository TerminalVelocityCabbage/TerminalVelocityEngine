package com.terminalvelocitycabbage.templates.camera.firstperson.firstperson;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.Camera;
import com.terminalvelocitycabbage.engine.utils.EasingUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Direction.OUT;
import static com.terminalvelocitycabbage.engine.utils.EasingUtil.Function.CIRCULAR;

public class FirstPersonCamera extends Camera {

    private final float moveModifier = 100f;
    private final float rotateModifier = .05f;

    private Vector2f rotateTarget = new Vector2f(0f, 0f);
    private Vector2f currentRotation = new Vector2f(0f, 0f);
    private float rotateProgress = 1f;

    private Vector3f moveTarget = new Vector3f(0f, 0f, 0f);
    private Vector3f currentPosition = new Vector3f(0f, 0f, 0f);
    private float moveProgress = 1f;

    public FirstPersonCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);
    }

    @Override
    public <T extends InputHandler> void update(T inputHandler, float deltaTime) {

        //Avoid division by 0
        if (deltaTime == 0) deltaTime += 0.01f;

        //Update Target Rotations
        if (inputHandler.isRightButtonHolding()) {
            modifyRotateTarget(inputHandler.getMouseDeltaY(), inputHandler.getMouseDeltaX(), deltaTime);
        }

        //Update the rotations
        updateRotations(deltaTime);

        //Update Target Movement
        modifyMoveTarget(deltaTime);

        //Update current rotations
        updateMovements(deltaTime);
    }

    public void modifyRotateTarget(float deltaVelocityX, float deltaVelocityY, float deltaTime) {
        rotateTarget.add(rotateModifier * deltaVelocityX / deltaTime, rotateModifier * deltaVelocityY / deltaTime);
        rotateProgress = 0;
    }

    public void updateRotations(float deltaTime) {
        rotateProgress += 5 / deltaTime;
        rotateProgress = Math.min(rotateProgress, 1);
        currentRotation.set(
                EasingUtil.lerp(currentRotation.x, rotateTarget.x, rotateProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentRotation.y, rotateTarget.y, rotateProgress, OUT, CIRCULAR)
        );
    }

    public void modifyMoveTarget(float deltaTime) {

        moveProgress = 0f;

        int xInput = 0;
        int yInput = 0;
        int zInput = 0;

        if(FirstPersonInputHandler.LEFT.isKeyPressed()) xInput--;
        if(FirstPersonInputHandler.RIGHT.isKeyPressed()) xInput++;
        if(FirstPersonInputHandler.UP.isKeyPressed()) yInput++;
        if(FirstPersonInputHandler.DOWN.isKeyPressed()) yInput--;
        if(FirstPersonInputHandler.FORWARD.isKeyPressed()) zInput--;
        if(FirstPersonInputHandler.BACKWARDS.isKeyPressed()) zInput++;

        float xMovement = moveModifier * xInput / deltaTime;
        float yMovement = moveModifier * yInput / deltaTime;
        float zMovement = moveModifier * zInput / deltaTime;

        moveTarget.add(
                ((float)Math.sin(currentRotation.y) * zMovement) + (float)Math.sin(currentRotation.y - Math.toRadians(90)) * xMovement,
                -yMovement,
                ((float)Math.cos(currentRotation.y) * -zMovement) + (float)Math.cos(currentRotation.y - Math.toRadians(90)) * -xMovement
        );
    }

    public void updateMovements(float deltaTime) {
        moveProgress += (moveModifier / deltaTime) / 2000f;
        moveProgress = Math.min(moveProgress, 1);
        currentPosition.set(
                EasingUtil.lerp(currentPosition.x, moveTarget.x, moveProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentPosition.y, moveTarget.y, moveProgress, OUT, CIRCULAR),
                EasingUtil.lerp(currentPosition.z, moveTarget.z, moveProgress, OUT, CIRCULAR)
        );
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotateX(currentRotation.x).rotateY(currentRotation.y).translate(currentPosition);
    }

    public Vector3f getPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(float x, float y, float z) {
        this.moveTarget.set(x, y, z);
        this.currentPosition.set(x, y, z);
        moveProgress = 1f;
    }
}
