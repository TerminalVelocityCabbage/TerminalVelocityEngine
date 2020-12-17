package com.terminalvelocitycabbage.engine.client.renderer.ui;

import org.joml.Vector4f;

public class UIStyle {

	Vector4f backgroundColor;
	Vector4f borderColor;
	int borderRadius;
	int borderThickness;
	Margin margin;

	public UIStyle() {
		this.backgroundColor = new Vector4f(1);
		this.borderColor = new Vector4f(0);
		this.borderRadius = 0;
		this.borderThickness = 0;
		this.margin = new Margin();
	}

	public Vector4f getBackgroundColor() {
		return backgroundColor;
	}

	public UIStyle setBackgroundColor(float r, float g, float b, float a) {
		this.backgroundColor.set(r, g, b, a);
		return this;
	}

	public Vector4f getBorderColor() {
		return borderColor;
	}

	public UIStyle setBorderColor(float r, float g, float b, float a) {
		this.borderColor.set(r, g, b, a);
		return this;
	}

	public int getBorderRadius() {
		return borderRadius;
	}

	public UIStyle setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
		return this;
	}

	public int getBorderThickness() {
		return borderThickness;
	}

	public UIStyle setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		return this;
	}

	public UIStyle setMargins(int left, int right, int top, int bottom) {
		this.margin.left.value = left;
		this.margin.right.value = right;
		this.margin.top.value = top;
		this.margin.bottom.value = bottom;
		return this;
	}

	public UIStyle setMarginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
		this.margin.left.unit = left;
		this.margin.right.unit = right;
		this.margin.top.unit = top;
		this.margin.bottom.unit = bottom;
		return this;
	}

	public UIStyle marginLeft(int value, UIDimension.Unit unit) {
		this.margin.left.value = value;
		this.margin.left.unit = unit;
		return this;
	}

	public UIStyle marginRight(int value, UIDimension.Unit unit) {
		this.margin.right.value = value;
		this.margin.right.unit = unit;
		return this;
	}

	public UIStyle marginTop(int value, UIDimension.Unit unit) {
		this.margin.top.value = value;
		this.margin.top.unit = unit;
		return this;
	}

	public UIStyle marginBottom(int value, UIDimension.Unit unit) {
		this.margin.bottom.value = value;
		this.margin.bottom.unit = unit;
		return this;
	}
}
