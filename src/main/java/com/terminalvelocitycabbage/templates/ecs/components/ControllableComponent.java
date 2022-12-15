package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class ControllableComponent implements Component {

    InputHandler inputHandler;

    @Override
    public void setDefaults() {
        inputHandler = null;
    }

    public InputHandler setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        return this.inputHandler;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
