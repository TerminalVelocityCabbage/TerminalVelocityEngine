package com.terminalvelocitycabbage.engine.profiling;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.opengl.ARBTimerQuery.GL_TIME_ELAPSED;
import static org.lwjgl.opengl.ARBTimerQuery.glGetQueryObjectui64v;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * A timer for the GPU, create an instance of this timer
 */
public class GPUTimer {

    private static final int GPU_QUERY_COUNT = 5;

    boolean supported;
    int cur;
    int ret;
    IntBuffer queries = BufferUtils.createIntBuffer(GPU_QUERY_COUNT);

    static final FloatBuffer gpuTimes = BufferUtils.createFloatBuffer(3);

    int currentNumTimes;

    public GPUTimer() {
        //memset(timer, 0, sizeof(*timer));
        supported = GL.getCapabilities().GL_ARB_timer_query;
        cur = 0;
        ret = 0;
        BufferUtils.zeroBuffer(queries);

        if (supported) {
            glGenQueries(queries);
        }
    }

    /**
     * starts this sample period
     */
    public void startGPUTimer() {
        if (!supported) {
            return;
        }
        glBeginQuery(GL_TIME_ELAPSED, queries.get(cur % GPU_QUERY_COUNT));
        cur++;
    }

    /**
     * stops this sample period
     *
     * @param maxTimes the maximum number of time samples to return from this method call
     * @return the number of times added to the current times
     */
    public int stopGPUTimer(int maxTimes) {
        currentNumTimes = 0;
        if (!supported) {
            return currentNumTimes;
        }

        glEndQuery(GL_TIME_ELAPSED);

        try (MemoryStack stack = stackPush()) {
            IntBuffer available = stack.ints(1);
            while (available.get(0) != 0 && ret <= cur) {
                // check for results if there are any
                glGetQueryObjectiv(queries.get(ret % GPU_QUERY_COUNT), GL_QUERY_RESULT_AVAILABLE, available);
                if (available.get(0) != 0) {
                    LongBuffer timeElapsed = stack.mallocLong(1);
                    glGetQueryObjectui64v(queries.get(ret % GPU_QUERY_COUNT), GL_QUERY_RESULT, timeElapsed);
                    ret++;
                    if (currentNumTimes < maxTimes) {
                        gpuTimes.put(currentNumTimes, (float)(timeElapsed.get(0) * 1e-9));
                        currentNumTimes++;
                    }
                }
            }
        }
        return currentNumTimes;
    }

    /**
     * @return the current array of times since the last stopTimer call
     */
    public int getCurrentTimes() {
        return currentNumTimes;
    }

    /**
     * @param time the index for which you wish to retrieve a time for
     * @return the time at that requested index
     */
    public float getTime(int time) {
        return gpuTimes.get(time);
    }

    /**
     * @return a boolean weather or not this GPU supports timing in this implementation
     */
    public boolean isSupported() {
        return supported;
    }
}
