package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

	private int textureID;

	public final int width;
	public final int height;

	public Texture(ResourceManager resourceManager, Identifier identifier) {
		this(load(resourceManager, identifier));
	}

	public Texture(ByteBuffer imageBuffer) {
		ByteBuffer buffer;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			buffer = stbi_load_from_memory(imageBuffer, w, h, channels, 4);
			if (buffer == null) {
				throw new RuntimeException("Image file not loaded: " + stbi_failure_reason());
			}

			this.height = h.get();
			this.width = w.get();
		}

		this.textureID = createTexture(buffer);
		stbi_image_free(buffer);
	}

	private int createTexture(ByteBuffer buf) {

		//Create a new OpenGL texture
		int textureId = glGenTextures();
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, textureId);

		//Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		//Setup the UV/ST coordinate system
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);

		//Upload the texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		//Generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);

		return textureId;
	}

	private static ByteBuffer load(ResourceManager resourceManager, Identifier identifier) {
		ByteBuffer buf = null;
		try {
			// Open the PNG file as an InputStream
			InputStream in;
			Optional<Resource> file = resourceManager.getResource(identifier);
			if (file.isPresent()) {
				in = file.get().openStream();
			} else {
				throw new RuntimeException("Count not find resource " + identifier.toString());
			}
			byte[] bytes = in.readAllBytes();
			buf = ByteBuffer.wrap(bytes);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return buf;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void destroy() {
		glDeleteTextures(textureID);
	}

	public Material toMaterial() {
		return Material.builder().texture(this).build();
	}
}
