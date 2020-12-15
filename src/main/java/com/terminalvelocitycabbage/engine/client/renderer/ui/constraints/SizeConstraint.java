package com.terminalvelocitycabbage.engine.client.renderer.ui.constraints;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIConstraint;

public class SizeConstraint extends UIConstraint {

	Type type;
	int value;
	Unit unit;

	public SizeConstraint(Type type, int value, Unit unit) {
		this.type = type;
		this.unit = unit;
		this.value = value;
	}

	@Override
	public void apply() {
		if (unit.equals(Unit.PERCENT)) {
			if (type.equals(Type.WIDTH)) {
				element.width = value * 2 / 100f;
			} else if (type.equals(Type.HEIGHT)) {
				element.height = value * 2 / 100f;
			}
		}
		if (unit.equals(Unit.PIXELS)) {
			if (type.equals(Type.WIDTH)) {
				element.width = value * 2 / element.getCanvas().width;
			} else if (type.equals(Type.HEIGHT)) {
				element.height = value * 2 / element.getCanvas().height;
			}
		}
	}

	public enum Type {
		WIDTH("width"),
		HEIGHT("height");

		final String name;

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum Unit {
		PIXELS("pixels"),
		PERCENT("percent");

		final String name;

		Unit(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
