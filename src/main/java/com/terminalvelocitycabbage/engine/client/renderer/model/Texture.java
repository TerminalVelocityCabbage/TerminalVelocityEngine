package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.client.util.PNGDecoder;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

	private final ResourceManager resourceManager;
	private final Identifier identifier;

	private int textureID;

	public int width;
	public int height;

	public Texture(ResourceManager resourceManager, Identifier identifier) {
		this.resourceManager = resourceManager;
		this.identifier = identifier;
	}

	public void bind(int texture) {
		textureID = loadPNGTexture(resourceManager, identifier, texture);
		glActiveTexture(texture);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void destroy() {
		glDeleteTextures(textureID);
	}

	private int loadPNGTexture(ResourceManager resourceManager, Identifier identifier, int textureUnit) {
		ByteBuffer buf = null;

		try {
			// Open the PNG file as an InputStream
			InputStream in;
			Optional<Resource> file = resourceManager.getResource(identifier);
			if (file.isPresent()) {
				in = file.get().openStream();
			} else {
				Log.error("Count not find resource " + identifier.toString());
				return -1;
			}
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			width = decoder.getWidth();
			height = decoder.getHeight();

			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(Float.BYTES * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * Float.BYTES, PNGDecoder.Format.RGBA);
			buf.flip();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = glGenTextures();
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);

		// Setup the UV/ST coordinate system
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

		return texId;
	}
}
