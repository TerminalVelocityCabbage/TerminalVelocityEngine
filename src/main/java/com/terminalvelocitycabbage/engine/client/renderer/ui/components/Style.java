package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import org.joml.Vector4f;

public class Style {

	Vector4f color;
	Vector4f borderColor;
	int borderRadius;
	int borderThickness;
	Margin margin;

	public Style() {
		this.color = new Vector4f(1);
		this.borderColor = new Vector4f(0);
		this.borderRadius = 0;
		this.borderThickness = 0;
		this.margin = new Margin();
	}

	public Vector4f getColor() {
		return color;
	}

	public Style setColor(float r, float g, float b, float a) {
		this.color.set(r, g, b, a);
		return this;
	}

	public Style setColor(Vector4f color) {
		this.color = color;
		return this;
	}

	public Vector4f getBorderColor() {
		return borderColor;
	}

	public Style setBorderColor(float r, float g, float b, float a) {
		this.borderColor.set(r, g, b, a);
		return this;
	}

	public int getBorderRadius() {
		return borderRadius;
	}

	public Style setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
		return this;
	}

	public int getBorderThickness() {
		return borderThickness;
	}

	public Style setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		return this;
	}

	public Style margin(int value, UIDimension.Unit unit) {
		return setMargins(value, value, value, value).setMarginUnits(unit, unit, unit, unit);
	}

	public Style setMargins(int left, int right, int top, int bottom) {
		this.margin.setMargins(left, right, top, bottom);
		return this;
	}

	public Style setMarginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
		this.margin.setMarginUnits(left, right, top, bottom);
		return this;
	}

	public Style marginLeft(int value, UIDimension.Unit unit) {
		this.margin.left.value = value;
		this.margin.left.unit = unit;
		return this;
	}

	public Style marginRight(int value, UIDimension.Unit unit) {
		this.margin.right.value = value;
		this.margin.right.unit = unit;
		return this;
	}

	public Style marginTop(int value, UIDimension.Unit unit) {
		this.margin.top.value = value;
		this.margin.top.unit = unit;
		return this;
	}

	public Style marginBottom(int value, UIDimension.Unit unit) {
		this.margin.bottom.value = value;
		this.margin.bottom.unit = unit;
		return this;
	}

	public Margin getMargin() {
		return margin;
	}
}
