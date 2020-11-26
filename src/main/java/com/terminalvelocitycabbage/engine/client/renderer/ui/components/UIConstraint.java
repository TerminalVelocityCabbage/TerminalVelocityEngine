package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

public abstract class UIConstraint {

	public UIElement element;

	public void setParent(UIElement element) {
		this.element = element;
	}

	public abstract void apply();

}
