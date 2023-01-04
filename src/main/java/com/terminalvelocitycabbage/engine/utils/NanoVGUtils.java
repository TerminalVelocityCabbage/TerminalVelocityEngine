package com.terminalvelocitycabbage.engine.utils;

import org.lwjgl.nanovg.NVGColor;

public class NanoVGUtils {

    public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);

        return color;
    }

}
