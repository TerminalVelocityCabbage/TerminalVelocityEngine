package com.terminalvelocitycabbage.engine.client.renderer.model.types;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.CuboidShape;

import java.util.Collections;

public class CuboidModel extends Model {

    public CuboidModel(RenderFormat format, RenderMode mode,
                       Vertex frontTL, Vertex frontTR, Vertex frontBR, Vertex frontBL,
                       Vertex backTL, Vertex backTR, Vertex backBR, Vertex backBL) {
        super(format, mode, Collections.singletonList(new Model.Part(
                CuboidShape.createCuboid(
                        frontTL, frontBL, frontBR, frontTR,
                        frontTR, frontBL, backBL, backTL,
                        backTL, backBL, backBR, backTR,
                        backTR, backBR, frontBL, frontTL,
                        backTR, frontTL, frontTR, backTL,
                        frontBL, backBR, backBL, frontBR
                )
        )));
    }
}
