package com.terminalvelocitycabbage.engine.client.renderer.components;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FirstPersonCamera extends Camera {

    private float moveModifier = 60f;
    private float rotateModifier = 1f;

    //For adding movement queues
    private Vector3f deltaPosition = new Vector3f();
    private Vector2f deltaRotation = new Vector2f();

    private final Vector3f position;
    private float pitch;
    private float yaw;

    private Matrix4f viewMatrix;

    public FirstPersonCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);

        position = new Vector3f(0, 0, 0);

        viewMatrix = new Matrix4f();
    }

    public void rotate(float deltaTime) {
        deltaRotation.mul(deltaTime);
        pitch += deltaRotation.x;
        yaw += deltaRotation.y;
    }

    public void move(float deltaTime) {
        position.add(
                ((float)Math.sin(yaw) * deltaPosition.z) + ((float)Math.sin(yaw - Math.toRadians(90)) * deltaPosition.x) * deltaTime * moveModifier,
                -deltaPosition.y,
                ((float)Math.cos(yaw) * -deltaPosition.z) + ((float)Math.cos(yaw - Math.toRadians(90)) * -deltaPosition.x) * deltaTime * moveModifier);
    }

    public void update(float deltaTime) {
        deltaRotation.mul(rotateModifier);
        rotate(deltaTime);
        move(deltaTime);
    }

    public FirstPersonCamera queueRotate(float x, float y) {
        deltaRotation.add(y, x);
        return this;
    }

    public FirstPersonCamera queueMove(float x, float y, float z) {
        deltaPosition.add(x, y, z);
        return this;
    }

    public void resetDeltas() {
        deltaPosition.zero();
        deltaRotation.zero();
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
