package com.terminalvelocitycabbage.engine.utils;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import org.lwjgl.nanovg.NVGColor;

import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class NanoVGUtils {

    public static NVGColor rgba(Color color, NVGColor nvgColor) {
        nvgColor.r(color.r);
        nvgColor.g(color.g);
        nvgColor.b(color.b);
        nvgColor.a(color.a);

        return nvgColor;
    }

    public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);

        return color;
    }

    public static String loadFont(ResourceManager resourceManager, Identifier identifier) {
        var optionalResource = resourceManager.getResource(identifier);
        if (optionalResource.isPresent()) {
            var buff = optionalResource.get().asByteBuffer(true);
            var font = nvgCreateFontMem(ClientBase.getRenderer().getNanoVG(), identifier.toString(), buff, false);
            if (font == -1) {
                Log.error("Could not add font " + identifier);
            }
        }
        return identifier.toString();
    }

    public static ByteBuffer cpToUTF8(int cp) {
        return memUTF8(new String(Character.toChars(cp)), false);
    }
}
