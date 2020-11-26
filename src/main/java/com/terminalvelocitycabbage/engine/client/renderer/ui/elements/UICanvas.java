package com.terminalvelocitycabbage.engine.client.renderer.ui.elements;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIElement;

public class UICanvas extends UIElement {

	public UICanvas() {
		isRoot = true;
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
