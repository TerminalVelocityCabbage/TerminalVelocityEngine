package com.terminalvelocitycabbage.templates.gameobjects.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;

public class LineShapes {

    public static MeshPart createLine(Vertex start, Vertex end) {
        return new MeshPart(
                new Vertex[]{ start, end },
                new int[] { 0, 1 }
        );
    }

    //Vertex (back/front)(bottom/top)(left/right)
    public static MeshPart createLineBox(Vertex btl, Vertex btr, Vertex ftr, Vertex ftl, Vertex bbl, Vertex bbr, Vertex fbr, Vertex fbl) {
        return new MeshPart(
                new Vertex[]{ btl, btr, ftr, ftl, bbl, bbr, fbr, fbl },
                new int[] { 0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7, 7, 4, 3, 7, 0, 4, 1, 5, 2, 6 }
        );
    }
}
