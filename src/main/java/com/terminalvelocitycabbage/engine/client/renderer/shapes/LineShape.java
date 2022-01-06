package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;

public class LineShape {

    public static MeshPart createLine(Vertex start, Vertex end) {
        return new MeshPart(
                new Vertex[]{ start, end },
                new int[] { 0, 1 }
        );
    }
}
