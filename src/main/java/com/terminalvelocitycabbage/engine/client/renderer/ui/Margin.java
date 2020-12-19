package com.terminalvelocitycabbage.engine.client.renderer.ui;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PIXELS;

public class Margin {

	UIDimension left;
	UIDimension right;
	UIDimension top;
	UIDimension bottom;

	public Margin() {
		left = new UIDimension(0, PIXELS);
		right = new UIDimension(0, PIXELS);
		top = new UIDimension(0, PIXELS);
		bottom = new UIDimension(0, PIXELS);
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

	public void setMargins(int value) {
		setMargins(value, value, value, value);
	}

	public void setMargins(int left, int right, int top, int bottom) {
		this.left.value = left;
		this.right.value = right;
		this.top.value = top;
		this.bottom.value = bottom;
	}


}
