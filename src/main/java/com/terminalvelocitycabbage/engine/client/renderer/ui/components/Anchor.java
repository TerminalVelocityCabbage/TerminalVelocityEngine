package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

//TODO convert to record
public class Anchor {

	AnchorPoint anchorPoint;
	AnchorDirection anchorDirection;

	public Anchor(AnchorPoint anchorPoint) {
		this(anchorPoint, anchorPoint.defaultAnchorDirection);
	}

	public Anchor(AnchorPoint anchorPoint, AnchorDirection anchorDirection) {
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
