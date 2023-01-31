package com.terminalvelocitycabbage.engine.client.renderer.shader;

import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 *
 * std140 memory layout
 *
 * scalar type (bool, int, uint, float) - Takes size of basic scalar type (sizeof(GLfloat) or 4 bytes)
 * 2D vector type (bvec2, ivec2, uvec2, vec2) - Twice the size of basic scalar type, i.e. 8 bytes (no space wasted)
 * 3D vector type (bvec3, ivec3, uvec3, vec3) - Four times the size of basic scalar type, i.e. 16 bytes (one scalar wasted)
 * 4D vector type (bvec4, ivec4, uvec4, vec4) - Four times the size of basic scalar type, i.e. 16 bytes (no space wasted)
 * array - Size of one array element times length of the array, the whole thing padded as vec4 in the end
 * mat3 - Stored as an array of 3x 3D vectors, that means we have 9 scalars and whole thing is padded as if we had 3x vec4 (3 scalars wasted)
 * mat4 - Stored as an array of 4x 4D vectors, that means we have 16 scalars and whole thing fits just perfectly (no space wasted)

 */

public class UniformBufferObject {

    private int bufferBindingSlot;
    private int bufferID;
    private Buffer buffer;

    private UniformBufferObject(int bindingSlot) {
        bufferID = glGenBuffers();
        bufferBindingSlot = bindingSlot;
    }

    /**
     * @param data the data to be inserted into the ubo
     * @param usage one of GL_(freq of access)_(nature of access)
     *            Frequency
     *              STREAM  - The data store contents will be modified once and used at most a few times.
     *              STATIC  - The data store contents will be modified once and used many times.
     *              DYNAMIC - The data store contents will be modified repeatedly and used many times.
     *            Nature
     *              DRAW    - The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.
     *              READ    - The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.
     *              COPY    - The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.
     * @return
     */
    public static UniformBufferObject createFloatArrayUBO(int bindingSlot, float[] data, int usage) {

        //Create UniformBufferObject to store data in
        UniformBufferObject ubo = new UniformBufferObject(bindingSlot);

        ubo.buffer = std140izeFloatArrayToBuffer(data);

        //Bind to the current buffer
        glBindBuffer(GL_UNIFORM_BUFFER, ubo.bufferID);

        //Allocate buffer space with opengl
        glBufferData(GL_UNIFORM_BUFFER, data.length * 16L, usage);

        //Bind the buffer to the desired binding slot target
        glBindBufferBase(GL_UNIFORM_BUFFER, ubo.bufferBindingSlot, ubo.bufferID);
        //Below is equivalent to above, but since we're not combining buffers at this time we will just use base
        //glBindBufferRange(GL_UNIFORM_BUFFER, bufferSlot, bufferID, 0, size);

        //Set the initial data of the buffer
        ubo.updateBufferData(0, ubo.buffer);

        return ubo;
    }

    public static FloatBuffer std140izeFloatArrayToBuffer(float[] data) {
        //Create Data Allocation
        float[] std140data = new float[data.length * 4];
        for (int i = 0; i < data.length; i++) {
            std140data[i * 4] = data[i];
            std140data[i * 4 + 1] = 0;
            std140data[i * 4 + 2] = 0;
            std140data[i * 4 + 3] = 0;
        }
        return BufferUtils.createFloatBuffer(std140data.length).put(std140data).flip();
    }

    //TODO double or triple buffering options so that we don't stall out the rendering pipeline by waiting for opengl to finish reading the data issued to the previous frame
    public void updateBufferData(int offset, Buffer data) {

        //The Offset is basically where you want to start setting the data

        //Bind to the current buffer
        glBindBuffer(GL_UNIFORM_BUFFER, bufferID);

        buffer = data;

        if (data instanceof FloatBuffer) {
            glBufferSubData(GL_UNIFORM_BUFFER, offset, (FloatBuffer)data);
        }
        else {
            glBufferSubData(GL_UNIFORM_BUFFER, offset, (IntBuffer)data);
        }
    }

    public int getBufferID() {
        return bufferID;
    }

    public void destroy() {
        glDeleteBuffers(bufferID);
    }

}
