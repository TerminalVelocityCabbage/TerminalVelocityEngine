package com.terminalvelocitycabbage.templates.marchingcubes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.scheduler.TaskBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.terminalvelocitycabbage.engine.Engine.ID;

public class ChunkManager {

    private final List<Chunk> chunks = new ArrayList<>();

    public void render(ShaderProgram program) {
        for (Chunk chunk : this.chunks) {
            //TODO when this becomes ECS based add a material component to the chunk entities
            //program.setUniform("material", chunk.getMaterial());
            chunk.render();
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

        var bindChunkTask = TaskBuilder.builder()
                .identifier(new Identifier(ID, "bind-chunk-[" + x + "]-[" + y + "]-[" + z + "]"))
                .executes((taskContext) -> chunk.bindChunk((MeshPart) taskContext.previous()))
                .build();

        var marchTask  = TaskBuilder.builder()
                .identifier(new Identifier(ID, "march-chunk-with-[" + x + "]-[" + y + "]-[" + z + "]"))
                .async()
                .executes((taskContext) -> taskContext.setReturn(chunk.generateMarchingMesh()))
                .then(bindChunkTask)
                .build();

        var initChunkTask = TaskBuilder.builder()
                .identifier(new Identifier(ID, "init-chunk-with-[" + x + "]-[" + y + "]-[" + z + "]"))
                .async()
                .executes((taskContext) -> chunk.initializeChunkWith(initializer))
                .then(marchTask)
                .build();

        ClientBase.getScheduler().scheduleTask(initChunkTask);
    }
}