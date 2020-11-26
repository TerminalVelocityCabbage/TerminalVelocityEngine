package com.terminalvelocitycabbage.engine.client.renderer.ui.constraints;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIConstraint;

public class PositionConstraint extends UIConstraint {

	Type type;
	float value;

	public PositionConstraint(Type type, float value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public void apply() {
		if (type.equals(Type.LEFT) || type.equals(Type.HORIZONTAL) || type.equals(Type.ALL)) {
			element.marginLeft += value;
		}
		if (type.equals(Type.RIGHT) || type.equals(Type.HORIZONTAL) || type.equals(Type.ALL)) {
			element.marginRight += value;
		}
		if (type.equals(Type.TOP) || type.equals(Type.VERTICAL) || type.equals(Type.ALL)) {
			element.marginTop += value;
		}
		if (type.equals(Type.BOTTOM) || type.equals(Type.VERTICAL) || type.equals(Type.ALL)) {
			element.marginBottom += value;
		}
	}

	public enum Type {
		LEFT("left"),
		RIGHT("right"),
		TOP("top"),
		BOTTOM("bottom"),
		HORIZONTAL("horizontal"),
		VERTICAL("vertical"),
		ALL("all");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
