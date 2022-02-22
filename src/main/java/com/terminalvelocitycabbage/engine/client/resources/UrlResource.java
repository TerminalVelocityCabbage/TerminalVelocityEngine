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

	protected UrlResource(Identifier identifier, URL url) {
		this.identifier = identifier;
		this.url = url;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public InputStream openStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
			Log.crash("Resource Loading Error could not open Stream", new RuntimeException(e));
			return null;
		}
	}

	@Override
	public Optional<DataInputStream> asDataStream() {
		Optional<DataInputStream> out = Optional.empty();
		try {
			out = Optional.of(new DataInputStream(url.openStream()));
		} catch (IOException e) {
			Log.crash("Resource Loading Error, could not get DataStream", new RuntimeException(e));
		}
		return out;
	}

	@Override
	public Optional<String> asString() {
		Optional<String> out = Optional.empty();
		try {
			out = Optional.of(IOUtils.toString(openStream(), StandardCharsets.UTF_8.name()));
		} catch (IOException e) {
			Log.crash("Resource Loading Error, could not get as String", new RuntimeException(e));
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
}
