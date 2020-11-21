package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.client.util.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

	private int textureID;

	public int width;
	public int height;

	public Texture(ResourceManager resourceManager, Identifier identifier) {
		ByteBuffer buf = this.load(resourceManager, identifier);
		this.createTexture(buf);
	}

	private void createTexture(ByteBuffer buf) {
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		//Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		//Setup the UV/ST coordinate system
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		//Upload the texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		//Generate Mip Map
		glGenerateMipmap(GL_TEXTURE_2D);
	}

	private ByteBuffer load(ResourceManager resourceManager, Identifier identifier) {
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
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			this.width = decoder.getWidth();
			this.height = decoder.getHeight();

			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(Float.BYTES * width * height);
			decoder.decode(buf, width * Float.BYTES, PNGDecoder.Format.RGBA);
			buf.flip();
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
