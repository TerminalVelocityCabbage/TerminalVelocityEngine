package com.terminalvelocitycabbage.engine.client.renderer.model.loader;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.templates.entity.shapes.CuboidShape;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import net.dumbcode.studio.animation.instance.AnimatedCube;
import net.dumbcode.studio.model.CubeInfo;
import net.dumbcode.studio.model.ModelLoader;
import net.dumbcode.studio.model.RotationOrder;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.stream.Collectors;

public class AnimatedModelLoader {

	public static class Part extends Model.Part implements AnimatedCube {

		CubeInfo cube;

		public Part(CubeInfo cube, float[][] uv) {
			super(
					CuboidShape.createCuboid(
						Vertex.positionUvNormal(1, 1, 0, uv[0][0], uv[0][3], 1, 0, 0),
						Vertex.positionUvNormal(1, 1, 1, uv[0][2], uv[0][3], 1, 0, 0),
						Vertex.positionUvNormal(1, 0, 1, uv[0][2], uv[0][1], 1, 0, 0),
						Vertex.positionUvNormal(1, 0, 0, uv[0][0], uv[0][1], 1, 0, 0),
						Vertex.positionUvNormal(0, 1, 1, uv[1][0], uv[1][3], -1, 0, 0),
						Vertex.positionUvNormal(0, 1, 0, uv[1][2], uv[1][3], -1, 0, 0),
						Vertex.positionUvNormal(0, 0, 0, uv[1][2], uv[1][1], -1, 0, 0),
						Vertex.positionUvNormal(0, 0, 1, uv[1][0], uv[1][1], -1, 0, 0),
						Vertex.positionUvNormal(1, 1, 0, uv[2][0], uv[2][3], 0, 1, 0),
						Vertex.positionUvNormal(0, 1, 0, uv[2][2], uv[2][3], 0, 1, 0),
						Vertex.positionUvNormal(0, 1, 1, uv[2][2], uv[2][1], 0, 1, 0),
						Vertex.positionUvNormal(1, 1, 1, uv[2][0], uv[2][1], 0, 1, 0),
						Vertex.positionUvNormal(1, 0, 1, uv[3][0], uv[3][3], 0, -1, 0),
						Vertex.positionUvNormal(0, 0, 1, uv[3][2], uv[3][3], 0, -1, 0),
						Vertex.positionUvNormal(0, 0, 0, uv[3][2], uv[3][1], 0, -1, 0),
						Vertex.positionUvNormal(1, 0, 0, uv[3][0], uv[3][1], 0, -1, 0),
						Vertex.positionUvNormal(1, 1, 1, uv[4][0], uv[4][3], 0, 0, 1),
						Vertex.positionUvNormal(0, 1, 1, uv[4][2], uv[4][3], 0, 0, 1),
						Vertex.positionUvNormal(0, 0, 1, uv[4][2], uv[4][1], 0, 0, 1),
						Vertex.positionUvNormal(1, 0, 1, uv[4][0], uv[4][1], 0, 0, 1),
						Vertex.positionUvNormal(0, 1, 0, uv[5][0], uv[5][3], 0, 0, -1),
						Vertex.positionUvNormal(1, 1, 0, uv[5][2], uv[5][3], 0, 0, -1),
						Vertex.positionUvNormal(1, 0, 0, uv[5][2], uv[5][1], 0, 0, -1),
						Vertex.positionUvNormal(0, 0, 0, uv[5][0], uv[5][1], 0, 0, -1)
					),
					new Vector3f(cube.getOffset()[0] - cube.getCubeGrow()[0], cube.getOffset()[1] - cube.getCubeGrow()[1], cube.getOffset()[2] - cube.getCubeGrow()[2]),
					new Vector3f(cube.getRotationPoint()),
					new Quaternionf().rotationXYZ(cube.getRotation()[0], cube.getRotation()[1], cube.getRotation()[2]),
					new Vector3f(cube.getDimensions()[0] + (2*cube.getCubeGrow()[0]), cube.getDimensions()[1] + (2*cube.getCubeGrow()[1]), cube.getDimensions()[2] + (2*cube.getCubeGrow()[2])),
					cube.getChildren().stream().map(Part::createPart).collect(Collectors.toList())
			);
			this.cube = cube;
		}

		public static Part createPart(CubeInfo info) {
			float[][] uv = info.getGeneratedUVs();
			return new Part(info, uv);
		}

		@Override
		public CubeInfo getInfo() {
			return this.cube;
		}

		@Override
		public void setRotation(float x, float y, float z) {
			this.rotation.rotationXYZ(x, y, z);
		}

		@Override
		public void setPosition(float x, float y, float z) {
			this.position.set(x, y, z);
		}

		@Override
		public void setCubeGrow(float x, float y, float z) {
			float[] off = this.cube.getOffset();
			this.offset.set(off[0]-x, off[1]-y, off[2]-z);

			int[] dimension = this.cube.getDimensions();
			this.scale.set(dimension[0]+2*x, dimension[1]+2*y, dimension[2]+2*z);
		}
	}

	public static Model load(ResourceManager resourceManager, Identifier model) {
		return resourceManager.getResource(model)
				.map(resource -> {
					try {
						return resource.openStream();
					} catch (IOException e) {
						Log.crash("Model Loading error", "failed to load animation", new RuntimeException("No resource found for identifier: " + model.toString(), e));
						return null;
					}
				})
				.map(stream -> {
					try {
						return ModelLoader.loadModel(stream, RotationOrder.XYZ);
					} catch (IOException e) {
						Log.crash("Model Loading error", "failed to load animation", new RuntimeException("Could not read model: " + model.toString(), e));
						return null;
					}
				})
				.map(Model::new)
				.orElseThrow();
	}
}
