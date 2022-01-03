package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import net.dumbcode.studio.animation.info.AnimationEntryData;
import net.dumbcode.studio.animation.info.AnimationInfo;
import net.dumbcode.studio.animation.info.AnimationLoader;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import net.dumbcode.studio.model.ModelInfo;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AnimatedModel extends Model {

    public Map<String, AnimationInfo> animations;
    public final ModelAnimationHandler handler;
    private Map<String, UUID> activeAnimations;
    private ArrayList<AnimatedModelLoader.Part> parts;

    public AnimatedModel(ModelInfo model) {
        super(RenderFormat.POSITION_UV_NORMAL, new RenderMode(RenderMode.Modes.TRIANGLES), model.getRoots().stream().map(AnimatedModelLoader.Part::createPart).collect(Collectors.toList()));

        this.animations = new HashMap<>();
        this.activeAnimations = new HashMap<>();

        this.parts = new ArrayList<>();
        recursiveOnPart(modelParts, parts::add);
        this.handler = new ModelAnimationHandler(model.getOrder());
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

    public AnimationInfo addAnimation(String name, ResourceManager resourceManager, Identifier identifier) {
        try {
            AnimationInfo info = AnimationLoader.loadAnimation(resourceManager.getResource(identifier).orElseThrow().openStream());
            animations.put(name, info);
            return info;
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
