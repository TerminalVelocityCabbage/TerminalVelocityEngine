package com.terminalvelocitycabbage.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class BufferUtils {

    public static ByteBuffer inputStreamToByteBuffer(InputStream inputStream) {

        ByteBuffer buffer = null;

        try(ReadableByteChannel rbc = Channels.newChannel(inputStream)) {
            buffer = org.lwjgl.BufferUtils.createByteBuffer(8096);

            while(true) {
                int bytes = rbc.read(buffer);

                if(bytes == -1) {
                    break;
                }

                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        buffer.flip();

        return buffer;
    }

    private static java.nio.ByteBuffer resizeBuffer(java.nio.ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = org.lwjgl.BufferUtils.createByteBuffer(newCapacity);

        buffer.flip();
        newBuffer.put(buffer);

        return newBuffer;
    }

}
