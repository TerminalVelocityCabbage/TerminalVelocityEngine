package com.terminalvelocitycabbage.templates.gameobjects.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;

public class PointShape {

    public static MeshPart createPoint(Vertex point) {
        return new MeshPart(
                new Vertex[]{ point },
                new int[] { 0 }
        );
    }
}
