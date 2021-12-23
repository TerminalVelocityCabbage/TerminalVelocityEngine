package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.RectangleModel;
import com.terminalvelocitycabbage.engine.events.EventContext;
import com.terminalvelocitycabbage.engine.events.HandleEvent;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Canvas extends UIRenderable<Canvas> {

	Window window;
	boolean active;
	List<Container> containers;

	public Model model;

	public Canvas(Window window) {
		super();
		this.window = window;
		active = false;
		this.containers = new ArrayList<>();
		ClientBase.instance.addEventHandler(EventContext.CLIENT, this);
		this.backgroundAlpha = new AnimatableUIValue(0);

		this.model = new Model(RenderFormat.UI, new ArrayList<>(Collections.singletonList(this.part)));
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	@Override
	public void onPartsChange() {
		this.model.modelParts.clear();
		this.model.modelParts.add(this.part);
		for (UIRenderable child : this.getAllChildren()) {
			this.model.modelParts.add(child.part);
		}
		this.model.onPartsChange();
		this.model.bind();
	}

	public void uiChanged() {
		this.model.bind();
	}

	public void addContainer(Container container) {
		container.setParent(this);
		containers.add(container);
		this.onPartsChange();
	}

	@HandleEvent(WindowResizeEvent.EVENT)
	public void onWindowResize(WindowResizeEvent event) {
		queueUpdate();
	}

	private boolean needsRebinding() {
		if(this.needsUpdate) {
			return true;
		}
		for (UIRenderable child : this.getAllChildren()) {
			if(child.needsUpdate) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update() {
		if(!this.needsRebinding()) {
			return;
		}
		if (needsUpdate && isActive()) {

			float leftX = -1f;
			float rightX = 1f;
			float topY = 1f;
			float bottomY = -1f;

			//Window dimensions
			int windowWidth = getWindow().width();
			int windowHeight = getWindow().height();

			//Screen dimensions
			int screenWidth = getWindow().monitorWidth();
			int screenHeight = getWindow().monitorHeight();

			//Offset based on margins
			leftX += getMargin().left().getUnitizedValue(screenWidth, windowWidth);
			rightX -= getMargin().right().getUnitizedValue(screenWidth, windowWidth);
			topY -= getMargin().top().getUnitizedValue(screenHeight, windowHeight);
			bottomY += getMargin().bottom().getUnitizedValue(screenHeight, windowHeight);

			//Set the vertexes based on the calculated positions
			vertex1.setXYZ(leftX, topY, 0);
			vertex2.setXYZ(leftX, bottomY, 0);
			vertex3.setXYZ(rightX, bottomY, 0);
			vertex4.setXYZ(rightX, topY, 0);

			for (Container container : containers) {
				container.queueUpdate();
			}
			this.needsUpdate = false;
		}

		for (Container container : this.containers) {
			container.update();
		}

		this.model.bind();
		this.model.update(new Vector3f(), new Quaternionf(), new Vector3f(1));
		this.model.mesh.dumpAsObj();
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public void queueUpdate() {
		super.queueUpdate();
		containers.forEach(UIRenderable::queueUpdate);
	}

	public Window getWindow() {
		return window;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public List<UIRenderable> getAllChildren() {

		//Add all of this canvas' containers to a list
		List<Container> allContainers = new ArrayList<>(containers);
		//For each of those containers recursively add all containers in that tree
		containers.forEach(container -> allContainers.addAll(container.getAllContainers()));

		//Add all of the containers in this canvas' tree
		List<UIRenderable> elements = new ArrayList<>(allContainers);
		//Add all elements of the containers to the list of children
		allContainers.forEach(container -> elements.addAll(container.getElements()));
		return elements;
	}
}
