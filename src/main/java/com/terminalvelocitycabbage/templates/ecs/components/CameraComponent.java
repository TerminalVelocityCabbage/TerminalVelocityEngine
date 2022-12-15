package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Matrix4f;

public class CameraComponent implements Component {

    float fov;
    float clippingPlane;
    float farPlane;
    Matrix4f projectionMatrix;

    @Override
    public void setDefaults() {
        this.fov = (float) Math.toRadians(60);
        this.clippingPlane = 0.1f;
        this.farPlane = 1000f;
        if (projectionMatrix == null) this.projectionMatrix = new Matrix4f();
        createProjectionMatrix();
    }

    public CameraComponent configureView(float fov, float clippingPlane, float farPlane) {
        setFov(fov);
        setClippingPlane(clippingPlane);
        setFarPlane(farPlane);
        updateProjectionMatrix();
        return this;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getClippingPlane() {
        return clippingPlane;
    }

    public void setClippingPlane(float clippingPlane) {
        this.clippingPlane = clippingPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void createProjectionMatrix() {
        projectionMatrix.identity().perspective(fov, Renderer.getWindow().aspectRatio(), clippingPlane, farPlane);
    }

    public void updateProjectionMatrix() {
        projectionMatrix.setPerspective(fov, Renderer.getWindow().aspectRatio(), clippingPlane, farPlane);
    }
}
