package com.terminalvelocitycabbage.templates.camera.free;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.Camera;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Deprecated()
//Don't use this right now, it doesn't work at all, will re-implement when parented to objects with actual physics
public class FreeCamera extends Camera {

    Matrix4f viewMatrix = new Matrix4f();

    public Vector3f linearAcceleration = new Vector3f();
    public Vector3f linearVelocity = new Vector3f();

    public Vector3f angularAcceleration = new Vector3f();
    public Vector3f angularVelocity = new Vector3f();

    public Vector3f position = new Vector3f();
    public Quaternionf rotation = new Quaternionf();

    private Vector3f tempVec = new Vector3f();

    public float accelerationFactor = 6.0f;
    public float rotateZ = 0.0f;

    public FreeCamera(int fov, float clippingPlane, float farPlane) {
        super(fov, clippingPlane, farPlane);
    }

    @Override
    public void update(InputHandler inputHandler, float deltaTime) {

        //Update velocity based on accelerations
        linearVelocity.fma(deltaTime, linearAcceleration);
        angularVelocity.fma(deltaTime, angularAcceleration);

        //Update transformations based on velocity
        rotation.integrate(deltaTime, angularVelocity.x, angularVelocity.y, angularVelocity.z);
        position.fma(deltaTime, linearVelocity);
    }

    public void accelerateForward() {
        linearAcceleration.fma(accelerationFactor, forward(tempVec));
    }

    public void accelerateBackwards() {
        linearAcceleration.fma(-accelerationFactor, forward(tempVec));
    }

    public void accelerateRight() {
        linearAcceleration.fma(accelerationFactor, right(tempVec));
    }

    public void accelerateLeft() {
        linearAcceleration.fma(-accelerationFactor, right(tempVec));
    }

    public void accelerateUp() {
        linearAcceleration.fma(accelerationFactor, up(tempVec));
    }

    public void accelerateDown() {
        linearAcceleration.fma(-accelerationFactor, up(tempVec));
    }

    public void accelerateTwistLeft() {
        rotateZ -= 1.0f;
    }

    public void accelerateTwistRight() {
        rotateZ += 1.0f;
    }

    public void rotate(float mouseX, float mouseY, float rotateZ) {
        angularVelocity.set(mouseY, mouseX, rotateZ);
    }

    public Vector3f right(Vector3f dest) {
        return rotation.positiveX(dest);
    }

    public Vector3f up(Vector3f dest) {
        return rotation.positiveY(dest);
    }

    public Vector3f forward(Vector3f dest) {
        return rotation.positiveZ(dest).negate();
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotate(rotation).translate(-position.x, -position.y, -position.z);
    }
}
