package com.terminalvelocitycabbage.engine.client.renderer.ui;

import org.joml.Vector4f;

public class UIStyle {

	Vector4f backgroundColor;
	Vector4f borderColor;
	float borderRadius;
	float borderThickness;

	private UIStyle(Vector4f backgroundColor, Vector4f borderColor, float borderRadius, float borderThickness) {
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderRadius = borderRadius;
		this.borderThickness = borderThickness;
	}

	public Vector4f getBackgroundColor() {
		return backgroundColor;
	}

	public Vector4f getBorderColor() {
		return borderColor;
	}

	public float getBorderRadius() {
		return borderRadius;
	}

	public float getBorderThickness() {
		return borderThickness;
	}

	public static class Builder {

		Vector4f backgroundColor;
		Vector4f borderColor;
		float borderRadius;
		float borderThickness;

		public Builder() {
			this.backgroundColor = new Vector4f(1);
			this.borderColor = new Vector4f(0);
			this.borderRadius = 0f;
			this.borderThickness = 0f;
		}

		public Builder backgroundColor(float r, float g, float b, float opacity) {
			this.backgroundColor.set(r, g, b, opacity);
			return this;
		}

		public Builder borderColor(float r, float g, float b, float opacity) {
			this.borderColor.set(r, g, b, opacity);
			return this;
		}

		public Builder borderRadius(float radius) {
			this.borderRadius = radius;
			return this;
		}

		public Builder borderThickness(float borderThickness) {
			this.borderThickness = borderThickness;
			return this;
		}

		public UIStyle build() {
			return new UIStyle(backgroundColor, borderColor, borderRadius, borderThickness);
		}
	}
}
