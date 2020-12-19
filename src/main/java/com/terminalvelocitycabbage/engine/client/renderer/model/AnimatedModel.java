package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import net.dumbcode.studio.animation.info.AnimationInfo;
import net.dumbcode.studio.animation.info.AnimationLoader;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import net.dumbcode.studio.model.ModelInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AnimatedModel extends Model {

    public Map<String, AnimationInfo> animations;
    public final ModelAnimationHandler handler;

    public AnimatedModel(ModelInfo model) {
        super(model.getRoots().stream().map(AnimatedModelLoader.Part::createPart).collect(Collectors.toList()));

        this.animations = new HashMap<>();

        List<AnimatedModelLoader.Part> partMap = new ArrayList<>();
        recursiveOnPart(modelParts, partMap::add);
        this.handler = new ModelAnimationHandler(model.getOrder(), partMap);
    }

    public <T extends Model.Part>void recursiveOnPart(List<T> modelParts, Consumer<AnimatedModelLoader.Part> consumer) {
        for (Model.Part part : modelParts) {
            consumer.accept((AnimatedModelLoader.Part) part);
            recursiveOnPart(part.children, consumer);
        }
    }

    public AnimationInfo addAnimation(String name, ResourceManager resourceManager, Identifier identifier) {
        try {
            AnimationInfo info = AnimationLoader.loadAnimation(resourceManager.getResource(identifier).orElseThrow().openStream());
            animations.put(name, info);
            return info;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnimationInfo getAnimation(String name) {
        if (animations.containsKey(name)) {
            return animations.get(name);
        } else {
            throw new RuntimeException("Could not get animation " + name + " in model " + this.toString() + " no value found.");
        }
    }
}
