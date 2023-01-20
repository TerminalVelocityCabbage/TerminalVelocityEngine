package com.terminalvelocitycabbage.engine.client.renderer.util;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.opengl.GL11C.glGetInteger;
import static org.lwjgl.opengl.GL31C.GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT;

public class Capabilities {

    private GLCapabilities capabilities;

    private boolean useMultiDrawIndirect;
    private boolean useBufferStorage;
    private boolean useClearBuffer;
    private boolean drawPointsWithGS;
    private boolean useInverseDepth;
    private boolean useNvMultisampleCoverage;
    private boolean canGenerateDrawCallsViaShader;
    private boolean useOcclusionCulling;
    private boolean useTemporalCoherenceOcclusionCulling;
    private boolean canSourceIndirectDrawCallCountFromBuffer;
    private boolean useRepresentativeFragmentTest;
    private boolean canUseSynchronousDebugCallback;
    /* Other queried OpenGL state/configuration */
    private int uniformBufferOffsetAlignment;

    /**
     * Query all (optional) capabilites/extensions that we want to use from the OpenGL context via
     * LWJGL's {@link GLCapabilities}.
     */
    public void determineOpenGLCapabilities() {

        capabilities = GL.createCapabilities();

        useMultiDrawIndirect = capabilities.GL_ARB_multi_draw_indirect || capabilities.OpenGL43;
        useBufferStorage = capabilities.GL_ARB_buffer_storage || capabilities.OpenGL44;
        useClearBuffer = capabilities.GL_ARB_clear_buffer_object || capabilities.OpenGL43;
        drawPointsWithGS = useMultiDrawIndirect; // <- we just haven't implemented point/GS rendering without MDI yet
        useInverseDepth = capabilities.GL_ARB_clip_control || capabilities.OpenGL45;
        useNvMultisampleCoverage = capabilities.GL_NV_framebuffer_multisample_coverage;
        canUseSynchronousDebugCallback = capabilities.GL_ARB_debug_output || capabilities.OpenGL43;
        canGenerateDrawCallsViaShader = capabilities.GL_ARB_shader_image_load_store/* 4.2 */ && capabilities.GL_ARB_shader_storage_buffer_object/* 4.3 */
                && capabilities.GL_ARB_shader_atomic_counters/* 4.2 */ || capabilities.OpenGL43;
        useOcclusionCulling = canGenerateDrawCallsViaShader && useMultiDrawIndirect;
        useTemporalCoherenceOcclusionCulling = useOcclusionCulling;
        canSourceIndirectDrawCallCountFromBuffer = canGenerateDrawCallsViaShader && (capabilities.GL_ARB_indirect_parameters || capabilities.OpenGL46);
        useRepresentativeFragmentTest = capabilities.GL_NV_representative_fragment_test;

        /* Query the necessary UBO alignment which we need for multi-buffering */
        uniformBufferOffsetAlignment = glGetInteger(GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT);
    }

    private void print() {
        System.out.println("useMultiDrawIndirect: " + useMultiDrawIndirect);
        System.out.println("useBufferStorage: " + useBufferStorage);
        System.out.println("drawPointsWithGS: " + drawPointsWithGS);
        System.out.println("useInverseDepth: " + useInverseDepth);
        System.out.println("useNvMultisampleCoverage: " + useNvMultisampleCoverage);
        System.out.println("canUseSynchronousDebugCallback: " + canUseSynchronousDebugCallback);
        System.out.println("canGenerateDrawCallsViaShader: " + canGenerateDrawCallsViaShader);
        System.out.println("useOcclusionCulling: " + useOcclusionCulling);
        System.out.println("useTemporalCoherenceOcclusionCulling: " + useTemporalCoherenceOcclusionCulling);
        System.out.println("canSourceIndirectDrawCallCountFromBuffer: " + canSourceIndirectDrawCallCountFromBuffer);
        System.out.println("useRepresentativeFragmentTest: " + useRepresentativeFragmentTest);
        System.out.println("uniformBufferOffsetAlignment: " + uniformBufferOffsetAlignment);
    }

    public GLCapabilities getCapabilities() {
        return capabilities;
    }

    public boolean useMultiDrawIndirect() {
        return useMultiDrawIndirect;
    }

    public boolean useBufferStorage() {
        return useBufferStorage;
    }

    public boolean useClearBuffer() {
        return useClearBuffer;
    }

    public boolean drawPointsWithGS() {
        return drawPointsWithGS;
    }

    public boolean useInverseDepth() {
        return useInverseDepth;
    }

    public boolean useNvMultisampleCoverage() {
        return useNvMultisampleCoverage;
    }

    public boolean canGenerateDrawCallsViaShader() {
        return canGenerateDrawCallsViaShader;
    }

    public boolean useOcclusionCulling() {
        return useOcclusionCulling;
    }

    public boolean useTemporalCoherenceOcclusionCulling() {
        return useTemporalCoherenceOcclusionCulling;
    }

    public boolean canSourceIndirectDrawCallCountFromBuffer() {
        return canSourceIndirectDrawCallCountFromBuffer;
    }

    public boolean useRepresentativeFragmentTest() {
        return useRepresentativeFragmentTest;
    }

    public boolean canUseSynchronousDebugCallback() {
        return canUseSynchronousDebugCallback;
    }

    public int getUniformBufferOffsetAlignment() {
        return uniformBufferOffsetAlignment;
    }
}
