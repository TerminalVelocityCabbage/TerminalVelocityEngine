package com.terminalvelocitycabbage.engine.prefabs.camera.firstperson.firstperson;

import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FirstPersonCamera extends Camera {

    private final float moveModifier = 60f;
    private final float rotateModifier = 1f;

    //For adding movement queues
    private final Vector3f deltaPosition = new Vector3f();
    private final Vector2f deltaRotation = new Vector2f();

    private final Vector3f position;
    private float pitch;
    private float yaw;

    public FirstPersonCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);

        position = new Vector3f(0, 0, 0);
    }

    @Override
    public <T extends InputHandler> void update(T inputHandler, float deltaTime) {

        deltaPosition.zero();
        deltaRotation.zero();

        if (FirstPersonInputHandler.FORWARD.isKeyPressed()) deltaPosition.add(0, 0, -1);
        if (FirstPersonInputHandler.BACKWARDS.isKeyPressed()) deltaPosition.add(0, 0, 1);
        if (FirstPersonInputHandler.LEFT.isKeyPressed()) deltaPosition.add(-1, 0, 0);
        if (FirstPersonInputHandler.RIGHT.isKeyPressed()) deltaPosition.add(1, 0, 0);
        if (FirstPersonInputHandler.UP.isKeyPressed()) deltaPosition.add(0, 1, 0);
        if (FirstPersonInputHandler.DOWN.isKeyPressed()) deltaPosition.add(0, -1, 0);

        if (inputHandler.isRightButtonHolding()) {
            deltaRotation.add(inputHandler.getMouseDeltaX(), inputHandler.getMouseDeltaY());
        }

        rotate(deltaTime);
        move(deltaTime);
    }

    public void rotate(float deltaTime) {
        deltaRotation.mul(deltaTime);
        pitch += deltaRotation.x;
        yaw += deltaRotation.y;
    }

    public void move(float deltaTime) {
        deltaPosition.mul(deltaTime);
        position.add(
                ((float)Math.sin(yaw) * deltaPosition.z) + ((float)Math.sin(yaw - Math.toRadians(90)) * deltaPosition.x) * deltaTime * moveModifier,
                -deltaPosition.y,
                ((float)Math.cos(yaw) * -deltaPosition.z) + ((float)Math.cos(yaw - Math.toRadians(90)) * -deltaPosition.x) * deltaTime * moveModifier);
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotateX(pitch).rotateY(yaw).translate(position);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
