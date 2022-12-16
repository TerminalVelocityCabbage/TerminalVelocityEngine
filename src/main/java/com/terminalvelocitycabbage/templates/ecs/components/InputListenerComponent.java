package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.input.InputFrame;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class InputListenerComponent implements Component {

    InputFrame currentInputFrame;

    @Override
    public void setDefaults() {
        currentInputFrame = new InputFrame();
    }

    public InputFrame getCurrentInputFrame() {
        return currentInputFrame;
    }

    public void setCurrentInputFrame(InputFrame currentInputFrame) {
        this.currentInputFrame = currentInputFrame;
    }
}
