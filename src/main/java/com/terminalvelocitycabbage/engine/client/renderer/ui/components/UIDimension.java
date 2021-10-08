package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import com.terminalvelocitycabbage.engine.client.renderer.ui.AnimatableUIValue;
import com.terminalvelocitycabbage.engine.debug.Log;

public class UIDimension {

	AnimatableUIValue value;
	Unit unit;

	public UIDimension(float value, Unit unit) {
		this.value = new AnimatableUIValue(value);
		this.unit = unit;
	}

	public int getPixelValue(int windowDimension) {
		if (unit.equals(Unit.PIXELS)) {
			return (int)value.getValue();
		} else if (unit.equals(Unit.PERCENT)) {
			return (int)(value.getValue() * windowDimension);
		}
		Log.crash("UI Error", new RuntimeException("Unknown unit for UIDimension"));
		return -1;
	}

	public float getUnitizedValue(int screenDimension, int windowDimension) {
		if (unit.equals(Unit.PERCENT)) {
			return (value.getValue() / 50f);
		} else if (unit.equals(Unit.PIXELS)) {
			return ((value.getValue() / (float)screenDimension) * ((float)screenDimension / (float)windowDimension)) * 2f;
		}
		Log.crash("UI Error", new RuntimeException("Unknown unit for UIDimension"));
		return -1;
	}

	public enum Unit {
		PIXELS("px"),
		PERCENT("%");

		String suffix;

		Unit(String suffix) {
			this.suffix = suffix;
		}
	}

	public int getValueDirect() {
		return (int)value.getValue();
	}

	public Unit getUnitDirect() {
		return unit;
	}
}
