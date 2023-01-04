package com.terminalvelocitycabbage.engine.client.renderer.util;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RayCaster {

    Vector3f rayStart;
    Vector3f rayEnd;
    Vector3f rayDirection;

    public RayCaster() {
        rayStart = new Vector3f();
        rayEnd = new Vector3f();
        rayDirection = new Vector3f();
    }

    public void cast(Matrix4f projectionMatrix, Matrix4f viewMatrix, Vector3f worldPosition, float mouseX, float mouseY, float distance) {
        //Collect Variables required to compute the ray
        Matrix4f projectionMatrix1 = new Matrix4f(projectionMatrix);

        //Compute the ray
        projectionMatrix1.mul(viewMatrix).unprojectRay(
                mouseX,
                ClientBase.getWindow().height() - mouseY,
                new int[] {0, 0, ClientBase.getWindow().width(), ClientBase.getWindow().height()},
                rayStart, rayDirection);

        //Normalize the direction, so we can more accurately determine the length the ray should be
        rayDirection.normalize();

        //Get the end of the ray
        worldPosition.add(rayDirection.mul(distance, rayEnd), rayEnd);
    }

    public Vector3f getRayStart() {
        return rayStart;
    }

    public Vector3f getRayEnd() {
        return rayEnd;
    }

    public Vector3f getRayDirection() {
        return rayDirection;
    }
}
