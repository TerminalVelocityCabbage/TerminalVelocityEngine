package com.terminalvelocitycabbage.engine.util;

import org.joml.Vector4f;
import org.joml.Vector4i;

public class Color {


    /**
     * Converts 0-255 color values to 0-1 color values
     * @param originalColor an rgba vector 4i from range 0 - 255 color values
     * @return a clamped 0-1 float rgba from the given originalColor
     */
    public static Vector4f clampColor(Vector4i originalColor) {
        return new Vector4f(
                (float)originalColor.x / 255,
                (float)originalColor.y / 255,
                (float)originalColor.z / 255,
                (float)originalColor.w / 255
        );
    }

}
