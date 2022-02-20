package com.terminalvelocitycabbage.engine.client.renderer.shader;

import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glBufferSubData;
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

    private int bufferID;
    private Buffer buffer;

    private UniformBufferObject() {

    }

    public UniformBufferObject createFloatArrayUBO(float[] data) {

        //Create UniformBufferObject to store data in
        UniformBufferObject ubo = new UniformBufferObject();

        //Create Data Allocation
        //Floats are 4 bytes
        int size = data.length * 4;
        ubo.buffer = BufferUtils.createFloatBuffer(size).put(data).flip();

        return ubo;
    }

    public void bindUBO() {

    }

    public void setBufferData(int offset, Buffer data) {

        bindUBO();

        if (data instanceof FloatBuffer) {
            glBufferSubData(GL_UNIFORM_BUFFER, offset, (FloatBuffer)data);
        }
        else {
            glBufferSubData(GL_UNIFORM_BUFFER, offset, (IntBuffer)data);
        }
    }

    public void bindToBindingPoint(int bindingPoint) {

    }

    public int getBufferID() {
        return bufferID;
    }

    public void destroy() {

    }

}
