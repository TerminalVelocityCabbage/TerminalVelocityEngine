package com.terminalvelocitycabbage.engine.client.renderer.model;

public class VertexCounter {

	private int vertexCount;

	public int getVertexIndex(int count) {
		int ret = this.vertexCount;
		this.vertexCount += count;
		return ret;
	}
}
