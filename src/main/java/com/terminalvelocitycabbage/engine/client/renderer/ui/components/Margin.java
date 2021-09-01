package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import com.terminalvelocitycabbage.engine.client.renderer.ui.AnimatableUIValue;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PIXELS;

public class Margin {

	UIDimension left;
	UIDimension right;
	UIDimension top;
	UIDimension bottom;

	public Margin() {
		left = new UIDimension(new AnimatableUIValue(0), PIXELS);
		right = new UIDimension(new AnimatableUIValue(0), PIXELS);
		top = new UIDimension(new AnimatableUIValue(0), PIXELS);
		bottom = new UIDimension(new AnimatableUIValue(0), PIXELS);
	}

	public void setMarginUnit(UIDimension.Unit unit) {
		setMarginUnits(unit, unit, unit, unit);
	}

	public void setMarginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
		this.left.unit = left;
		this.right.unit = right;
		this.top.unit = top;
		this.bottom.unit = bottom;
	}

	public void setMargins(AnimatableUIValue value) {
		setMargins(value, value, value, value);
	}

	public void setMargins(AnimatableUIValue left, AnimatableUIValue right, AnimatableUIValue top, AnimatableUIValue bottom) {
		this.left.value = left;
		this.right.value = right;
		this.top.value = top;
		this.bottom.value = bottom;
	}

	public UIDimension left() {
		return left;
	}

	public UIDimension right() {
		return right;
	}

	public UIDimension top() {
		return top;
	}

	public UIDimension bottom() {
		return bottom;
	}
}
