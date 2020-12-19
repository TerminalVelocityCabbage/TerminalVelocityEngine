package com.terminalvelocitycabbage.engine.client.renderer.ui;

public class UIAnchor {

	AnchorPoint anchorPoint;
	AnchorDirection anchorDirection;

	public UIAnchor(AnchorPoint anchorPoint) {
		this(anchorPoint, anchorPoint.defaultAnchorDirection);
	}

	public UIAnchor(AnchorPoint anchorPoint, AnchorDirection anchorDirection) {
		this.anchorPoint = anchorPoint;
		this.anchorDirection = anchorDirection;
	}

	public AnchorPoint getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(AnchorPoint anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public AnchorDirection getAnchorDirection() {
		return anchorDirection;
	}

	public void setAnchorDirection(AnchorDirection anchorDirection) {
		this.anchorDirection = anchorDirection;
	}
}
