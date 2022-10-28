package com.terminalvelocitycabbage.engine.prefabs.gameobjects.shapes;

import com.terminalvelocitycabbage.engine.utils.VectorUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Modified from: "https://github.com/mwohlf/lwjgl-basics/blob/master/src/main/java/net/wohlfart/gl/elements/debug/Icosphere.java"
public class IcosphereShape {

    //Create the 3 intersecting planes that make up a Icosahedron
    private static final float T = (1.0f + ((float) Math.sqrt(5))) / 2.0f;
    private static final List<Vector3f> UNIT_ICO_POSITIONS = new ArrayList<>(
            Arrays.asList(
           new Vector3f(-1, T, 0),
           new Vector3f(1, T, 0),
           new Vector3f(-1, -T, 0),
           new Vector3f(1, -T, 0),
           new Vector3f(0f, -1, T),
           new Vector3f(0f, 1, T),
           new Vector3f(0f, -1, -T),
           new Vector3f(0f, 1, -T),
           new Vector3f(T, 0, -1),
           new Vector3f(T, 0, 1),
           new Vector3f(-T, 0, -1),
           new Vector3f(-T, 0, 1))
    );

    //Connect the corners of these planes into the most basic 20 sided IcoSphere
    private static final int[] UNIT_INDICES = new int[]{
            0, 11, 5,
            0, 5, 1,
            0, 1, 7,
            0, 7, 10,
            0, 10, 11,
            1, 5, 9,
            5, 11, 4,
            11, 10, 2,
            10, 7, 6,
            7, 1, 8,
            3, 9, 4,
            3, 4, 2,
            3, 2, 6,
            3, 6, 8,
            3, 8, 9,
            4, 9, 5,
            2, 4, 11,
            6, 2, 10,
            8, 6, 7,
            9, 8, 1
    };

    public static Vector3f[] getUnitFace(int i) {
        return new Vector3f[]{
            UNIT_ICO_POSITIONS.get(UNIT_INDICES[i * 3]),
            UNIT_ICO_POSITIONS.get(UNIT_INDICES[i * 3 + 1]),
            UNIT_ICO_POSITIONS.get(UNIT_INDICES[i * 3 + 2])
        };
    }

    public static List<Vector3f> getVerticesForDivisions(int faceID, int divisions) {

        Vector3f[] existingFace = getUnitFace(faceID);

        // get the 3 corners of the triangle
        final Vector3f v1 = new Vector3f(existingFace[0]); // top
        final Vector3f v2 = new Vector3f(existingFace[1]); // bottom left
        final Vector3f v3 = new Vector3f(existingFace[2]); // bottom right

        //Create a place for the new vertex locations to live
        List<Vector3f> newVertexLocations = new ArrayList<>();

        //Get outside vertexes of new triangle
        int numSideTris = getSideTrianglesForDivisions(divisions);

        //Add top vertex first
        newVertexLocations.add(v1);

        //i is the current row starting at 1 because we can skip the top vertex as we already added it.
        for (int i = 1; i < numSideTris + 1; i++) {
            //Get the start and end of the current row by finding a point on the line from top to bottom left & top to bottom right
            Vector3f firstInRow = VectorUtils.findPercentDistVector(v1, v2, (1f/numSideTris) * i);
            Vector3f lastInRow = VectorUtils.findPercentDistVector(v1, v3, (1f/numSideTris) * i);

            for (int j = 0; j < i+1; j++) {
                newVertexLocations.add(VectorUtils.findPercentDistVector(firstInRow ,lastInRow, j / (float) i));
            }
        }

        //Normalize all the vertices to get them on the sphere
        for (Vector3f vec : newVertexLocations) {
            vec.normalize();
        }
        
        return newVertexLocations;
    }

    public static int[] getIndicesForDivisions(int divisions) {

        //Get indices for the faces of the triangle
        int numUp = getPointUpTrisForDivisions(divisions);
        int numDown = getPointDownTrisForDivisions(divisions);
        int[] newIndices = new int[(numUp * 3) + (numDown * 3)];

        //Get indices for triangles that point up
        for (int i = 0; i < numUp; i++) {
            newIndices[i * 3] = i;
            newIndices[i * 3 + 1] = (i + (getCurrentRowIndex(i) + 1));
            newIndices[i * 3 + 2] = (newIndices[i * 3 + 1] + 1);
        }

        //Get indices for triangles that point down
        for (int i = 0; i < numDown; i++) {
            newIndices[i * 3 + (numUp * 3)] = (i + (getCurrentRowIndex(i) + 1) + 1);
            newIndices[i * 3 + 1 + (numUp * 3)] = (newIndices[i * 3 + (numUp * 3)] - 1);
            newIndices[i * 3 + 2 + (numUp * 3)] = (newIndices[i * 3 + (numUp * 3)] + (getCurrentRowIndex(i) + 1) + 1);
        }

        return newIndices;
    }

    /**
     * @param i the current index of the triangle vertex to test
     * @return the row starting from 0 the the current vertex is positioned in
     */
    public static int getCurrentRowIndex(int i) {
        return (int)Math.floor((-1 + Math.sqrt(1 + 8*i)) / 2);
    }

    /**
     * @param divisions the number of times the face in question has been divided
     * @return the number of triangles that wil border any given edge of unit triangle for the number of divisions provided
     */
    private static int getSideTrianglesForDivisions(int divisions) {
        return (int) Math.pow(2, divisions);
    }

    /**
     * @param divisions the number of times the face in question has been divided
     * @return the number of triangles that face will have
     */
    private static int getNumTrisForDivisions(int divisions) {
        divisions = ((int) Math.pow(2, divisions));
        return ((divisions * (divisions + 1))/2) + (((divisions - 1) * (divisions))/2);
    }

    /**
     * @param divisions the number of times the face in question has been divided
     * @return the number of vertices that will make up a face divided divisions times
     */
    private static int getNumVerticesForDivisions(int divisions) {
        divisions = ((int) Math.pow(2, divisions)) + 1;
        return ((divisions * (divisions + 1))/2);
    }

    /**
     * Uses the total number of vertices - the number on the bottom row to get the number of face up vertices
     *
     * @param divisions the number of times the face in question has been divided
     * @return the number of point 'up' triangles in a face
     */
    private static int getPointUpTrisForDivisions(int divisions) {
        return (getNumVerticesForDivisions(divisions) - getSideTrianglesForDivisions(divisions)) - 1;
    }

    /**
     * @param divisions the number of times the face in question has been divided
     * @return the number of inner triangle that point 'down'
     */
    private static int getPointDownTrisForDivisions(int divisions) {
        return getNumTrisForDivisions(divisions) - getPointUpTrisForDivisions(divisions);
    }
}