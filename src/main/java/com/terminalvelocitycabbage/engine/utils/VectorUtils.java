package com.terminalvelocitycabbage.engine.utils;

import org.joml.Vector3f;

public class VectorUtils {

    /**
     * @param v1 the first vector of the line
     * @param v2 the last vector of the line
     * @return a new Vector3f at the middle of the line drawn between v1 and v2
     */
    public static Vector3f find2Vec3Mid(final Vector3f v1, final Vector3f v2) {
        return new Vector3f((v1.x + v2.x) / 2f, (v1.y + v2.y) / 2f, (v1.z + v2.z) / 2f);
    }

    /**
     * @param startVec The start of the line
     * @param endVec The end of the line
     * @param position The percentage (0-1) the resultant vector should be placed along the line between start and end
     * @return a new Vector3f at the requested location
     */
    public static Vector3f findPercentDistVector(final Vector3f startVec, final Vector3f endVec, float position) {
        return new Vector3f((startVec.x + endVec.x) * position, (startVec.y + endVec.y) * position, (startVec.z + endVec.z) * position);
    }

}
