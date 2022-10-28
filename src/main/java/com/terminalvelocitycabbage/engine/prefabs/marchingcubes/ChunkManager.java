package com.terminalvelocitycabbage.engine.prefabs.marchingcubes;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ChunkManager extends EmptyGameObject {

    private final List<Chunk> chunks = new ArrayList<>();

    public ChunkManager() {
        super(new Vector3f(), new Quaternionf(), new Vector3f(1F));
    }

    public void render(ShaderProgram program) {
        for (Chunk chunk : this.chunks) {
            program.setUniform("material", chunk.getMaterial());
            chunk.render();
        }
    }

    public void renderDebug(ShaderProgram program, Matrix4f viewMatrix) {
        for (Chunk chunk : this.chunks) {
            program.setUniform("modelViewMatrix", chunk.getBoundary().getModelViewMatrix(viewMatrix));
            program.setUniform("normalTransformationMatrix", chunk.getBoundary().getTransformationMatrix());
            chunk.getBoundary().render();
        }
    }

    public void destroy() {
        for (Chunk chunk : this.chunks) {
            chunk.destroy();
        }
    }

    public void startChunk(int x, int y, int z, Chunk.ChunkInitializer initializer, Chunk.ColourGetter colourGetter) {
        Chunk chunk = new Chunk(x, y, z, colourGetter);
        this.chunks.add(chunk);
        chunk.initializeChunkWith(initializer);
    }

    @Override
    public void update() {
    }
}