package com.terminalvelocitycabbage.engine.client.renderer.ui;

public class UIDimension {

	int value;
	Unit unit;

	public UIDimension(int value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public int getPixelValue(int screenDimension) {
		if (unit.equals(Unit.PIXELS)) {
			return value;
		} else if (unit.equals(Unit.PERCENT)) {
			return value * screenDimension;
		}
		throw new RuntimeException("Unknown unit for UIDimension");
	}

	public float getUnitizedValue(int screenDimension) {
		if (unit.equals(Unit.PERCENT)) {
			return value / 50f;
		} else if (unit.equals(Unit.PIXELS)) {
			return (value / (screenDimension / 2f));
		}
		throw new RuntimeException("Unknown unit for UIDimension");
	}

	public enum Unit {
		PIXELS("px"),
		PERCENT("%");

		String suffix;

		Unit(String suffix) {
			this.suffix = suffix;
		}
	}

}
