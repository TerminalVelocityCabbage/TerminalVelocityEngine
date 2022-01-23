package com.terminalvelocitycabbage.engine.client.renderer.util;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RayCaster {

    Vector3f rayStart;
    Vector3f rayEnd;

    public RayCaster() {
        rayStart = new Vector3f();
        rayEnd = new Vector3f();
    }

    public void cast(Camera camera, float mouseX, float mouseY, float distance) {
        //Collect Variables required to compute the ray
        Matrix4f projectionMatrix = new Matrix4f(camera.getProjectionMatrix());
        Vector3f direction = new Vector3f();

        //Compute the ray
        projectionMatrix.mul(camera.getViewMatrix()).unprojectRay(
                mouseX,
                Renderer.getWindow().height() - mouseY,
                new int[] {0, 0, Renderer.getWindow().width(), Renderer.getWindow().height()},
                rayStart, direction);

        //Normalize the direction, so we can more accurately determine the length the ray should be
        direction.normalize();

        //Get the end of the ray
        camera.getWorldPosition().add(direction.mul(distance), rayEnd);
    }

    public Vector3f getRayStart() {
        return rayStart;
    }

    public Vector3f getRayEnd() {
        return rayEnd;
    }
}
