package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;
import com.terminalvelocitycabbage.engine.utils.VectorUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Modified from: "https://github.com/mwohlf/lwjgl-basics/blob/master/src/main/java/net/wohlfart/gl/elements/debug/Icosphere.java"
public class Icosphere {

    //Create the 3 intersecting planes that make up a Icosahedron
    private static final float T = (1.0f + ((float) Math.sqrt(5))) / 2.0f;
    private static final List<ModelVertex> UNIT_ICO_VERTICES = new ArrayList<>(
            Arrays.asList(
            new ModelVertex().setXYZ(-1, T, 0).setNormal(-1f, T, 0).setUv(0, 0),
            new ModelVertex().setXYZ(1, T, 0).setNormal(1f, T, 0).setUv(0, 0),
            new ModelVertex().setXYZ(-1, -T, 0).setNormal(-1f, -T, 0).setUv(0, 0),
            new ModelVertex().setXYZ(1, -T, 0).setNormal(1f, -T, 0).setUv(0, 0),
            new ModelVertex().setXYZ(0f, -1, T).setNormal(0f, -1, T).setUv(0, 0),
            new ModelVertex().setXYZ(0f, 1, T).setNormal(0f, 1, T).setUv(0, 0),
            new ModelVertex().setXYZ(0f, -1, -T).setNormal(0f, -1, -T).setUv(0, 0),
            new ModelVertex().setXYZ(0f, 1, -T).setNormal(0f, 1, -T).setUv(0, 0),
            new ModelVertex().setXYZ(T, 0, -1).setNormal(T, 0, -1).setUv(0, 0),
            new ModelVertex().setXYZ(T, 0, 1).setNormal(T, 0, 1).setUv(0, 0),
            new ModelVertex().setXYZ(-T, 0, -1).setNormal(-T, 0, -1).setUv(0, 0),
            new ModelVertex().setXYZ(-T, 0, 1).setNormal(-T, 0, 1).setUv(0, 0))
    );

    //Connect the corners of these planes into the most basic 20 sided IcoSphere
    private static final short[] UNIT_INDICES = new short[]{
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

    public static Model.Part getUnitFace(int i) {
        return new Model.Part(
                new ModelMeshPart(
                        new ModelVertex[]{
                                UNIT_ICO_VERTICES.get(UNIT_INDICES[i * 3]),
                                UNIT_ICO_VERTICES.get(UNIT_INDICES[i * 3 + 1]),
                                UNIT_ICO_VERTICES.get(UNIT_INDICES[i * 3 + 2])
                        },
                        new short[] { 0, 1, 2 }
                )
        );
    }

    public static Model.Part getDividedFace(int faceID, int divisions) {

        Model.Part existingFace = getUnitFace(faceID);

        // get the 3 corners of the triangle
        final Vector3f v1 = new Vector3f(((ModelMeshPart)existingFace.meshPart).getVertex(0).getXYZ()); // top
        final Vector3f v2 = new Vector3f(((ModelMeshPart)existingFace.meshPart).getVertex(1).getXYZ()); // bottom left
        final Vector3f v3 = new Vector3f(((ModelMeshPart)existingFace.meshPart).getVertex(2).getXYZ()); // bottom right

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
        for (Vector3f vec: newVertexLocations) {
            vec.normalize();
        }

        //Get indices for the faces of the triangle
        short numUp = (short)getPointUpTrisForDivisions(divisions);
        short numDown = (short)getPointDownTrisForDivisions(divisions);
        short[] newIndices = new short[(numUp * 3) + (numDown * 3)];

        //Get indices for triangles that point up
        for (short i = 0; i < numUp; i++) {
            newIndices[i * 3] = i;
            newIndices[i * 3 + 1] = (short)(i + (getCurrentRowIndex(i) + 1));
            newIndices[i * 3 + 2] = (short)(newIndices[i * 3 + 1] + 1);
        }

        //Get indices for triangles that point down
        for (short i = 0; i < numDown; i++) {
            newIndices[i * 3 + (numUp * 3)] = (short)(i + (getCurrentRowIndex(i) + 1) + 1);
            newIndices[i * 3 + 1 + (numUp * 3)] = (short)(newIndices[i * 3 + (numUp * 3)] - 1);
            newIndices[i * 3 + 2 + (numUp * 3)] = (short)(newIndices[i * 3 + (numUp * 3)] + (getCurrentRowIndex(i) + 1) + 1);
        }

        //Combine vertex data with indices to create the ModelVertexes
        ModelVertex[] newVertices = new ModelVertex[newVertexLocations.size()];
        for (int i = 0; i < newVertexLocations.size(); i++) {
            Vector3f cL = newVertexLocations.get(i);
            newVertices[i] = new ModelVertex().setXYZ(cL.x(), cL.y(), cL.z()).setNormal(cL.x(), cL.y(), cL.z()).setUv(0, 0);
        }

        return new Model.Part(new ModelMeshPart(newVertices, newIndices));
    }

    /**
     * @param i the current index of the triangle vertex to test
     * @return the row starting from 0 the the current vertex is positioned in
     */
    public static short getCurrentRowIndex(int i) {
        return (short)Math.floor((-1 + Math.sqrt(1 + 8*i)) / 2);
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