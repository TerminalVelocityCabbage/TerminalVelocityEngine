package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIElement;

public class UICanvas extends UIElement {

	public UICanvas() {
		isRoot = true;
	}

	@Override
	public UIElement getCanvas() {
		return this;
	}
}
