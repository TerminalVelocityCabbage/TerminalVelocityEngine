package com.terminalvelocitycabbage.engine.client.resources;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class UrlResource implements Resource {

	private final Identifier identifier;
	private final URL url;
	private boolean invalid;

	protected UrlResource(Identifier identifier, URL url) {
		this.identifier = identifier;
		this.url = url;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public InputStream openStream() throws IOException {
		if (invalid) {
			Log.crash("Resource Loading Error", new RuntimeException("Invalid Resource for url: " + url.toString()));
		}
		return url.openStream();
	}

	@Override
	public Optional<DataInputStream> asDataStream() {
		if (invalid) {
			Log.crash("Resource Loading Error", new RuntimeException("Invalid Resource for url: " + url.toString()));
		}
		try {
			return Optional.of(new DataInputStream(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public Optional<String> asString() {
		Optional<String> out = Optional.empty();
		try {
			out = Optional.of(IOUtils.toString(openStream(), StandardCharsets.UTF_8.name()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public Optional<java.nio.ByteBuffer> asByteBuffer() {

		java.nio.ByteBuffer buffer = null;
		Path path = Paths.get(url.getPath().replaceFirst("/", ""));

		if(Files.isReadable(path)) {
			try(SeekableByteChannel sbc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int)sbc.size() + 1);
				while(sbc.read(buffer) != -1);
			} catch(IOException e) {}
		} else {
			try(InputStream source = openStream(); ReadableByteChannel rbc = Channels.newChannel(source)) {
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
		}

		buffer.flip();

		return Optional.of(buffer);
	}

	private static java.nio.ByteBuffer resizeBuffer(java.nio.ByteBuffer buffer, int newCapacity) {
		java.nio.ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);

		buffer.flip();
		newBuffer.put(buffer);

		return newBuffer;
	}

	public boolean isInvalid() {
		return invalid;
	}
}
