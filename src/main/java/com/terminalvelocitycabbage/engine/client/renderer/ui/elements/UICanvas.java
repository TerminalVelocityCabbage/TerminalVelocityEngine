package com.terminalvelocitycabbage.engine.client.renderer.ui.elements;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIElement;
import org.joml.Vector3f;

public class UICanvas extends UIElement {

	public UICanvas() {
		color = new Vector3f(1, 1, 1);
		backgroundOpacity = 1;
	}

	@Override
	public UIElement getCanvas() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public UICanvas build() {
		return super.build(null);
	}
}
