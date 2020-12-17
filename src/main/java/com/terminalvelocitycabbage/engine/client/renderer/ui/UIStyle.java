package com.terminalvelocitycabbage.engine.client.renderer.ui;

import org.joml.Vector4f;

public class UIStyle {

	Vector4f backgroundColor;
	Vector4f borderColor;
	int borderRadius;
	float borderThickness;

	private UIStyle(Vector4f backgroundColor, Vector4f borderColor, int borderRadius, float borderThickness) {
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderRadius = borderRadius;
		this.borderThickness = borderThickness;
	}

	public Vector4f getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(float r, float g, float b, float a) {
		this.backgroundColor.set(r, g, b, a);
	}

	public Vector4f getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(float r, float g, float b, float a) {
		this.borderColor.set(r, g, b, a);
	}

	public int getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
	}

	public float getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(float borderThickness) {
		this.borderThickness = borderThickness;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		Vector4f backgroundColor;
		Vector4f borderColor;
		int borderRadius;
		float borderThickness;

		public Builder() {
			this.backgroundColor = new Vector4f(1);
			this.borderColor = new Vector4f(0);
			this.borderRadius = 0;
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

		public Builder borderRadius(int radius) {
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
