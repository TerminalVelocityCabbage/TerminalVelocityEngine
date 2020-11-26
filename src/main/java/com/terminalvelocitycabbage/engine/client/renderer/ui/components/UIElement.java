package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import org.joml.Vector3f;

public abstract class UIElementBase {

	float marginLeft;
	float marginRight;
	float marginTop;
	float marginBottom;

	Vector3f color;
	float backgroundOpacity;

	float borderRadius;
	Vector3f borderColor;
	float borderWidth;

	public UIElementBase() {
		marginLeft = 0f;
		marginRight = 0f;
		marginTop = 0f;
		marginBottom = 0f;
		color = new Vector3f(0);
		backgroundOpacity = 0f;
		borderRadius = 0;
		borderColor = new Vector3f(0);
		borderWidth = 0;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getBackgroundOpacity() {
		return backgroundOpacity;
	}

	public void setBackgroundOpacity(float backgroundOpacity) {
		this.backgroundOpacity = backgroundOpacity;
	}

	public float getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(float borderRadius) {
		this.borderRadius = borderRadius;
	}

	public Vector3f getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Vector3f borderColor) {
		this.borderColor = borderColor;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}
}
