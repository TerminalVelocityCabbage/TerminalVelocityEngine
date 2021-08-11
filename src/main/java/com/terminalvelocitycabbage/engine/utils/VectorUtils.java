package com.terminalvelocitycabbage.engine.utils;

import org.joml.Vector3f;

public class VectorUtils {

    public static Vector3f find2Vec3Mid(final Vector3f v1, final Vector3f v2) {
        return new Vector3f((v1.x + v2.x) / 2f, (v1.y + v2.y) / 2f, (v1.z + v2.z) / 2f);
    }

}
