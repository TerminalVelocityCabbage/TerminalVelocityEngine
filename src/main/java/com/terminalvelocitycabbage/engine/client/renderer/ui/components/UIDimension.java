package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import com.terminalvelocitycabbage.engine.debug.Log;

public class UIDimension {

	int value;
	Unit unit;

	public UIDimension(int value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public int getPixelValue(int windowDimension) {
		if (unit.equals(Unit.PIXELS)) {
			return value;
		} else if (unit.equals(Unit.PERCENT)) {
			return value * windowDimension;
		}
		Log.crash("UI Error", new RuntimeException("Unknown unit for UIDimension"));
		return -1;
	}

	public float getUnitizedValue(int screenDimension, int windowDimension) {
		if (unit.equals(Unit.PERCENT)) {
			return value / 50f;
		} else if (unit.equals(Unit.PIXELS)) {
			return (((float)value / (float)screenDimension) * ((float)screenDimension / (float)windowDimension)) * 2f;
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
		return value;
	}

	public Unit getUnitDirect() {
		return unit;
	}
}
