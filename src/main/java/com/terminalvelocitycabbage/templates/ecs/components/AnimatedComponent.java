package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.ecs.Component;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import net.dumbcode.studio.animation.info.AnimationEntryData;
import net.dumbcode.studio.animation.info.AnimationInfo;
import net.dumbcode.studio.animation.info.AnimationLoader;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class AnimatedComponent implements Component {

    private Map<String, AnimationInfo> animations;
    private ModelAnimationHandler handler;
    private Map<String, UUID> activeAnimations;
    private ArrayList<AnimatedModelLoader.Part> parts;

    @Override
    public void setDefaults() {
        animations = new HashMap<>();
        activeAnimations = new HashMap<>();
        parts = new ArrayList<>();
        handler = new ModelAnimationHandler(); //setSrc need to be called on this with the model as the source.
    }

    public AnimatedComponent initWithModel(Model model, Entity source) {
        recursiveOnPart(model.modelParts, parts::add);
        handler.setSrc(source); //TODO verify that this doesn't really matter what the source is
        return this;
    }

    public <T extends Model.Part>void recursiveOnPart(List<T> modelParts, Consumer<AnimatedModelLoader.Part> consumer) {
        for (Model.Part part : modelParts) {
            consumer.accept((AnimatedModelLoader.Part) part);
            recursiveOnPart(part.children, consumer);
        }
    }

    public AnimationEntryData startAnimation(String name) {
        if (!animations.containsKey(name)) {
            Log.crash("Animation Error", new RuntimeException("Animation not found " + name));
        }
        AnimationEntryData data = animations.get(name).data();
        handler.startAnimation(data);
        return data;
    }

    public void stopAnimation(String name) {
        if (animations.containsKey(name)) {
            if (activeAnimations.containsKey(name)) {
                handler.markRemoved(activeAnimations.get(name));
                activeAnimations.remove(name);
            }
        } else {
            Log.crash("Animation Error", new RuntimeException("Animation not found " + name));
        }
    }

    public AnimatedComponent addAnimation(String name, ResourceManager resourceManager, Identifier identifier) {
        try {
            animations.put(name, AnimationLoader.loadAnimation(resourceManager.getResource(identifier).orElseThrow().openStream()));
            return this;
        } catch (IOException e) {
            Log.crash("Animation Error", new RuntimeException("Animation not found " + name, e));
        }
        return null;
    }

    public AnimationInfo getAnimation(String name) {
        if (!animations.containsKey(name)) {
            Log.crash("Animation Error", new RuntimeException("Could not get animation " + name + " in model " + this.toString() + " no value found."));
        }
        return animations.get(name);
    }

    //Delta time is in seconds
    public void animate(float deltaTime) {
        this.handler.animate(parts, deltaTime);
    }
}
