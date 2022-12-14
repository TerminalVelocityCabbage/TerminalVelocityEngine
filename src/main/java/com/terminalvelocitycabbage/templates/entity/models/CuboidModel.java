package com.terminalvelocitycabbage.templates.entity.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.templates.entity.shapes.CuboidShape;

import java.util.Collections;

public class CuboidModel extends Model {

    public CuboidModel(RenderFormat format, RenderMode mode,
                       Vertex frontTL, Vertex frontTR, Vertex frontBR, Vertex frontBL,
                       Vertex backTL, Vertex backTR, Vertex backBR, Vertex backBL) {
        super(format, mode, Collections.singletonList(new Model.Part(
                CuboidShape.createCuboid(
                        frontTL, frontBL, frontBR, frontTR,
                        frontTR, frontBR, backBR, backTR,
                        backTL, backBL, backBR, backTR,
                        backTL, backBL, frontBL, frontTL,
                        backTL, frontTL, frontTR, backTR,
                        frontBL, backBL, backBR, frontBR
                )
        )));
    }
}
