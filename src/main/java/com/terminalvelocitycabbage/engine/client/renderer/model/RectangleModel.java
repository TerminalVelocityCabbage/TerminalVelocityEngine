package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;

import java.util.Arrays;
import java.util.Collections;

public class RectangleModel extends Model {

    public final Vertex[] vertices;

    public RectangleModel(RenderFormat format, Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        super(format, Collections.singletonList(new Part(new MeshPart(
            new Vertex[] { v1, v2, v3, v4 },
            new int[] { 0, 1, 2, 2, 3, 0 }
        ))));
        this.vertices = new Vertex[] { v1, v2, v3, v4 };
    }

    @Override
    public String toString() {
        return "RectangleModel{" +
                "vertices=" + Arrays.toString(vertices) +
                '}';
    }
}
