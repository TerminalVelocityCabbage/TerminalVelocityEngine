package com.terminalvelocitycabbage.engine.prefabs.marchingcubes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.prefabs.gameobjects.BoxLine;
import com.terminalvelocitycabbage.engine.scheduler.TaskBuilder;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;
import java.util.function.Function;

import static com.terminalvelocitycabbage.engine.Engine.ID;

public class Chunk {

    public static final int VOXELS_PER_CHUNK_AXIS = 20;
    public static final float SIZE_PER_VOXEL_AXIS = 1; //This is basically just like a scale.
    public static final float SIZE_PER_CHUNK = SIZE_PER_VOXEL_AXIS * VOXELS_PER_CHUNK_AXIS;

    private final int chunkX, chunkY, chunkZ;

    //+1 as is inclusive, +1+1 as we want to have a view either side
    private final float[][][] data = new float[VOXELS_PER_CHUNK_AXIS+3][VOXELS_PER_CHUNK_AXIS+3][VOXELS_PER_CHUNK_AXIS+3];

    private final Model chunkModel;
    private final BoxLine boundary;

    private final ColourGetter colourGetter;
    private boolean generated;

    public Chunk(int x, int y, int z, ColourGetter colourGetter) {
        this.chunkX = x;
        this.chunkY = y;
        this.chunkZ = z;

        this.colourGetter = colourGetter;

        this.chunkModel = new Model(RenderFormat.POSITION_UV_NORMAL_COLOUR, new RenderMode(RenderMode.Modes.TRIANGLES), new ArrayList<>());
        this.chunkModel.setMaterial(Material.builder().build());
        this.chunkModel.bind();

        this.boundary = new BoxLine(new Vector3f(x * SIZE_PER_CHUNK, y * SIZE_PER_CHUNK, z * SIZE_PER_CHUNK), SIZE_PER_CHUNK, SIZE_PER_CHUNK, SIZE_PER_CHUNK, new Vector4f(1, 0, 0, 1));

        this.generated = false;
    }

    public void initializeChunkWith(ChunkInitializer initializer) {
        for (int x = -1; x <= VOXELS_PER_CHUNK_AXIS+1; x++) {
            float[][] yz = this.data[x+1];
            for (int y = -1; y <= VOXELS_PER_CHUNK_AXIS+1; y++) {
                float[] arr = yz[y+1];
                for (int z = -1; z <= VOXELS_PER_CHUNK_AXIS+1; z++) {
                    arr[z+1] = initializer.getValueAt(
                            this.chunkX*SIZE_PER_CHUNK + x*SIZE_PER_VOXEL_AXIS,
                            this.chunkY*SIZE_PER_CHUNK + y*SIZE_PER_VOXEL_AXIS,
                            this.chunkZ*SIZE_PER_CHUNK + z*SIZE_PER_VOXEL_AXIS
                    );
                }
            }
        }
        this.marchCubes();
    }

    public void marchCubes() {

        var bindChunkTask = TaskBuilder.builder()
                .identifier(new Identifier(ID, "bind-chunk-[" + chunkX + "]-[" + chunkY + "]-[" + chunkZ + "]"))
                .executes((taskContext) -> bindChunk((MeshPart) taskContext.previous()))
                .build();

        var initChunkTask = TaskBuilder.builder()
                .identifier(new Identifier(ID, "init-chunk-[" + chunkX + "]-[" + chunkY + "]-[" + chunkZ + "]"))
                .async()
                .executes((taskContext) -> taskContext.setReturn(this.generateMarchingMesh()))
                .then(bindChunkTask)
                .build();

        ClientBase.getScheduler().scheduleTask(initChunkTask);
//        this.chunkModel.mesh.dumpAsObj();
    }

    private void bindChunk(MeshPart mesh) {
        this.chunkModel.modelParts.clear();
        this.chunkModel.modelParts.add(new Model.Part(mesh));
        this.chunkModel.resizeBuffer();
        this.chunkModel.onPartsChange();
        this.chunkModel.update(new Vector3f(this.chunkX, this.chunkY, this.chunkZ).mul(SIZE_PER_CHUNK), new Quaternionf(), new Vector3f(1));
        boundary.bind();
        boundary.queueAndUpdate();
        this.generated = true;
    }

    public Material getMaterial() {
        return this.chunkModel.getMaterial();
    }

    public void render() {
        if (generated) {
            this.chunkModel.render();
        }
    }

    public BoxLine getBoundary() {
        return boundary;
    }

    public void destroy() {
        this.chunkModel.destroy();
    }

    private MeshPart generateMarchingMesh() {
        //We need to make it Integer, instead of int, as we want un-added entries to be null, not 0.
        int size = (VOXELS_PER_CHUNK_AXIS+3)*(VOXELS_PER_CHUNK_AXIS+3)*(VOXELS_PER_CHUNK_AXIS+3);
        Integer[] xEdgeCache = new Integer[size];
        Integer[] yEdgeCache = new Integer[size];
        Integer[] zEdgeCache = new Integer[size];

        MeshBuilder builder = new MeshBuilder();

        Vector3f position = new Vector3f();
        Vector3f modifiedPosA = new Vector3f();
        Vector3f modifiedPosB = new Vector3f();

        final float s = SIZE_PER_VOXEL_AXIS;

        int[] vertList = new int[12];

        for (int z = 0; z < VOXELS_PER_CHUNK_AXIS+2; z++) {
            for (int y = 0; y < VOXELS_PER_CHUNK_AXIS+2; y++) {
                for (int x = 0; x < VOXELS_PER_CHUNK_AXIS+2; x++) {
                    boolean isDummy = x == 0 || y == 0 || z == 0 || x == VOXELS_PER_CHUNK_AXIS+1 || y == VOXELS_PER_CHUNK_AXIS+1 || z == VOXELS_PER_CHUNK_AXIS+1;
                    //                   _________6_________x1/y1/z1
                    //                  /|                 /|
                    //                 7 |                5 |
                    //                /  11              /  |
                    //            y1 /___|____4_________/   10
                    //              |    |              |   |
                    //              |    |              |   |
                    //              |    |________2_____|___| z1
                    //              8   /               9   /
                    //              |  3                |  1
                    //              | /                 | /
                    //              |/________0_________|/
                    //          x0/y0/z0                x1

                    //With the value points being:
                    //                7  ___________________  6
                    //                  /|                 /|
                    //                 / |                / |
                    //                /  |               /  |
                    //           4   /___|______________/ 5 |
                    //              |    |              |   |
                    //              |    |              |   |
                    //              |  3 |______________|___| 2
                    //              |   /               |   /
                    //              |  /                |  /
                    //              | /                 | /
                    //              |/__________________|/
                    //             0                     1


                    //              y      z
                    //              |     /
                    //              |    /
                    //              |   /
                    //              |  /
                    //              | /
                    //              |/__________________ x
                    //
                    //To cache and re-use the indices, I use x/y/zEdgeCache, which essentially constructs a grid of these 3 line axis, each the size of a voxel
                    //Then, to get an edge, I just get the location of it's corner.

                    //All the edges on the yz plane (x edges)
                    int nextAxis = VOXELS_PER_CHUNK_AXIS+2;
                    int e = x + (y * nextAxis) + (z * nextAxis*nextAxis); //current
                    int ex = e + 1; //next X
                    int ey = e + nextAxis; //next Y
                    int ez = e + nextAxis*nextAxis; //next Z

                    int exz = ez+1; //Vertex 2 -> 6, the next X and next Z
                    int exy = ey+1; //Vertex 5 -> 6, the next X and next Y
                    int eyz = ez+nextAxis; //Vertex 7 -> 6, the next Y and next X


                    position.set(
                            x*SIZE_PER_VOXEL_AXIS-1, y*SIZE_PER_VOXEL_AXIS-1, z*SIZE_PER_VOXEL_AXIS-1
                    );

                    float value0 = this.data[x][y][z];
                    float value1 = this.data[x+1][y][z];
                    float value2 = this.data[x+1][y][z+1];
                    float value3 = this.data[x][y][z+1];
                    float value4 = this.data[x][y+1][z];
                    float value5 = this.data[x+1][y+1][z];
                    float value6 = this.data[x+1][y+1][z+1];
                    float value7 = this.data[x][y+1][z+1];

                    int cubeIndex = 0;
                    if (value0 > 0) cubeIndex |= 1;
                    if (value1 > 0) cubeIndex |= 2;
                    if (value2 > 0) cubeIndex |= 4;
                    if (value3 > 0) cubeIndex |= 8;
                    if (value4 > 0) cubeIndex |= 16;
                    if (value5 > 0) cubeIndex |= 32;
                    if (value6 > 0) cubeIndex |= 64;
                    if (value7 > 0) cubeIndex |= 128;

                    int bits = MarchingCubesTables.EDGE_TABLE[cubeIndex];
                    if(bits == 0) {
                        continue;
                    }

                    float mu;
                    if ((bits & 1) != 0) {
                        //0 to 1
                        mu = (-value0) / (value1 - value0);
                        vertList[0] = xEdgeCache[e] = builder.pushAndReturnIndex(isDummy, xEdgeCache[e], position.add(0, 0, 0, modifiedPosA), position.add(s, 0, 0, modifiedPosB), mu);
                    }
                    if ((bits & 2) != 0) {
                        //1 to 2
                        mu = (-value1) / (value2 - value1);
                        vertList[1] = zEdgeCache[ex] = builder.pushAndReturnIndex(isDummy, zEdgeCache[ex], position.add(s, 0, 0, modifiedPosA), position.add(s, 0, s, modifiedPosB), mu);
                    }
                    if ((bits & 4) != 0) {
                        //3 to 2
                        mu = (-value3) / (value2 - value3);
                        vertList[2] = xEdgeCache[ez] = builder.pushAndReturnIndex(isDummy, xEdgeCache[ez], position.add(0, 0, s, modifiedPosA), position.add(s, 0, s, modifiedPosB), mu);
                    }
                    if ((bits & 8) != 0) {
                        //0 to 3
                        mu = (-value0) / (value3 - value0);
                        vertList[3] = zEdgeCache[e] = builder.pushAndReturnIndex(isDummy, zEdgeCache[e], position.add(0, 0, 0, modifiedPosA), position.add(0, 0, s, modifiedPosB), mu);
                    }
                    // top of the cube
                    if ((bits & 16) != 0) {
                        //4 to 5
                        mu = (-value4) / (value5 - value4);
                        vertList[4] = xEdgeCache[ey] = builder.pushAndReturnIndex(isDummy, xEdgeCache[ey], position.add(0, s, 0, modifiedPosA), position.add(s, s, 0, modifiedPosB), mu);
                    }
                    if ((bits & 32) != 0) {
                        //5 to 6
                        mu = (-value5) / (value6 - value5);
                        vertList[5] = zEdgeCache[exy] = builder.pushAndReturnIndex(isDummy, zEdgeCache[exy], position.add(s, s, 0, modifiedPosA), position.add(s, s, s, modifiedPosB), mu);
                    }
                    if ((bits & 64) != 0) {
                        //7 to 6
                        mu = (-value7) / (value6 - value7);
                        vertList[6] = xEdgeCache[eyz] = builder.pushAndReturnIndex(isDummy, xEdgeCache[eyz], position.add(0, s, s, modifiedPosA), position.add(s, s, s, modifiedPosB), mu);
                    }
                    if ((bits & 128) != 0) {
                        //4 to 7
                        mu = (-value4) / (value7 - value4);
                        vertList[7] = zEdgeCache[ey] = builder.pushAndReturnIndex(isDummy, zEdgeCache[ey], position.add(0, s, 0, modifiedPosA), position.add(0, s, s, modifiedPosB), mu);
                    }
                    // vertical lines of the cube
                    if ((bits & 256) != 0) {
                        //0 to 4
                        mu = (-value0) / (value4 - value0);
                        vertList[8] = yEdgeCache[e] = builder.pushAndReturnIndex(isDummy, yEdgeCache[e], position.add(0, 0, 0, modifiedPosA), position.add(0, s, 0, modifiedPosB), mu);
                    }
                    if ((bits & 512) != 0) {
                        //1 to 5
                        mu = (-value1) / (value5 - value1);
                        vertList[9] = yEdgeCache[ex] = builder.pushAndReturnIndex(isDummy, yEdgeCache[ex], position.add(s, 0, 0, modifiedPosA), position.add(s, s, 0, modifiedPosB), mu);
                    }
                    if ((bits & 1024) != 0) {
                        //2 to 6
                        mu = (-value2) / (value6 - value2);
                        vertList[10] = yEdgeCache[exz] = builder.pushAndReturnIndex(isDummy, yEdgeCache[exz], position.add(s, 0, s, modifiedPosA), position.add(s, s, s, modifiedPosB), mu);
                    }
                    if ((bits & 2048) != 0) {
                        //3 to 6
                        mu = (-value3) / (value7 - value3);
                        vertList[11] = yEdgeCache[ez] = builder.pushAndReturnIndex(isDummy, yEdgeCache[ez], position.add(0, 0, s, modifiedPosA), position.add(0, s, s, modifiedPosB), mu);
                    }

                    // "Re-purpose cubeindex into an offset into triTable."
                    cubeIndex <<= 4;

                    for(int i = cubeIndex; MarchingCubesTables.TRI_TABLE[i] != -1; i+=3) {
                        int index1 = MarchingCubesTables.TRI_TABLE[i];
                        int index2 = MarchingCubesTables.TRI_TABLE[i+1];
                        int index3 = MarchingCubesTables.TRI_TABLE[i+2];
                        builder.createTriangle(isDummy, vertList[index1], vertList[index2], vertList[index3]);
                    }
                }
            }
        }

        return builder.build();
    }


    private static final Function<Integer, List<int[]>> CREATOR = integer -> new ArrayList<>();

    private class MeshBuilder {

        //Vertex Index: List<{trianglev1, trianglev2, trianglev3}>
        private final Map<Integer, List<int[]>> normalComputeMap = new HashMap<>();

        private final Set<Integer> dummyTriangles = new HashSet<>();
        private final Set<Integer> dummyVertex = new HashSet<>();

        private final List<Vertex> vertices = new ArrayList<>();
        private final List<Integer> indices = new ArrayList<>();

        private final float[] colours = new float[3];

        public void createTriangle(boolean dummy, int index1, int index2, int index3) {
            if(dummy) {
                this.dummyTriangles.add(this.indices.size());
            }
            this.indices.add(index1);
            this.indices.add(index2);
            this.indices.add(index3);

            int[] aint = new int[] { index1, index2, index3 };
            this.normalComputeMap.computeIfAbsent(index1, CREATOR).add(aint);
            this.normalComputeMap.computeIfAbsent(index2, CREATOR).add(aint);
            this.normalComputeMap.computeIfAbsent(index3, CREATOR).add(aint);
        }

        public int pushAndReturnIndex(boolean isDummy, Integer cachedValue, Vector3f from, Vector3f to, float t) {
            if(cachedValue != null) {
                //If we have a legitimate vertex, we need to make sure it's not removed
                if(!isDummy && this.dummyVertex.contains(cachedValue)) {
                    this.dummyVertex.remove(cachedValue);
                }
                return cachedValue;
            }

            float x = Math.fma(to.x() - from.x, t, from.x);
            float y = Math.fma(to.y() - from.y, t, from.y);
            float z = Math.fma(to.z() - from.z, t, from.z);

            colourGetter.fillColours(colours, chunkX*SIZE_PER_CHUNK+x, chunkY*SIZE_PER_CHUNK+y, chunkZ*SIZE_PER_CHUNK+z);

            Vertex vertex = Vertex.positionUvNormalColour(
                    x, y, z,
                    0, 0,
                    0, 0, 0,
                    colours[0], colours[1], colours[2], 1
            );
            vertex.setFormat(RenderFormat.POSITION_UV_NORMAL_COLOUR);
            int v = this.vertices.size();
            this.vertices.add(vertex);

            if(isDummy) {
                this.dummyVertex.add(v);
            }

            return v;
        }


        private void calculateNormals() {
            Vector3f normal = new Vector3f();

            Vector3f vec1 = new Vector3f();
            Vector3f vec2 = new Vector3f();
            Vector3f vec3 = new Vector3f();

            for (Integer i : this.normalComputeMap.keySet()) {
                normal.set(0);
                List<int[]> list = this.normalComputeMap.get(i);
                for (int[] ints : list) {
                    vec1.set(this.vertices.get(ints[0]).getXYZ());
                    vec2.set(this.vertices.get(ints[1]).getXYZ());
                    vec3.set(this.vertices.get(ints[2]).getXYZ());

                    vec3.sub(vec1);
                    vec2.sub(vec1);

                    Vector3f normalize = vec3.cross(vec2).normalize();
                    normal.add(normalize);
                }
                this.vertices.get(i).setNormalXYZ(normal.x / list.size(), normal.y / list.size(), normal.z / list.size());
            }
        }

        private void removeDummys() {
            //We need to go in reverse order
            ArrayList<Integer> integers = new ArrayList<>(this.dummyTriangles);
            integers.sort(Comparator.reverseOrder());
            for (Integer v : integers) {
                this.indices.remove(v.intValue());
                this.indices.remove(v.intValue());
                this.indices.remove(v.intValue());
            }

            //We need to decrement and indices that appeared after the removed vertices.
            integers = new ArrayList<>(this.dummyVertex);
            integers.sort(Comparator.reverseOrder());
            for (Integer v : integers) {
                this.vertices.remove(v.intValue());
                for (int i = 0; i < this.indices.size(); i++) {
                    if(this.indices.get(i) >= v) {
                        this.indices.set(i, this.indices.get(i) - 1);
                    }
                }
            }
        }

        public MeshPart build() {
            this.calculateNormals();
            this.removeDummys();
            int[] arr = new int[this.indices.size()];
            for (int i = 0; i < this.indices.size(); i++) {
                arr[i] = this.indices.get(i);
            }
            return new MeshPart(
                    this.vertices.toArray(new Vertex[0]),
                    arr
            );
        }
    }

    public interface ChunkInitializer {
        float getValueAt(float x, float y, float z);
    }

    public interface ColourGetter {
        void fillColours(float[] colours, float x, float y, float z);
    }

}