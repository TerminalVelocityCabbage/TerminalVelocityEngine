package com.terminalvelocitycabbage.engine.client.renderer.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class PNGDecoder2 {

    int width;
    int height;

    ByteBuffer imageBuffer;

    public PNGDecoder2(InputStream inputStream) throws IOException {
        imageBuffer = allocate(inputStream);
    }

    public ByteBuffer decode() {
        ByteBuffer image;

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer wBuffer = stack.mallocInt(1);
            IntBuffer hBuffer = stack.mallocInt(1);
            IntBuffer component = stack.mallocInt(1);

            assert imageBuffer != null;
            if(!STBImage.stbi_info_from_memory(imageBuffer, wBuffer, hBuffer, component)) {
                throw new RuntimeException("Failed to read image information: " + STBImage.stbi_failure_reason());
            } else {
                System.out.println("Okay with reason: " + STBImage.stbi_failure_reason());
            }

            image = STBImage.stbi_load_from_memory(imageBuffer, wBuffer, hBuffer, component, 4);

            width = wBuffer.get(0);
            height = hBuffer.get(0);

            if(image == null) {
                throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
            }

            return image;
        }
    }

    private static ByteBuffer allocate(InputStream source) throws IOException {

        ByteBuffer buffer = null;

        try (ReadableByteChannel rbc = Channels.newChannel(source)) {
            buffer = BufferUtils.createByteBuffer(8096);

            while(true) {
                int bytes = rbc.read(buffer);

                if(bytes == -1) {
                    break;
                }

                if(buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        buffer.flip();

        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);

        buffer.flip();
        newBuffer.put(buffer);

        return newBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
