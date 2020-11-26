package com.terminalvelocitycabbage.engine.client.renderer.ui.constraints;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIConstraint;
import com.terminalvelocitycabbage.engine.debug.Log;

public class CenterConstraint extends UIConstraint {

	Direction direction;

	public CenterConstraint(Direction direction) {
		this.direction = direction;
	}

	@Override
	public void apply() {
		if (direction.equals(Direction.HORIZONTAL) || direction.equals(Direction.BOTH)) {
			if (element.width == 0f) {
				Log.warn("element of 0 width attempted to be centered horizontally, this may be of cause adding the center constraint before width constraints.");
			}
			if (element.parent.itemsLeftAligned) {
				element.marginLeft += (element.parent.width / 2) - (element.width / 2);
			} else {
				element.marginRight += (element.parent.width / 2) - (element.width / 2);
			}
		}
		if (direction.equals(Direction.VERTICAL) || direction.equals(Direction.BOTH)) {
			if (element.width == 0f) {
				Log.warn("element of 0 width attempted to be centered horizontally, this may be of cause adding the center constraint before width constraints.");
			}
			if (element.parent.itemsTopStart) {
				element.marginTop += (element.parent.height / 2) - (element.height / 2);
			} else {
				element.marginBottom += (element.parent.height / 2) - (element.height / 2);
			}
		}
	}

	public enum Direction {
		HORIZONTAL("horizontal"),
		VERTICAL("vertical"),
		BOTH("both");

		final String name;

		Direction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
