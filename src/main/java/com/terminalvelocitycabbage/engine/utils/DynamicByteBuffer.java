package com.terminalvelocitycabbage.engine.utils;

import java.text.NumberFormat;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Dynamically growable ByteBuffer.
 */
public class DynamicByteBuffer {
    public long addr;
    public int pos, cap;
    private static final NumberFormat INT_FORMATTER = NumberFormat.getIntegerInstance();
    private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();

    /**
     * Allocate a ByteBuffer with the given initial capacity.
     */
    public DynamicByteBuffer(int initialCapacity) {
        addr = nmemAlloc(initialCapacity);
        cap = initialCapacity;
        //Log.debug("Creating new DynamicByteBuffer with capacity [" + INT_FORMATTER.format(cap / 1024) + " KB]");
    }

    private void grow() {
        int newCap = (int) (cap * 1.75f);
        //Log.debug("Growing DynamicByteBuffer from [" + INT_FORMATTER.format(cap / 1024) + " KB] to [" + INT_FORMATTER.format(newCap / 1024) + " KB]");
        long newAddr = nmemRealloc(addr, newCap);
        cap = newCap;
        addr = newAddr;
    }

    public void free() {
        //Log.debug("Freeing DynamicByteBuffer (used " + PERCENT_FORMATTER.format((float) pos / cap) + " of capacity)");
        nmemFree(addr);
    }

    public DynamicByteBuffer putInt(int v) {
        if (cap - pos < Integer.BYTES)
            grow();
        return putIntNoGrow(v);
    }

    private DynamicByteBuffer putIntNoGrow(int v) {
        memPutInt(addr + pos, v);
        pos += Integer.BYTES;
        return this;
    }

    public DynamicByteBuffer putShort(int v) {
        if (cap - pos < Short.BYTES)
            grow();
        return putShortNoGrow(v);
    }

    private DynamicByteBuffer putShortNoGrow(int v) {
        memPutShort(addr + pos, (short) v);
        pos += Short.BYTES;
        return this;
    }
}
