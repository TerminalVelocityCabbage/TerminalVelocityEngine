package com.terminalvelocitycabbage.engine.client.renderer.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

//TODO make this fit new way of doing input handler
public class FirstPersonCamera extends Camera {

    private float moveModifier = 0.1f;
    private float rotateModifier = 0.01f;

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

    public void rotate(float x, float y, float z) {
        rotation.rotateXYZ(x, y, z);
    }

    public void move(float offsetX, float offsetY, float offsetZ) {
        position.add(
                ((float)Math.sin(rotation.y) * -offsetZ) + ((float)Math.sin(rotation.y - 90) * -offsetX),
                offsetY,
                ((float)Math.cos(rotation.y) * offsetZ) + ((float)Math.cos(rotation.y - 90) * offsetX));
    }

    public void update(float deltaTime) {
        //Update transformations based on velocity
        rotation.integrate(deltaTime, deltaRotation.x, deltaRotation.y, 0f);
        //TODO use move to convert it to rotated movement
        position.fma(deltaTime, deltaPosition);
    }

    public void queueRotate(float y, float x) {
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
        return viewMatrix.identity().rotate(rotation).translate(position.negate(tempVec3));
    }
}
