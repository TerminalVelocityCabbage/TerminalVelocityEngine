package com.terminalvelocitycabbage.engine.client.renderer.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

//TODO make this fit new way of doing input handler
public class FirstPersonCamera extends Camera {

    private float moveModifier = 60f;
    private float rotateModifier = 1f;

    //For adding movement queues
    private Vector3f deltaPosition = new Vector3f();
    private Vector2f deltaRotation = new Vector2f();

    private final Vector3f position;
    private final Vector3f tempVec3;
    private final Quaternionf rotation;

    private Matrix4f viewMatrix;

    public FirstPersonCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);

        position = new Vector3f(0, 0, 0);
        tempVec3 = new Vector3f();
        rotation = new Quaternionf();

        viewMatrix = new Matrix4f();
    }

    public void rotate(float deltaTime) {
        deltaRotation.mul(deltaTime);
        rotation.x += deltaRotation.x;
        rotation.y += deltaRotation.y;
    }

    public void move(float deltaTime) {
        deltaPosition.mul(deltaTime);
        position.add(
                ((float)Math.sin(rotation.y) * -deltaPosition.z) + ((float)Math.sin(rotation.y - Math.toRadians(90)) * -deltaPosition.x),
                deltaPosition.y,
                ((float)Math.cos(rotation.y) * deltaPosition.z) + ((float)Math.cos(rotation.y - Math.toRadians(90)) * deltaPosition.x));
    }

    public void update(float deltaTime) {
        deltaPosition.mul(moveModifier);
        deltaRotation.mul(rotateModifier);
        rotate(deltaTime);
        move(deltaTime);
    }

    public void queueRotate(float x, float y) {
        deltaRotation.add(y, x);
    }

    public void queueMove(float x, float y, float z) {
        deltaPosition.add(x, y, z);
    }

    public void resetDeltas() {
        deltaPosition.zero();
        deltaRotation.zero();
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotateX(rotation.x).rotateY(rotation.y).translate(position.negate(tempVec3));
    }
}
