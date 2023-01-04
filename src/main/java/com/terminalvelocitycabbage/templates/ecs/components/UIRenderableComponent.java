package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;

public interface UIRenderableComponent extends Component {

    float xPos = 0;
    float yPos = 0;

    void draw(long vg);

}
