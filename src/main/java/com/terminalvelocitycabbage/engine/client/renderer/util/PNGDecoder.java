package com.terminalvelocitycabbage.engine.client.renderer.util;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class PNGDecoder {

    int width;
    int height;
    int numComponents;
    boolean hdr;

    ByteBuffer imageBuffer;

    public PNGDecoder(ResourceManager resourceManager, Identifier identifier) throws IOException {
        if (resourceManager.getResource(identifier).isPresent()) {
            imageBuffer = resourceManager.getResource(identifier).get().asByteBuffer();
        }
    }

    public ByteBuffer decode() {
        ByteBuffer image;

        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Decode the image
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                Log.crash("Image Loading Error", new RuntimeException("Failed to load image: " + stbi_failure_reason()));
            }

            width = w.get(0);
            height = h.get(0);
            numComponents = comp.get(0);
            hdr = stbi_is_hdr_from_memory(imageBuffer);
        }

        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
