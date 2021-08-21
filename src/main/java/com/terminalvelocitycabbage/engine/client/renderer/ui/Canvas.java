package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;
import com.terminalvelocitycabbage.engine.events.EventContext;
import com.terminalvelocitycabbage.engine.events.HandleEvent;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Canvas extends UIRenderable {

	Window window;
	boolean active;
	List<Container> containers;

	public Canvas(Window window) {
		super(new Style());
		this.window = window;
		active = false;
		this.containers = new ArrayList<>();
		ClientBase.instance.addEventHandler(EventContext.CLIENT, this);
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

	public void addContainer(Container container) {
		container.setParent(this);
		containers.add(container);
		container.bind();
	}

	public Model getRectangle() {
		return rectangle;
	}

	@HandleEvent(WindowResizeEvent.EVENT)
	public void onWindowResize(WindowResizeEvent event) {
		queueUpdate();
	}

	@Override
	public void update() {

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
			leftX += style.getMargin().left().getUnitizedValue(screenWidth, windowWidth);
			rightX -= style.getMargin().right().getUnitizedValue(screenWidth, windowWidth);
			topY -= style.getMargin().top().getUnitizedValue(screenHeight, windowHeight);
			bottomY += style.getMargin().bottom().getUnitizedValue(screenHeight, windowHeight);

			//Set the vertexes based on the calculated positions
			rectangle.vertices[0].setXYZ(leftX, topY, 0);
			rectangle.vertices[1].setXYZ(leftX, bottomY, 0);
			rectangle.vertices[2].setXYZ(rightX, bottomY, 0);
			rectangle.vertices[3].setXYZ(rightX, topY, 0);

			rectangle.update(new Vector3f(), new Quaternionf().identity(), new Vector3f(1F)); //TODO
			for (Container container : containers) {
				container.queueUpdate();
			}
			this.needsUpdate = false;
		}
	}

	@Override
	public void render() {
		if (isActive()) {
			super.render();
		}
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

	@Override
	public void destroy() {
		super.destroy();
		for (UIRenderable element : containers) {
			element.destroy();
		}
	}

	@Override
	public Canvas onHover(Consumer<UIRenderable> consumer) {
		return (Canvas) super.onHover(consumer);
	}

	@Override
	public Canvas onUnHover(Consumer<UIRenderable> consumer) {
		return (Canvas) super.onUnHover(consumer);
	}

	@Override
	public Canvas onClick(Consumer<UIRenderable> consumer) {
		return (Canvas) super.onClick(consumer);
	}

	@Override
	public Canvas onRightClick(Consumer<UIRenderable> consumer) {
		return (Canvas) super.onRightClick(consumer);
	}

	@Override
	public Canvas onDoubleClick(int tickTime, Consumer<UIRenderable> consumer) {
		return (Canvas) super.onDoubleClick(tickTime, consumer);
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
